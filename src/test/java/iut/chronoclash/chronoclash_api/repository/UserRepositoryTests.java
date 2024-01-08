package iut.chronoclash.chronoclash_api.repository;

import iut.chronoclash.chronoclash_api.api.model.User;
import iut.chronoclash.chronoclash_api.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("name", "username", "email", "password", "image", "role", null);
        userRepository.save(user);
    }

    @Test
    void testUserRepositorySave() {
        assertEquals(1, userRepository.count());
    }

    @Test
    void testUserRepositoryFindById() {
        assertEquals(user, userRepository.findById(user.getId()).get());
    }

    @Test
    void testUserRepositoryFindByUsername() {
        assertEquals(user, userRepository.findByUsername(user.getUsername()).get());
    }

    @Test
    void testUserRepositoryDelete() {
        userRepository.delete(user);
        assertEquals(0, userRepository.count());
    }

    @Test
    void testUserRepositoryDeleteById() {
        userRepository.deleteById(user.getId());
        assertEquals(0, userRepository.count());
    }

}