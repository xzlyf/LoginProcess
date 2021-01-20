package com.xz.xlogin.entity;

import java.io.Serializable;

/**
 * @Author: xz
 * @Date: 2020/11/22
 * <p>
 * 1.0 服务端分页返回实体标准
 */
public class PagingResult<T> extends ApiResult<T> implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * 当前页为第几页
     */
    private int page;

    /**
     * 每页大小
     */
    private int size;

    /**
     * 总共有多少页
     */
    private int totalPages;

    /**
     * 总共有多少条数据
     */
    private long totalElements;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
