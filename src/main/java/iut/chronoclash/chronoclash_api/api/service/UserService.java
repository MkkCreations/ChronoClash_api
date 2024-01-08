package iut.chronoclash.chronoclash_api.api.service;

import iut.chronoclash.chronoclash_api.api.model.Operation;
import iut.chronoclash.chronoclash_api.api.model.User;
import iut.chronoclash.chronoclash_api.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    LogService logService;

    @Autowired
    public UserService(UserRepository userRepository, LogService logService) {
        this.userRepository = userRepository;
        this.logService = logService;
    }

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

        Operation operation = new Operation();
        operation.setType("update");
        operation.setDate(new Date().toString());
        operation.setDescription(String.format("User %s updated info", user.getUsername()));

        logService.createLog("User", operation, user);
        return userRepository.save(user);
    }

    public User create(User user) {
        return userRepository.save(user);
    }

}