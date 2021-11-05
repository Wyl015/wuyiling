package com.wuyiling.leetcode.Leetcode;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author wuyiling
 * @Date 14:38 2021/11/5
 *
 * @Description
 *      给你一个整数数组 arr 和一个整数 difference，请你找出并返回 arr 中最长等差子序列的长度，该子序列中相邻元素之间的差等于 difference 。
 *      子序列 是指在不改变其余元素顺序的情况下，通过删除一些元素或不删除任何元素而从 arr 派生出来的序列。
 *      1 <= arr.length <= 105
 *      -104 <= arr[i], difference <= 104
 * @Example
 *      输入：arr = [1,5,7,8,5,3,4,2,1], difference = -2
 *      输出：4
 *      解释：最长的等差子序列是 [7,5,3,1]。
 **/

@Data
public class M1218longestSubsequence {
    int[] arr = {1,5,7,8,5,3,4,2,1};
    int difference = -2;

//  Main方法
    public static void main(String[] args) {
        System.out.println("构造");
        M1218longestSubsequence solution = new M1218longestSubsequence();
        System.out.println("开始执行方法=============");
        int result = solution.longestSubsequence1(solution.arr, solution.difference);
        System.out.println("输出结果：" + result);
        System.out.println("小方法测试==============" );
        test();
    }
//  题解

//  方法一：动态规划
    //从左往右遍历 \textit{arr}arr，并计算出以 \textit{arr}[i]arr[i] 为结尾的最长的等差子序列的长度，取所有长度的最大值，即为答案
    public int longestSubsequence1(int[] arr, int difference) {
        int ans = 0;
        Map<Integer, Integer> dp = new HashMap<Integer, Integer>();
        for (int v : arr) {
            dp.put(v, dp.getOrDefault(v - difference, 0) + 1);
            ans = Math.max(ans, dp.get(v));
        }
        return ans;
    }


//  小方法测试
    public static void  test() {
        int[] array = {1,5,7,8,5,3,4,2,1};
        List<Integer> resultList = new ArrayList<>(array.length);
        for (Integer a : array) {
            resultList.add(a);
        }
        System.out.println(resultList);
    }
}



/**
 *
 * @Description 数组转换成List集合
 * 1.使用原生方式，拆分数组，添加到List
 * List<String> resultList = new ArrayList<>(array.length);
 * for (String s : array) {
 *     resultList.add(s);
 * }
 *
 * 2.使用Arrays.asList()
 * List<String> resultList= new ArrayList<>(Arrays.asList(array));
 * 注意：调用Arrays.asList()时，其返回值类型是ArrayList，但此ArrayList是Array的内部类，调用add()时，会报错：java.lang.UnsupportedOperationException，并且结果会因为array的某个值的改变而改变，故需要再次构造一个新的ArrayList。
 *
 * 3.使用Collections.addAll()
 * List<String> resultList = new ArrayList<>(array.length);
 * Collections.addAll(resultList,array);
 *
 * 4.使用List.of()
 * 此方法为 Java9新增方法，定义在List接口内，并且为静态方法，故可以由类名直接调用。
 * List<String> resultList = List.of(array);
 *
 *
 *
 *
 * IDEA 错误: 找不到或无法加载主类 解决方法
 *
 * 在IDEA的使用过程中，经常断掉服务或者重启服务，最近断掉服务重启时突然遇到了一个启动报错：
 * 错误：找不到或无法加载主类
 * 猜测：
 * 1，未能成功编译；
 * 尝试：菜单---》Build---》Rebuild Prodject
 * 结果：启动服务仍然报同样的错误
 * 2，缓存问题；
 * 尝试：菜单---》File---》Invalidate Caches/Restart 选择Invalidate and Restart 或者 只是Invalidate，清除掉缓存，然后Rebuild Project
 * 结果：启动成功，问题解决
 * 设置一下file-->project structure-->Module：
 * paths里面的编译路径Complier output：
 *
 * 使用第二种方法解决
 **/