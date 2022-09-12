package com.omgxy.technote.core.entity;

import com.omgxy.technote.system.entity.dto.BaseQuery;
import lombok.Data;

@Data
public class BookQuery extends BaseQuery {

    private String categoryId;
}
