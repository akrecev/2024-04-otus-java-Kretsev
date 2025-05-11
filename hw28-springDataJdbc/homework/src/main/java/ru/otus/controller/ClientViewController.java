package ru.otus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientViewController {
    @GetMapping("/clients")
    public String showClientsPage() {
        return "clients";
    }
}
