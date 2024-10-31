package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FilmService filmService;
    private final EventService eventService;


    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable Long id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findUserFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getMutualFriends(userService.getUserById(id), userService.getUserById(otherId));
    }

    @GetMapping("{id}/recommendations")
    public Collection<Film> getRecommendations(@PathVariable Long id) {
        return filmService.getRecomendations(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.createFriend(userService.getUserById(id).getId(), userService.getUserById(friendId).getId());
    }

    @DeleteMapping
    public String delete(@Valid @RequestBody User user) {
        return userService.removeUser(user);
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable Long id) {
        return userService.removeUser(userService.getUserById(id));

    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriend(userService.getUserById(id), userService.getUserById(friendId));
    }

    @GetMapping("/{id}/feed")
    public Collection<Event> getEvents(@PathVariable Long id) {
        return eventService.getEvents(id);
    }
}

