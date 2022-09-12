package com.omgxy.technote.core.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Category {

    private String id;

    private String name;

    private String icon;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Long sort;

}
