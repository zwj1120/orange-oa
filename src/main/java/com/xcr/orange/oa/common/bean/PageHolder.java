package com.xcr.orange.oa.common.bean;

import com.gdk.jdbc.util.PageBounds;

import java.util.List;

/**
 * 分页查询结果
 *
 * @author Administrator
 * @date: 2020/11/10 10:44
 */
public class PageHolder<T> {

    private PageBounds pageBounds;

    private long totalRows;

    private List<T> datas;

    public PageHolder() {
    }

    public PageHolder(PageBounds pageBounds, int totalRows, List<T> datas) {
        this.pageBounds = pageBounds;
        this.totalRows = totalRows;
        this.datas = datas;
    }

    public PageBounds getPageBounds() {
        return pageBounds;
    }

    public void setPageBounds(PageBounds pageBounds) {
        this.pageBounds = pageBounds;
    }

    public long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
