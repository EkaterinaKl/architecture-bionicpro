package reportservice.service;

import org.springframework.stereotype.Component;
import reportservice.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class RandomDataGenerator {

    public List<User> generateUsers(int numberOfUsers) {
        List<User> users = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfUsers; i++) {
            User user = new User();
            user.setId((long)i);
            user.setName("User-" + i);
            user.setAge(random.nextInt(90));
            user.setRole(getRandomRole());
            users.add(user);
        }
        return users;
    }

    private String getRandomRole() {
        String[] roles = {"prothetic_user", "regular_user"};
        return roles[new Random().nextInt(roles.length)];
    }
}