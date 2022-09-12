package com.omgxy.technote.core.service;

import com.omgxy.technote.core.entity.Book;
import com.omgxy.technote.core.entity.Category;
import com.omgxy.technote.core.entity.BookQuery;
import com.omgxy.technote.core.mapper.BookCategoryMapper;
import com.omgxy.technote.core.mapper.BookMapper;
import com.omgxy.technote.system.entity.dto.Page;
import com.omgxy.technote.system.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookService {

    @Resource
    private BookMapper bookMapper;

    @Resource
    private BookCategoryMapper bookCategoryMapper;

    @Transactional(rollbackFor = Exception.class)
    public String addBook(Book book) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        book.setId(UUID.randomUUID().toString());
        Timestamp now = DateUtil.now();
        book.setCreatedAt(now);
        book.setUpdatedAt(now);
        book.setSort(System.currentTimeMillis());
        Integer rowInserted = bookMapper.add(book);

        bookCategoryMapper.deleteByBookId(book.getId());

        List<String> knowledgeBaseCategoryIdList = book.getCategoryIdList();

        if (CollectionUtils.isNotEmpty(knowledgeBaseCategoryIdList)) {
            bookCategoryMapper.addBatch(book.getId(), knowledgeBaseCategoryIdList);
        }

        if (rowInserted != 1) {
            throw new RuntimeException("add book failed");
        }
        return uuid;
    }

    public void updateBook(Book book) {
        if (StringUtils.isBlank(book.getId())) {
            throw new RuntimeException("book id not found");
        }
        book.setUpdatedAt(DateUtil.now());
        Integer rowUpdated = bookMapper.update(book);

        bookCategoryMapper.deleteByBookId(book.getId());

        List<String> knowledgeBaseCategoryIdList = book.getCategoryIdList();

        if (CollectionUtils.isNotEmpty(knowledgeBaseCategoryIdList)) {
            bookCategoryMapper.addBatch(book.getId(), knowledgeBaseCategoryIdList);
        }

        if (rowUpdated != 1) {
            throw new RuntimeException("update book failed");
        }
    }

    public void deleteBook(String id) {
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("book id not found");
        }
        Integer rowDeleted = bookMapper.delete(id);

        bookCategoryMapper.deleteByBookId(id);

        if (rowDeleted != 1) {
            throw new RuntimeException("delete book failed");
        }
    }

    public Book getBook(String id) {
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("book id not found");
        }
        Book book = bookMapper.get(id);
        if (book == null) {
            throw new RuntimeException("book not found");
        }
        List<Category> categoryList = book.getCategoryList();
        if (CollectionUtils.isNotEmpty(categoryList)) {
            book.setCategoryIdList(categoryList.stream().map(Category::getId).collect(Collectors.toList()));
        }
        return book;
    }

    public Page<Book> listBook(BookQuery query) {
        query.calcStart();
        Page<Book> page = new Page<>(query.getCurrentPage(), query.getPageSize(), 0);
        Integer total = bookMapper.count(query);
        if (total == 0) {
            page.setList(new ArrayList<>());
            return page;
        }
        page = new Page<>(query.getPageSize(), query.getPageSize(), total);
        List<Book> list = bookMapper.list(query);

        for (Book book : list) {
            List<Category> categoryList = book.getCategoryList();
            if (CollectionUtils.isNotEmpty(categoryList)) {
                book.setCategoryIdList(categoryList.stream().map(Category::getId).collect(Collectors.toList()));
            }
        }

        page.setList(list);
        return page;
    }
}
