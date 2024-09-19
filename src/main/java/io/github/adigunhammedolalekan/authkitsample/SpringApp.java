package io.github.adigunhammedolalekan.authkitsample;

import io.github.adigunhammedolalekan.authkit.service.AuthManager;
import io.github.adigunhammedolalekan.authkit.types.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.UUID;

@SpringBootApplication(scanBasePackages = {"io.github.adigunhammedolalekan.authkitsample.*"})
public class SpringApp implements CommandLineRunner {

    @Autowired
    private AuthManager authManager;

    private final Logger LOGGER = LoggerFactory.getLogger(SpringApp.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var email = "adigun" + UUID.randomUUID().toString().substring(1, 6) + "@gmail.com";
        var password = "$adigunHAMMED001";
        var user = authManager.signUp(email, password);
        LOGGER.info(user.toString());

        var token = authManager.login(email, password);
        LOGGER.info(token.toString());

        var users = authManager.getUsers();
        LOGGER.info(users.toString());
        LOGGER.info("User count: {}", users.size());

        // deleteUsers(users);
    }

    private void deleteUsers(List<User> users) {
        users.forEach(user -> {
            authManager.deleteUser(user.id());
            LOGGER.info("Deleted user: {}", user.id());
        });
    }
}
