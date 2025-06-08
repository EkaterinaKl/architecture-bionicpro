package reportservice.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private int age;
    private String role;
}