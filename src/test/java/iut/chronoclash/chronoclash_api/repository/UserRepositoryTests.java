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
        user = new User("name", "username", "email", "password", new byte[]{}, "role", null, null, null, null);
    }

    @Test
    void testUserRepositorySave() {
        userRepository.save(user);
        assertEquals(1, userRepository.count());
    }

    @Test
    void testUserRepositoryFindById() {
        userRepository.save(user);
        assertEquals(user, userRepository.findById(user.getId()).get());
    }

    @Test
    void testUserRepositoryFindByUsername() {
        userRepository.save(user);
        assertEquals(user, userRepository.findByUsername(user.getUsername()).get());
    }

    @Test
    void testUserRepositoryDelete() {
        userRepository.save(user);
        userRepository.delete(user);
        assertEquals(0, userRepository.count());
    }

    @Test
    void testUserRepositoryDeleteById() {
        userRepository.save(user);
        userRepository.deleteById(user.getId());
        assertEquals(0, userRepository.count());
    }

}