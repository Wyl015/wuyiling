package com.wuyiling.leetcode.QuestionBank;

import org.junit.jupiter.api.Test;

import java.util.*;

public class NO_407_TrapRainWater {

    String inputS = "[[1,4,3,1,3,2],[3,2,1,3,2,4],[2,3,3,2,3,1]]";
    int[][] inputI = {{1,4,3,1,3,2},{3,2,1,3,2,4},{2,3,3,2,3,1}};
    /*
     * @Author wyl
     * @Description 失败
     * @Date 16:22 2021/11/3
     * @Param []
     * @return void
     **/
    @Test
    public void test(){
        String[] result = inputS.split("\\],\\[");
        System.out.println(result.length);
        int[][] ints = new int[result.length][];
        System.out.println(ints.length);
        for (String s : result){
            s = s.replace("[","").replace("]", "");

            System.out.println(s);
            String[] split = s.split(",");



        }
    }

    @Test
    public void test2(){
        char[] chars = inputS.toCharArray();
        System.out.println(chars.length);
        System.out.println(chars);

    }
    @Test
    public void MainTest() {
        int result = trapRainWater(inputI);
        System.out.println(result);
    }


    public int trapRainWater(int[][] heightMap) {
        if (heightMap.length <= 2 || heightMap[0].length <= 2) {
            return 0;
        }
        int m = heightMap.length;
        int n = heightMap[0].length;
        boolean[][] visit = new boolean[m][n];
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> o1[1] - o2[1]);
        //将边缘无法纯水的空间，放入优先队列，设置 visit 为 T
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    pq.offer(new int[]{i * n + j, heightMap[i][j]});
                    visit[i][j] = true;
                }
            }
        }
        int res = 0;
        //绕圈方向
        int[] dirs = {-1, 0, 1, 0, -1};
        while (!pq.isEmpty()) {
            // 从边缘最底值开始遍历，访问附近未visit的点，比较后往里面放水
            // 弹出该点
            int[] curr = pq.poll();
            for (int k = 0; k < 4; ++k) {
                int nx = curr[0] / n + dirs[k];
                int ny = curr[0] % n + dirs[k + 1];
                // 在范围中，且visit[nx][ny]未访问
                if (nx >= 0 && nx < m && ny >= 0 && ny < n && !visit[nx][ny]) {
                    //该点 高于周边点
                    if (curr[1] > heightMap[nx][ny]) {
                        //res 加水量
                        res += curr[1] - heightMap[nx][ny];
                    }
                    // visit[nx][ny] 1.小于边缘，加满水到边缘点；2.高于边缘，保留高度
                    // 将该点放入优先队列 设置为已访问
                    pq.offer(new int[]{nx * n + ny, Math.max(heightMap[nx][ny], curr[1])});
                    visit[nx][ny] = true;
                    // 执行到下一个点，访问相邻点完毕
                }
                // 访问相邻点完毕
                // 到优先队列中的最低点，重复循环，直至优先队列 中 所有数全部弹出
            }
        }
        return res;
    }

}
