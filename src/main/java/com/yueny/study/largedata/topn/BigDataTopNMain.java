package com.yueny.study.largedata.topn;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.lang3.time.StopWatch;

import java.io.*;
import java.util.*;

/**
 * 1、 有100W个关键字(或者字符串)，每个字符串最大长度，小于等于50字节。
 *
 * 用高效的算法找出top10的热词，并对内存的占用不超过1MB。
 *
 * <pre>
      提示：
        先把100W个关键字hash映射到小文件，根据题意，100W * 50B = 5010^6 B = 50M，而内存只有1M，
        故干脆搞一个hash函数 % 50，分解成50个小文件；

        针对对每个小文件依次运用hashmap(key，value)完成每个key的value次数统计，后用堆找出每个小文件中value次数最大的top 10；
        -最后依次对每两小文件的top 10归并，得到最终的top 10。

        此外，很多细节需要注意下，举个例子，如若hash映射后导致分布不均的话，有的小文件可能会超过1M，
        故为保险起见，你可能会说根据数据范围分解成50~500或更多的小文件.
        但到底是多少呢？勿纠结具体数字，保持思路清晰关注细节即可。
 * </pre>
 */
public class BigDataTopNMain {
    /**
     * 大文件位置
     */
    public  static  String BIG_FILE_PATH="/Users/yueny/Downloads/temp";
    public  static  String BIG_FILE_NAME = BIG_FILE_PATH + "/bigdata.txt";
    public  static  String SORT_FILE_NAME= BIG_FILE_PATH + "/bigdatasort.txt";

    /**
     * 100， 即 取的 top  N
     */
    public  static  Integer LIMIT=100;

    /**
     * 换行符
     */
    public static String LINE_SEPARATOR="\r\n";


    public static void main(String[] args) throws  Exception{
//        BigDataTopNMain bigData = new BigDataTopNMain();
//        //生成大文件
//        bigData.dataWrite();

//        int tmp = Math.abs("121.76.244.225".hashCode() % LIMIT);
//        System.out.println("return " + tmp);

        // 切割
        StopWatch watch = new StopWatch();
        watch.start();

        Set<String> fileNameSets = hashToSmallFiles();
        watch.split();

        //统计出现频率最高的词汇
        List<Map.Entry<String, Integer>> returnTopN = getSort(fileNameSets);
        watch.split();

        watch.stop();
        System.out.println("return" + LIMIT + ":" + returnTopN +
                "， 耗时：" + watch.getTime() + "ms");
    }

    /**
     * 大文件分片排序, 将大文件hash成 小文件,顺序读写
     *
     * 则显然在单个文件中，topK的才是最终整体有可能的topK（一个文件中可能出现相同次数的，也要考虑进来）
     *
     * @return
     */
    private static Set<String> hashToSmallFiles() {
        // 生成的子文件名列表
        Set<String> fileNameSets = new TreeSet<>();

        // 维护 FileWriter 的哈希表， Key为
        Map<String, FileWriter> fileWriters = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BIG_FILE_NAME))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String fileName = proFileName(line);

                if(!fileWriters.containsKey(fileName)){
                    File file = new File(fileName);

                    if(!file.getParentFile().exists()){
                        Files.createParentDirs(file);
                    }
                    if(!file.exists()){
                        file.createNewFile();
                    }

                    FileWriter tmpWriter = new FileWriter(fileName);
                    fileWriters.put(fileName,tmpWriter);
                }

                FileWriter tmpWriterTemp = fileWriters.get(fileName);
                // 往子文件内写数据
                tmpWriterTemp.write(line + LINE_SEPARATOR);

                fileNameSets.add(fileName);
            }

            return fileNameSets;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //文件输入流关闭
            for (FileWriter ff : fileWriters.values()) {
                try {
                    ff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     *  生成散列文件名
     *
     * @param line 读取文件每行
     * @return  生成散列文件名
     */
    private static String proFileName(String line) {
        int len=line.length();
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(BIG_FILE_PATH  + "/separate/");

        // ip 解析
        String ipVal = Lists.newArrayList(Splitter.on("|").split(line)).get(0);
        // 散列成 LIMIT 个小文件
        int tmp = Math.abs(ipVal.hashCode() % LIMIT);

        stringBuffer.append("bigdata");
        stringBuffer.append(tmp);

        stringBuffer.append(".txt");

        return stringBuffer.toString();
    }


    /**
     * 排序
     *
     * 注意：特殊情况未处理，比如取前100的时候，120的单词频率和100一样
     *
     * @param fileNameSets 生成的子文件名列表
     * @return
     * @throws Exception
     */
    public static List<Map.Entry<String,Integer>> getSort(Set<String> fileNameSets) throws Exception{
        // 总的 top N
        List<Map.Entry<String,Integer>> entrysReturn=new ArrayList<>();

        for(String fileNameSet: fileNameSets){
            // 每个子文件内， 每个统计字符出现的次数
            Map<String, Integer> countMap = new HashMap<>();

            try (BufferedReader readerSet = new BufferedReader(new FileReader(fileNameSet))) {
                // 行数据
                String lineSet="";
                while ((lineSet = readerSet.readLine()) != null) {
                    String ipVal = Lists.newArrayList(Splitter.on("|").split(lineSet)).get(0);

                    String keyVal = ipVal;
                    if(countMap.containsKey(keyVal)){
                        int value = countMap.get(keyVal);

                        countMap.put(keyVal, value + 1);
                    }else {
                        countMap.put(keyVal, 1);
                    }
                }
            }

            List<Map.Entry<String,Integer>> entrys=new ArrayList<>(countMap.entrySet());
            Collections.sort(entrys, new Comparator<Map.Entry<String, Integer>>() {
                // 升序排序
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });

            if(entrys.size()>LIMIT){
                entrysReturn.addAll(entrys.subList(0, LIMIT));
            }else{
                entrysReturn.addAll(entrys);
            }
        }

        // 总topN 的再次排序
        Collections.sort(entrysReturn, new Comparator<Map.Entry<String, Integer>>() {
            // 升序排序
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        if(entrysReturn.size()>LIMIT){
            return entrysReturn.subList(0,LIMIT);
        }else{
            return entrysReturn;
        }
    }

    /** 生成数据方法start */
    public void dataWrite() throws IOException  {
        File file=new File(BIG_FILE_NAME);
        if(!file.exists()){
            file.createNewFile();
        }

        FileOutputStream out= new FileOutputStream(file,true);

        // 一千万
        for(int i=0; i<10000000; i++){
            StringBuffer sb=new StringBuffer();
            // 随机生成 IP
//            sb.append(IpGenerateUtil.getRandomIp());
            sb.append("|");
            sb.append(randomWord());
            sb.append("\r\n");
            out.write(sb.toString().getBytes("utf-8"));
        }
        out.close();
    }

    /**
     * 随机获取长度为12~20的大小写字母混杂的“单词”
     */
    private String randomWord() {
        // 12~20长度，包含12及20
        int length = 2 + (int) (Math.random() * 5);
        String word = "";
        for (int i = 0; i < length; i++) {
            word += (char) randomChar();
        }
        return word;
    }

    /**
     * 随机获取'a'~'z' 和 'A'~ 'Z'中的任一字符
     *
     * 'A'~ 'Z'对应ASCII值：65~90
     *
     * 'a'~'z'对应ASCII值：97~122
     *
     * @return
     */
    private byte randomChar() {
        // 0<= Math.random()< 1
        int flag = (int) (Math.random() * 2);// 0小写字母1大写字母
        byte resultBt;
        if (flag == 0) {
            byte bt = (byte) (Math.random() * 26);// 0 <= bt < 26
            resultBt = (byte) (65 + bt);
        } else {
            byte bt = (byte) (Math.random() * 26);// 0 <= bt < 26
            resultBt = (byte) (97 + bt);
        }
        return resultBt;
    }

}
