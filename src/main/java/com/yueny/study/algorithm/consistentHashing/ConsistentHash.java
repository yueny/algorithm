package com.yueny.study.algorithm.consistentHashing;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 参考 dubbo ConsistentHashLoadBalance
 */
public class ConsistentHash {

    /**
     * 将以有序Map的形式在内存中缓存每个节点的Hash值对应的物理节点信息。
     * key 为 node.getPath() 取md5后计算hash的前32位
     */
    private final SortedMap<Long, Node> virtualNodes;

    private final int replicaNumber;

    private final int identityHashCode;

    private static ConsistentHash consistentHash;


    /**
     * 在物理节点为个位数时，虚拟节点可设置为160个，此时可带来较好的表现（160*n个总节点数情况下，如果发生一个节点变动，映射关系变化率基本为1/n，达到预期）。
     *
     * 已知物理节点，虚拟节点数设置为160，可将这160*n的节点计算出Hash值，
     * 以Hash值为key，以物理节点标识为value，以有序Map的形式在内存中缓存，
     * 作为后续计算数据对象对应的物理节点时的查询数据。
     *
     * virtualNodes中缓存着所有虚拟节点Hash值对应的物理节点信息。
     *
     * @param nodes
     */
    public ConsistentHash(List<Node> nodes) {
        this.virtualNodes = new TreeMap<>();
        this.identityHashCode = identityHashCode(nodes);

        // 虚拟节点数
        this.replicaNumber = 160;
        // 第一层循环为物理节点数
        for (Node node : nodes) {
            // 第二第三层循环为开辟虚拟节点数。 目的是： 将每个物理节点虚拟出一定数量的虚拟节点，分散到这个值空间上。
            // 此处是给每个物理节点虚拟出 replicaNumber / 4 * 4 个虚拟节点。 一个真实节点对应 replicaNumber 个虚拟节点
            for (int i = 0; i < replicaNumber / 4; i++) {
                /**
                 * 可以选择服务器的ip或主机名作为关键字进行哈希，这样每台机器就能确定其在哈希环上的位置，
                 * 这里假设将N台服务器使用ip地址(getPath)哈希后在环空间的位置
                 */
                byte[] digestMd5 = md5(node.getPath() + i);

                for (int h = 0; h < 4; h++) {
                    // 此处 总计循环次数为  nodes.size() * (replicaNumber / 4) * 4
                    // = nodes.size() 物理节点数 * replicaNumber 虚拟节点数  = 1600
                    long hashj = hash(digestMd5, h);

                    /**
                     * key 为计算的hash结果。 value为存储值
                     *
                     * key:  节点地址
                     */
                    virtualNodes.put(hashj, node);
                }
            }
        }
    }

    private static int identityHashCode(List<Node> nodes){
//        Collections.sort(nodes);
//        StringBuilder sb = new StringBuilder();
//        for (String s: nodes
//             ) {
//            sb.append(s);
//        }
//        return sb.toString().hashCode();

        // 根据对象在内存中的地址算出来的一个数值。identityHashCode是根据Object类hashCode()方法来计算hash值，无论子类是否重写了hashCode()方法。
        // 如果重载了hashCode()方法，而又想获未重载之前的object.hashCode(),则可以使用System.identityHashCode()
        // 目的： 加速对象去重
        return System.identityHashCode(nodes);
    }

    public static Node select(Trigger trigger, List<Node> nodes) {
        // 如果物理节点没有变化，则 identityHashCode 不会有变化
        int _identityHashCode = identityHashCode(nodes);
        if (
                // 第一次初始化，consistentHash == null
                consistentHash == null
                // 物理节点有变化
                || consistentHash.identityHashCode != _identityHashCode) {
            synchronized (ConsistentHash.class) {
                if (consistentHash == null || consistentHash.identityHashCode != _identityHashCode) {
                    consistentHash = new ConsistentHash(nodes);
                }
            }
        }
        return consistentHash.select(trigger);
    }

    /**
     * 顺时针找下一个Node Hash值算法
     *
     * @param trigger
     * @return 下一个Node
     */
    public Node select(Trigger trigger) {
        /* 返回一个标记一个Job的唯一标志 */
        String key = trigger.toString();

        /* 计算Hash值 */
        byte[] digest = md5(key);
        long  hash = hash(digest, 0);

        /* 从节点Hash值中按规则寻找 */
        Node node = sekectForKey(hash);

        return node;
    }
    /**
     * 从节点Hash值中按规则寻找。
     *
     * 沿着顺时针方向，数据hash值向后找到第一个Node Hash值即认为该数据hash值对应的数据映射到该Node上。
     *
     * @param hash hsah 值
     * @return
     */
    private Node sekectForKey(long hash) {
        Node node;
        Long fromKey = hash;

        if (!virtualNodes.containsKey(fromKey)) {
            // 截取 map里面键key 大于等于 fromKey 的所有元素
            SortedMap<Long, Node> tailMap = virtualNodes.tailMap(fromKey);
            if (tailMap.isEmpty()) {
                // 如果 tailMap 为空即没有比fromKey大的键， 则返回 virtualNodes 集合中最小Key的元素值
                fromKey = virtualNodes.firstKey();
            } else {
                // 否则返回 tailMap 集合中最小Key的元素值， 即第一个 Node
                fromKey = tailMap.firstKey();
            }
        }

        node = virtualNodes.get(fromKey);
        return node;
    }

    private long hash(byte[] digestMd5, int number) {
        // 截取其中32位作为映射值
        return (
                ((long) (digestMd5[3 + number * 4] & 0xFF) << 24)
                | ((long) (digestMd5[2 + number * 4] & 0xFF) << 16)
                | ((long) (digestMd5[1 + number * 4] & 0xFF) << 8)
                | (digestMd5[0 + number * 4] & 0xFF)
            )
                & 0xFFFFFFFFL;
    }

    /**
     * 数据 md5 计算
     */
    private byte[] md5(String value) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        md5.reset();
        byte[] bytes;
        try {
            bytes = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        md5.update(bytes);
        return md5.digest();
    }


}

