package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private InMemoryUserStorage userStorage;
    private UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable Long id) {
        return userStorage.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findUserFriends(@PathVariable Long id) {
        HashSet<Long> userFriends = (HashSet<Long>) userStorage.getUserById(id).getFriends();
        if (userFriends == null) {
            userFriends = new HashSet<>();
            userStorage.getUserById(id).setFriends(userFriends);
        }
        return userFriends.stream()
                .map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findUserFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getMutualFriends(userStorage.getUserById(id), userStorage.getUserById(otherId));
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userStorage.addUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.addFriend(userStorage.getUserById(id), userStorage.getUserById(friendId));
    }

    @DeleteMapping
    public String delete(@Valid @RequestBody User user) {
        return userStorage.removeUser(user);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.removeFriend(userStorage.getUserById(id), userStorage.getUserById(friendId));
    }

}
