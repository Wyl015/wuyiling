package com.wuyiling.worktest.Utils.page;

import com.yuuwei.faceview.common.WebSearchResult;
import com.yuuwei.faceview.domain.dto.common.PageDTO;

import java.util.List;

/**
 * @author: lingjun.jlj
 * @date: 2019/12/24 15:04
 * @description: 内存分页工具
 */
public class PageHelper<T> {

    public PageHelper() {
    }

    /**
     * 数组分页
     *
     * @param list 需要分页的数组
     * @param page 起始页，从1开始
     * @param size 每页大小
     */
    public WebSearchResult<T> startPage(List<T> list, int page, int size) {
        int total = list.size();
        if (page < 1) {
            throw new RuntimeException("分页起始页必须从1开始");
        }
        int start = (page - 1) * size;
        int end = start + size;
        if (end > total) {
            end = total;
        }
        List<T> rows = list.subList(start, end);

        WebSearchResult<T> result = new WebSearchResult<>();
        result.setTotal(String.valueOf(total));
        result.setRows(rows);
        return result;
    }

    /**
     * 计算分页数据
     *
     * @param pageNum  起始页
     * @param pageSize 每页大小
     * @return
     */
    public static PageDTO calculateStartAndEndRow(int pageNum, int pageSize) {
        int startRow = pageNum > 0 ? (pageNum - 1) * pageSize : 0;
        return new PageDTO(startRow, pageSize);
    }


    public static void main(String[] args) {
        System.out.println(calculateStartAndEndRow(0, 20));
        System.out.println(calculateStartAndEndRow(1, 20));
        System.out.println(calculateStartAndEndRow(2, 20));
        System.out.println(calculateStartAndEndRow(3, 20));
    }

}
