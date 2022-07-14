package com.diduweiwu.base;

import lombok.Data;

/**
 * @author itest
 */
@Data
public class BasePageVo {
    private int pageNum;
    private int pageSize;

    public int getPageNum() {
        return pageNum - 1;
    }
}
