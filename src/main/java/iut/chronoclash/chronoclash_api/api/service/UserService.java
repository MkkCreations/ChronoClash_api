package iut.chronoclash.chronoclash_api.api.service;

import iut.chronoclash.chronoclash_api.api.model.Game;
import iut.chronoclash.chronoclash_api.api.model.Level;
import iut.chronoclash.chronoclash_api.api.model.Operation;
import iut.chronoclash.chronoclash_api.api.model.User;
import iut.chronoclash.chronoclash_api.api.repository.GameRepository;
import iut.chronoclash.chronoclash_api.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    LogService logService;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user id not found"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    public User update(User user, User newUser) {
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setImage(newUser.getImage());
        user.setLevel(newUser.getLevel());
        user.setGames(newUser.getGames());

        Operation operation = new Operation();
        operation.setType("update");
        operation.setDate(new Date().toString());
        operation.setDescription(String.format("User %s updated info", user.getUsername()));

        logService.createLog("User", operation, user);
        return userRepository.save(user);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User create(User user) {
        Level level = new Level();
        level.setOwner(user);
        level.setXp(0);
        level.setLevel(1);
        user.setLevel(level);
        return userRepository.save(user);
    }

    public User updateLevel(User user, int xp) {
        Level level = user.getLevel();
        level.setXp(level.getXp() + xp);
        if (level.getXp() >= 100) {
            level.setXp(level.getXp() - 100);
            level.setLevel(level.getLevel() + 1);
        }
        user.setLevel(level);
        return user;
    }
}