package reportservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reportservice.model.Report;
import reportservice.model.User;
import reportservice.service.RandomDataGenerator;

import java.util.List;

@RestController
public class ReportController {

    @Autowired
    private RandomDataGenerator randomDataGenerator;

    @GetMapping("/reports")
    public Report getReports() {
        List<User> users = randomDataGenerator.generateUsers(10);
        return new Report(users);
    }
}