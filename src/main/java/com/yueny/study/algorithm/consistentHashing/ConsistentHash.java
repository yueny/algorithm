package com.yueny.study.algorithm.consistentHashing;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash {

    private final TreeMap<Long, Node> virtualNodes;

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
        for (Node node : nodes) {
            for (int i = 0; i < replicaNumber / 4; i++) {
                byte[] digest = md5(node.getPath() + i);
                for (int h = 0; h < 4; h++) {
                    long m = hash(digest, h);
                    virtualNodes.put(m, node);
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
        return System.identityHashCode(nodes);
    }

    public static Node select(Trigger trigger, List<Node> nodes) {
        int _identityHashCode = identityHashCode(nodes);
        if (consistentHash == null || consistentHash.identityHashCode != _identityHashCode) {
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
     * 从节点Hash值中按规则寻找
     *
     * @param hash hsah 值
     * @return
     */
    private Node sekectForKey(long hash) {
        Node node;
        Long key = hash;

        if (!virtualNodes.containsKey(key)) {
            SortedMap<Long, Node> tailMap = virtualNodes.tailMap(key);
            if (tailMap.isEmpty()) {
                key = virtualNodes.firstKey();
            } else {
                key = tailMap.firstKey();
            }
        }

        node = virtualNodes.get(key);
        return node;
    }

    private long hash(byte[] digest, int number) {
        return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                | (digest[0 + number * 4] & 0xFF))
                & 0xFFFFFFFFL;
    }

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

