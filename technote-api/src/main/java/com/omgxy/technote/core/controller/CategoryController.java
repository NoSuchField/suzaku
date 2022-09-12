package com.omgxy.technote.core.controller;

import com.omgxy.technote.core.entity.Category;
import com.omgxy.technote.core.service.CategoryService;
import com.omgxy.technote.system.ResultEnum;
import com.omgxy.technote.system.entity.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public Response<String> addCategory(@RequestBody Category category) {
        return new Response<>(ResultEnum.OK, categoryService.addCategory(category));
    }

    @PostMapping("/update")
    public Response<String> updateCategory(@RequestBody Category category) {
        categoryService.updateCategory(category);
        return new Response<>(ResultEnum.OK);
    }

    @PostMapping("/delete")
    public Response<String> deleteCategory(@RequestParam("id") String id) {
        categoryService.deleteCategory(id);
        return new Response<>(ResultEnum.OK);
    }

    @PostMapping("/list")
    public Response<List<Category>> listCategory() {
        return new Response<>(ResultEnum.OK, categoryService.listCategory());
    }
}
