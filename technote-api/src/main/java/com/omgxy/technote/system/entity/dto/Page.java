package com.omgxy.technote.system.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class Page<T> {

    private List<T> list;

    private Integer totalCount;

    private Integer currentPage;

    @JsonIgnore
    private Integer start;

    private Integer pageSize;

    private Integer totalPage;

    public Page(BaseQuery query, Integer totalCount) {
        this(query.getPageSize(), query.getCurrentPage(), totalCount);
    }

    public Page(Integer pageSize) {
        if (pageSize == null) {
            return;
        }
        this.pageSize = pageSize <= 0 ? 10 : pageSize;
    }

    public Page(Integer currentPage, Integer pageSize) {

        if (currentPage == null || pageSize == null) {
            return;
        }

        this.currentPage = currentPage <= 0 ? 10 : currentPage;
        this.pageSize = pageSize <= 0 ? 10 : pageSize;
        this.start = (currentPage - 1) * pageSize;
    }

    public Page(Integer currentPage, Integer pageSize, Integer totalCount) {
        this(currentPage, pageSize);
        this.totalCount = totalCount;

        if (currentPage == null || pageSize == null) {
            return;
        }

        if (totalCount % pageSize == 0) {
            this.totalPage = totalCount / pageSize;
        } else {
            this.totalPage = totalCount / pageSize + 1;
        }

        if (list == null) {
            list = Collections.emptyList();
        }

    }

    public Integer getTotalPage() {
        if (this.totalPage == null) {
            if (totalCount % pageSize == 0) {
                this.totalPage = totalCount / pageSize;
            } else {
                this.totalPage = totalCount / pageSize + 1;
            }
        }
        return this.totalPage;
    }

    public List<T> getList() {
        return list == null ? Collections.emptyList() : list;
    }

    public void setList(List<T> list) {
        this.list = list == null ? Collections.emptyList() : list;
    }
}
