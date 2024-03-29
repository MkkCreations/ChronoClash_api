package iut.chronoclash.chronoclash_api.model;

import iut.chronoclash.chronoclash_api.api.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = User.class)
class UserTests {

    @Test
    void testUserConstructor() {
        byte[] img = new byte[]{};
        User user = new User("name", "username", "email", "password", img, "role", null, null, null, null);
        assertEquals("name", user.getName());
        assertEquals("username", user.getUsername());
        assertEquals("email", user.getEmail());
        assertEquals("password", user.getPassword());
        assertSame(img, user.getImage());
        assertEquals("role", user.getRole());
        assertNull(user.getLogs());
    }

    @Test
    void testUserSetters() {
        User user = new User();

        user.setName("name");
        user.setUsername("username");
        user.setEmail("email");
        user.setPassword("password");
        user.setImage(null);
        user.setRole("role");
        user.setLogs(null);

        assertEquals("name", user.getName());
        assertEquals("username", user.getUsername());
        assertEquals("email", user.getEmail());
        assertEquals("password", user.getPassword());
        assertSame(null, user.getImage());
        assertEquals("role", user.getRole());
        assertNull(user.getLogs());
    }

    @Test
    void testUserAuthorities() {
        User user = new User("name", "username", "email", "password", new byte[]{}, null, null, null, null, null);

        assertEquals(1, user.getAuthorities().size());
        assertTrue(user.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));
    }

}