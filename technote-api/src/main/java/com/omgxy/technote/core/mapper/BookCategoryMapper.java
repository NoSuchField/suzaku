package com.omgxy.technote.core.mapper;

import com.omgxy.technote.core.entity.Category;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCategoryMapper {

    void deleteByBookId(@Param("bookId") String bookId);

    void deleteByCategoryId(@Param("categoryId") String categoryId);

    void addBatch(@Param("bookId") String bookId, @Param("categoryIdList") List<String> categoryIdList);

    List<Category> getByBookId(@Param("bookId") String bookId);
}
