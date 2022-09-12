package com.omgxy.technote.core.mapper;

import com.omgxy.technote.core.entity.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageMapper {
    Integer add(Page page);

    Integer update(Page page);

    Integer delete(@Param("id") String id);

    Page get(@Param("id") String id);

    List<Page> list(@Param("bookId") String bookId);

}
