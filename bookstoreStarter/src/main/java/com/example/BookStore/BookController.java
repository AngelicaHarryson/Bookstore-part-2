package com.example.BookStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {

    private static final int PAGE_SIZE = 10;

    @GetMapping("/")
    public String books(Model model, @RequestParam(value="page", required=false, defaultValue="1") int page, RestTemplate restTemplate) {

        List<Book> books = getPage(page-1, PAGE_SIZE, restTemplate);
        int pageCount = numberOfPages(PAGE_SIZE, restTemplate);
        int[] pages = toArray(pageCount);

        model.addAttribute("books", books);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", page);
        model.addAttribute("showPrev", page > 1);
        model.addAttribute("showNext", page < pageCount);

        return "books";
    }

    @GetMapping("/book/{page}/{id}")
    public String book(Model model, @PathVariable Integer page, @PathVariable Long id, RestTemplate restTemplate) {
        Book book = restTemplate.getForObject("http://localhost:8080/book/" + id, Book.class);
        model.addAttribute("page", page);
        model.addAttribute("book", book);

        return "book";
    }


    private int[] toArray(int num) {
        int[] result = new int[num];
        for (int i = 0; i < num; i++) {
            result[i] = i+1;
        }
        return result;
    }

    private List<Book> getPage(int page, int pageSize, RestTemplate restTemplate) {
        List<Book> books = restTemplate.getForObject("http://localhost:8080/book", ArrayList.class);
        int from = Math.max(0,page*pageSize);
        int to = Math.min(books.size(),(page+1)*pageSize);

        return books.subList(from, to);
    }

    private int numberOfPages(int pageSize, RestTemplate restTemplate) {
        List<Book> books = restTemplate.getForObject("http://localhost:8080/book", ArrayList.class);
        return (int)Math.ceil(new Double(books.size()) / pageSize);
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("book", new Book());
        return "form";
    }

    @PostMapping("/save")
    public String set(@ModelAttribute Book book, RestTemplate restTemplate) {
        if (book.isNew()) {
            restTemplate.postForObject("http://localhost:8080/book/", book, Book.class);
        }
        else {
            restTemplate.put("http://localhost:8080/book/" + book.getId(), book, Book.class);
        }


        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id, RestTemplate restTemplate) {
        Book book = restTemplate.getForObject("http://localhost:8080/book/" + id, Book.class);
        model.addAttribute(book);
        return "form";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Long id, RestTemplate restTemplate) {
        restTemplate.delete("http://localhost:8080/book/" + id);
        return "redirect:/";
    }

}
