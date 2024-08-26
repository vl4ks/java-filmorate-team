package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAllUsers() {

        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Создаем нового пользователя");
        if (user.getEmail().isBlank()) {
            log.error("Имейл должен быть указан");
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            log.error("Этот имейл уже используется - {}", user.getEmail());
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        user.setId(getNextUserId());
        log.trace("Присвоили пользователю id: {}", user.getId());
        user.setLogin(user.getLogin());
        log.trace("Присвоили пользователю login: {}", user.getLogin());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        } else {
            user.setName(user.getName());
        }
        log.info("Присвоили пользователю name: {}", user.getName());
        user.setBirthday(user.getBirthday());
        log.trace("Присвоили пользователю birthday: {}", user.getBirthday());
        users.put(user.getId(), user);
        log.trace("Пользователь создан: {}", user);
        log.info("Пользователь c id {}  и email {} - создан", user.getId(), user.getEmail());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя");
        if (user.getId() == 0) {
            log.error("Id при изменении пользователя должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());
            log.trace("Взяли в обновление пользователя {}", oldUser);
            if (users.values().stream().filter(u -> u.getId() != oldUser.getId()).anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
                log.error("Этот имейл уже используется - {}", user.getEmail());
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
            oldUser.setEmail(user.getEmail());
            log.trace("Обновили email пользователя {}", oldUser);
            if (!user.getName().isBlank()) {
                oldUser.setName(user.getName());
                log.trace("Обновили username пользователя {}", oldUser);
            } else {
                oldUser.setName(user.getLogin());
                log.trace("Обновили username пользователя {}", oldUser);
            }
            if (user.getLogin() != null) {
                log.trace("Обновили password пользователя {}", oldUser);
                oldUser.setLogin(user.getLogin());
            }
            oldUser.setBirthday(user.getBirthday());
            log.trace("Обновили день рождения пользователя {}", oldUser);
            log.info("Обновленный пользователь c id {}  и email {}", oldUser.getId(), oldUser.getEmail());
            return oldUser;
        }
        throw new NotFoundException("Пользователь не найден");
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextUserId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
