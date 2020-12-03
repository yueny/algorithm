package com.yueny.study.algorithm.subset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 算法：给一个set打印出所有子集
 *
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-11-20 10:01
 */
public class SubSet {
    public static void main(String[] args) {
        // 2^5， 32个子集
        Integer[] nums = new Integer[]{1, 2, 3};

        List<List<Integer>> method1 = method1(nums);
        System.out.println("method1: " + method1);

        List<List<Integer>> method2 = method2(nums);
        System.out.println("method2: " + method2);

        List<List<Integer>> method3 = method3(nums);
        System.out.println("method3: " + method3);

        List<List<Integer>> method4 = method4(nums);
        System.out.println("method4: " + method4);

        printAllSubSet(nums);
    }

    /**
     * 方式一 ：
     * 思路：
     * 对于例子[1,2,3]来说，最开始是空集，那么我们现在要处理1，就在空集上加1，为[1]。
     * 现在我们有两个自己[]和[1]，下面我们来处理2，我们在之前的子集基础上，每个都加个2，可以分别得到[2]，[1, 2]。那么现在所有的子集合为[], [1], [2], [1, 2]。
     *
     * 同理处理3的情况可得[3], [1, 3], [2, 3], [1, 2, 3], 再加上之前的子集就是所有的子集合了。
     *
     * @param nums
     */
    public static List<List<Integer>> method1(Integer[] nums) {
        // 子集列表
        List<List<Integer>> res = new ArrayList<>();

        // 空集
        res.add(new ArrayList<>());

        if(nums==null||nums.length==0){
            return res;
        }

        // 非空集
        for(int i=0; i< nums.length; i++){
            // 目前已经存在的子集数量
            int size = res.size();

            for(int j=0; j<size; j++){
                // 取得该具体的子集
                List<Integer> cur=new ArrayList<>(res.get(j));
                // 子集中追加 i
                cur.add(nums[i]);

                // 追加后的列表作为新子集
                res.add(new ArrayList<>(cur));
            }
        }

        return res;
    }

    /**
     * 递归法实现子集枚举
     *
     * @param nums
     */
    public static List<List<Integer>> method2(Integer[] nums) {
        // 子集列表
        List<List<Integer>> res = new ArrayList<>();

        Arrays.sort(nums);
        dfs(res, nums, new ArrayList<>(), 0);

        return res;
    }
    /**
     *
     * @param res 输出的结果集合
     * @param nums 原始数组
     * @param tmp   临时对象
     * @param cur 表示当前位置
     */
    private static void dfs(List<List<Integer>> res, Integer[] nums, ArrayList<Integer> tmp, int cur) {
        if (nums.length == cur) {
            res.add(new ArrayList<>(tmp));
        } else {
            tmp.add(nums[cur]);

            dfs(res, nums, tmp, cur + 1);
            tmp.remove(tmp.size() - 1);
            dfs(res, nums, tmp, cur + 1);
        }
    }

    public static  List<List<Integer>> method3(Integer[] nums) {
        // 子集列表
        List<List<Integer>> res = new ArrayList<>();

        backtrack(res, nums, new ArrayList<>(), 0);

        return res;
    }

    private static void backtrack(List<List<Integer>> res, Integer[] nums, ArrayList<Integer> tmp, int start) {
        //走过的所有路径都是子集的一部分，所以都要加入到集合中
        res.add(new ArrayList<>(tmp));

        for(int i = start; i < nums.length; i++){
            //做出选择
            tmp.add(nums[i]);
            //递归
            backtrack(res, nums, tmp, i + 1);
            //撤销选择
            tmp.remove(tmp.size() - 1);
        }
    }

    /**
     * 迭代法实现 子集枚举
     *
     * 思路与算法
     *
     * 记原序列中元素的总数为 nn。原序列中的每个数字 a(i) 的状态可能有两种，即「在子集中」和「不在子集中」。
     *
     * 我们用 1 表示「在子集中」，0 表示不在子集中，那么每一个子集可以对应一个长度为 n 的 0/1 序列，第 i 位表示 a(i) 是否在子集中。
     *
     * @param nums
     * @return
     */
    public static  List<List<Integer>> method4(Integer[] nums) {
        // 子集列表
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();

        int len = nums.length;

        // 获得所有子集数. 1<<nums.length 等价于 2^nums.length,对于n个数字的数组，共2^n个子集
        //子集的长度是2的nums.length次方，这里通过移位计算
        int soo = 1 << len; // or  (int) Math.pow(2, len);
        System.out.println("子集数量：" + soo);

        //遍历从0到length中间的所有数字，根据数字中1的位置来找子集
        for (int mask = 0; mask < soo; ++mask) {
            temp.clear();
            for (int j = 0; j < len; ++j) {
                // 查看对应的二进制
//                String binaryMask = Integer.toBinaryString(mask);
//                String binarySpo = Integer.toBinaryString(1 << j);
                // ((mask >> j) & 1) 指 如果数字 mask 的某一个位置是1，就把数组中对应的数字添加到集合
                // 此处两种写法均可以：` (mask & (1 << j)) != 0 ` 或者 ` ((mask >> j) & 1) == 1 `
                if (((mask >> j) & 1) == 1) {
                    temp.add(nums[j]);
                }
            }

            // 添加一个计算出的子集
            res.add(new ArrayList<>(temp));
        }

        return res;
    }

    public static  void printAllSubSet(Integer[] nums) {
        int len = nums.length;
        int i = 0;

        // 获得所有子集数
        int count = (int) Math.pow(2, len);
        System.out.println("空集 : {}");

        // 输出所有子集
        for (i = 1; i < count; i++) {
            // 十进制转换为二进制输出. 将整数转换成二进制字符串，如果前面为0则会被去掉，比如001,则会显示为1
            String binaryStr = Integer.toBinaryString(i);
            System.out.println("二进制为: " + binaryStr + ", 对应的子集为: ");

            // 二进制字符串的长度
            int binLen = binaryStr.length() - 1;
            // 遍历二进制字符串，(每次遍历输出一个子集)
            for (int j = len - 1; j >= 0 && binLen >= 0; j--, binLen--) {
                // 二进制 数为1的，则输出对应位置的数值
                if (binaryStr.charAt(binLen) == '1') {
                    System.out.print(nums[j] + " ");
                }
            }
            System.out.println();
            // 开始下一个子集输出
        }
    }
}