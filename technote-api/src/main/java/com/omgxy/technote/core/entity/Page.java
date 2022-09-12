package com.omgxy.technote.core.entity;

import com.omgxy.technote.system.entity.dto.TreeNode;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Page {

    public String id;

    private String label;

    private String content;

    private String bookId;

    public String parentId;

    public List<Page> children;

    public List<TreeNode> path;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Long sort;

}
