package com.omgxy.technote.core.mapper;

import com.omgxy.technote.core.entity.Category;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {

    Integer add(Category category);

    Integer update(Category category);

    Integer delete(@Param("id") String id);

    List<Category> list();
}
