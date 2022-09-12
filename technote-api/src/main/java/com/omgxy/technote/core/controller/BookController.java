package com.omgxy.technote.core.controller;

import com.omgxy.technote.core.entity.Book;
import com.omgxy.technote.core.entity.BookQuery;
import com.omgxy.technote.core.service.BookService;
import com.omgxy.technote.system.ResultEnum;
import com.omgxy.technote.system.entity.dto.Page;
import com.omgxy.technote.system.entity.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public Response<String> addBook(@RequestBody Book book) {
        return new Response<>(ResultEnum.OK, bookService.addBook(book));
    }

    @PostMapping("/update")
    public Response<String> updateBook(@RequestBody Book book) {
        bookService.updateBook(book);
        return new Response<>(ResultEnum.OK);
    }

    @PostMapping("/delete")
    public Response<String> deleteBook(@RequestParam("id") String id) {
        bookService.deleteBook(id);
        return new Response<>(ResultEnum.OK);
    }

    @GetMapping("/detail")
    public Response<Book> getBook(@RequestParam("id") String id) {
        return new Response<>(ResultEnum.OK, bookService.getBook(id));
    }

    @PostMapping("/list")
    public Response<Page<Book>> listBook(@RequestBody BookQuery query) {
        return new Response<>(ResultEnum.OK, bookService.listBook(query));
    }

}
