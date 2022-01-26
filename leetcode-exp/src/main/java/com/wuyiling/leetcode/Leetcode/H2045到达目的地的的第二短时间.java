package com.wuyiling.leetcode.Leetcode;

import java.util.*;

public class H2045到达目的地的的第二短时间 {



    public static void main(String[] args) {
        int n = 5;
        int[][] edges = {{1,2},{2,3},{2,4},{4,5}};
        int time = 1;
        int change = 5;
        int i = Solution.secondMinimum(n, edges, time, change);
        System.out.println(i);

    }



    //解:

    static class Solution {
        public static int secondMinimum(int n, int[][] edges, int time, int change) {
            List<Integer>[] graph = new List[n + 1];
            for (int i = 0; i <= n; i++) {
                graph[i] = new ArrayList<Integer>();
            }
            for (int[] edge : edges) {
                graph[edge[0]].add(edge[1]);
                graph[edge[1]].add(edge[0]);
            }

            // path[i][0] 表示从 1 到 i 的最短路长度，path[i][1] 表示从 1 到 i 的严格次短路长度
            int[][] path = new int[n + 1][2];
            for (int i = 0; i <= n; i++) {
                Arrays.fill(path[i], Integer.MAX_VALUE);
            }

            path[1][0] = 0;
            Queue<int[]> queue = new ArrayDeque<int[]>();
            queue.offer(new int[]{1, 0});

            // 截至n的最短路径计算完毕
            while (path[n][1] == Integer.MAX_VALUE) {
                int[] arr = queue.poll();
                int cur = arr[0], len = arr[1];
                for (int next : graph[cur]) {
                    if (len + 1 < path[next][0]) {
                        // 最短路径
                        path[next][0] = len + 1;
                        queue.offer(new int[]{next, len + 1});
                    } else if (len + 1 > path[next][0] && len + 1 < path[next][1]) {
                        // 次短路径
                        path[next][1] = len + 1;
                        queue.offer(new int[]{next, len + 1});
                    }
                }
            }

            // 加上等待红灯的时间
            int ret = 0;
            for (int i = 0; i < path[n][1]; i++) {
                if (ret % (2 * change) >= change) {
                    ret = ret + (2 * change - ret % (2 * change));
                }
                ret = ret + time;
            }
            return ret;
        }
    }
}
