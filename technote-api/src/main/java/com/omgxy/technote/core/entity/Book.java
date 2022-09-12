package com.omgxy.technote.core.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Book {

    private String id;

    private String name;

    private String description;

    private String coverImage;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private List<String> categoryIdList;

    private List<Category> categoryList;

    private Long sort;

}
