package com.omgxy.technote.core.service;

import com.omgxy.technote.core.entity.Category;
import com.omgxy.technote.core.mapper.CategoryMapper;
import com.omgxy.technote.core.mapper.BookCategoryMapper;
import com.omgxy.technote.system.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private BookCategoryMapper bookCategoryMapper;

    @Transactional(rollbackFor = Exception.class)
    public String addCategory(Category category) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        category.setId(UUID.randomUUID().toString());
        Timestamp now = DateUtil.now();
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
        category.setSort(System.currentTimeMillis());
        Integer rowInserted = categoryMapper.add(category);
        if (rowInserted != 1) {
            throw new RuntimeException("add book failed");
        }
        return uuid;
    }

    public void updateCategory(Category category) {
        if (StringUtils.isBlank(category.getId())) {
            throw new RuntimeException("book id not found");
        }
        category.setUpdatedAt(DateUtil.now());
        Integer rowUpdated = categoryMapper.update(category);
        if (rowUpdated != 1) {
            throw new RuntimeException("update book failed");
        }
    }

    public void deleteCategory(String id) {
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("book id not found");
        }
        Integer rowDeleted = categoryMapper.delete(id);

        bookCategoryMapper.deleteByCategoryId(id);

        if (rowDeleted != 1) {
            throw new RuntimeException("delete book failed");
        }
    }

    public List<Category> listCategory() {
        return categoryMapper.list();
    }
}
