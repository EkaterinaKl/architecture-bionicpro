package org.reports.controller;

import org.reports.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.reports.service.RandomDataGenerator;

import java.util.List;

@RestController
public class ReportController {

    private final RandomDataGenerator generator;

    public ReportController(RandomDataGenerator generator) {
        this.generator = generator;
    }
    @GetMapping("/reports")
    public List<User> getReport() {
        return generator.generateUsers(5);
    }
}