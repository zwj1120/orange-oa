package com.xcr.orange.oa.common.bean;

import com.gdk.jdbc.util.PageBounds;

public class BaseQueryDTO {

    private PageBounds pageBounds;

    public PageBounds getPageBounds() {
        return pageBounds;
    }

    public void setPageBounds(PageBounds pageBounds) {
        this.pageBounds = pageBounds;
    }
}
