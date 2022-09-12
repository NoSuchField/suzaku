package com.omgxy.technote.core.mapper;

import com.omgxy.technote.core.entity.Book;
import com.omgxy.technote.core.entity.BookQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMapper {
    Integer add(Book book);

    Integer update(Book book);

    Integer delete(@Param("id") String id);

    Book get(@Param("id") String id);

    Integer count(BookQuery query);

    List<Book> list(BookQuery query);

}
