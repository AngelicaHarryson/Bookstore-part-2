package com.example.BookStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerController {

    @GetMapping("/customers")
    public String customers(Model model) {
        List<Customer> customers = new ArrayList<>();
        model.addAttribute("customers", customers);

        return "customers";
    }

    @GetMapping("/addCustomer")
    public String add(Model model) {
        model.addAttribute("customer", new Customer());
        return "customerForm";
    }

    @PostMapping("/saveCustomer")
    public String set(@ModelAttribute Customer customer) {
        return "redirect:/customers";
    }
}
