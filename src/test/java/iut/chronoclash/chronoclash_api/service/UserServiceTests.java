package iut.chronoclash.chronoclash_api.service;

import iut.chronoclash.chronoclash_api.api.model.User;
import iut.chronoclash.chronoclash_api.api.repository.UserRepository;
import iut.chronoclash.chronoclash_api.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserServiceTests {
    @Autowired
    private UserRepository userRepository;
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, null);
        user = new User("name", "username", "email", "password", "image", "role", null);
        userService.create(user);
    }

    @Test
    void testUserServiceCreate() {
        User user1 = new User("name1", "username1", "email1", "password1", "image1", "role1", null);
        assertEquals("name1", userService.create(user1).getName());
    }

    @Test
    void testUserServiceLoadUserByUsername() {
        assertEquals(user, userService.loadUserByUsername(user.getUsername()));
    }

}
