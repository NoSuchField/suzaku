package com.omgxy.technote.system.entity.dto;

import lombok.Data;

@Data
public class BaseQuery {

    /**
     * 当前页码
     */
    private Integer pageSize;

    /**
     * 起始位置
     */
    private Integer start;

    /**
     * 分页大小
     */
    private Integer currentPage;

    public void calcStart() {
        start = (pageSize == null || currentPage == null) ? 0 : (currentPage - 1) * pageSize;
    }
}
