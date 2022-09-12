package com.omgxy.technote.core.service;

import com.omgxy.technote.core.entity.Page;
import com.omgxy.technote.core.mapper.PageMapper;
import com.omgxy.technote.system.utils.DateUtil;
import com.omgxy.technote.system.utils.TreeListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PageService {

    @Resource
    private PageMapper pageMapper;

    @Transactional(rollbackFor = Exception.class)
    public String addPage(Page page) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        page.setId(UUID.randomUUID().toString());
        Timestamp now = DateUtil.now();
        page.setCreatedAt(now);
        page.setUpdatedAt(now);
        page.setSort(System.currentTimeMillis());
        Integer rowInserted = pageMapper.add(page);

        if (rowInserted != 1) {
            throw new RuntimeException("add page failed");
        }
        return uuid;
    }

    public void updatePage(Page page) {
        if (StringUtils.isBlank(page.getId())) {
            throw new RuntimeException("page id not found");
        }
        page.setUpdatedAt(DateUtil.now());
        Integer rowUpdated = pageMapper.update(page);

        if (rowUpdated != 1) {
            throw new RuntimeException("update page failed");
        }
    }

    public void deletePage(String id) {
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("page id not found");
        }
        Integer rowDeleted = pageMapper.delete(id);

        if (rowDeleted != 1) {
            throw new RuntimeException("delete page failed");
        }
    }

    public Page getPage(String id) {
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("page id not found");
        }
        Page page = pageMapper.get(id);
        if (page == null) {
            throw new RuntimeException("page not found");
        }
        return page;
    }

    public List<Page> listPage(String bookId) {
        List<Page> list = pageMapper.list(bookId);
        for (Page page : list) {
            page.setChildren(new ArrayList<>());
        }
        return new TreeListUtil<Page>().convertListToTree(list);
    }
}
