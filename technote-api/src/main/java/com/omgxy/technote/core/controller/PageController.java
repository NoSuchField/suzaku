package com.omgxy.technote.core.controller;

import com.omgxy.technote.core.entity.Page;
import com.omgxy.technote.core.service.PageService;
import com.omgxy.technote.system.ResultEnum;
import com.omgxy.technote.system.entity.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/page")
public class PageController {

    @Autowired
    private PageService pageService;

    @PostMapping("/add")
    public Response<String> addPage(@RequestBody Page page) {
        return new Response<>(ResultEnum.OK, pageService.addPage(page));
    }

    @PostMapping("/update")
    public Response<String> updatePage(@RequestBody Page page) {
        pageService.updatePage(page);
        return new Response<>(ResultEnum.OK);
    }

    @DeleteMapping("/delete")
    public Response<String> deletePage(@RequestParam("id") String id) {
        pageService.deletePage(id);
        return new Response<>(ResultEnum.OK);
    }

    @GetMapping("/detail")
    public Response<Page> getPage(@RequestParam("id") String id) {
        return new Response<>(ResultEnum.OK, pageService.getPage(id));
    }

    @GetMapping("/list")
    public Response<List<Page>> listPage(@RequestParam(value = "bookId", required = false) String bookId) {
        return new Response<>(ResultEnum.OK, pageService.listPage(bookId));
    }

}
