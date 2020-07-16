# 一致性Hash算法
## Consistent Hashing算法原理
### 1. 确定hashing值空间
给定值空间2^32，[0,2^32]是所有hash值的取值空间，形象地描述为如下一个环（ring）：
![a](https://static.oschina.net/uploads/space/2018/0120/171401_PArO_251057.png
)

### 2. 节点向值空间映射
将节点Node向这个值空间映射，取Node的Hash值，选取一个可以固定标识一个Node的属性值进行Hashing，假设以字符串形式输入，

之后取Node标识的md5值，然后截取其中32位作为映射值。

算法如下。md5取值如下：
```
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
```
因为映射值只需要32位即可，所以可以利用以下方式计算最终值（number取0即可）：
```
 private long hash(byte[] digest, int number) {
        return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                | (digest[0 + number * 4] & 0xFF))
                & 0xFFFFFFFFL;
    }
```

把n个节点Node通过以上方式取得hash值，映射到环形值空间如下：
![a](https://static.oschina.net/uploads/space/2018/0120/171418_4qVa_251057.png)

算法中，将以有序Map的形式在内存中缓存每个节点的Hash值对应的物理节点信息。
缓存于这个内存变量中：
private final TreeMap<Long, String> virtualNodes 。


###     3. 数据向值空间映射



~  end
