package com.wuyiling.leetcode.Leetcode;

import javafx.util.Pair;

import java.util.PriorityQueue;

public class Solution {

    public static void main(String[] args) {
        //入参
        int[][] heightMap = {{1,4,3,1,3,2},{3,2,1,3,2,4},{2,3,3,2,3,1}};
        //执行
        int result = trapRainWater(heightMap);
        //输出结果
        System.out.println(result);
    }

    public static int trapRainWater(int[][] heightMap) {
        int m = heightMap.length;
        int n = heightMap[0].length;

        if (m <= 2 || n <= 2) {
            return 0;
        }
        boolean[][] visit = new boolean[m][n];
        PriorityQueue<Pair<Integer, Integer>> priorityQueue = new PriorityQueue<>((o1, o2) -> o1.getValue() - o1.getValue());

        for(int i = 0; i < m; i ++) {
            for (int j = 0; j < n; j ++){
                if (i == 0 || j == 0 || i == m-1 || j == n-1) {
                    priorityQueue.offer(new Pair<>(i * n + j, heightMap[i][j]));
                    visit[i][j] = true;
                }
            }
        }

        int sum = 0;
        int[] derection = {-1,0,1,0,-1};
        int x, y;
        while(!priorityQueue.isEmpty()){
            Pair<Integer, Integer> dot = priorityQueue.poll();
            for (int i = 0; i < 4; i ++){
                x = derection[i] + dot.getKey() / n;
                y = derection[i + 1] + dot.getKey() % n;
                if (x <= 0 || x >= m || y <= 0 || y >= n ||
                        visit[x][y]) {
                    continue;
                }

                if (dot.getValue() > heightMap[x][y]) {
                    sum += dot.getValue()-heightMap[x][y];
                }
                priorityQueue.offer(new Pair<>(x * n + y, Math.max(dot.getValue(), heightMap[x][y])));
                heightMap[x][y] = Math.max(dot.getValue(), heightMap[x][y]);
                visit[x][y] = true;
            }
        }

        return sum;
    }
}
