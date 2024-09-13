package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        log.info("Создаем нового пользователя");
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            log.error("Этот имейл уже используется - {}", user.getEmail());
            throw new DuplicatedDataException(String.format("Этот имейл уже используется %s", user.getEmail()));
        }
        user.setId(getNextUserId());
        log.trace("Присвоили пользователю id: {}", user.getId());
        log.trace("Присвоили пользователю login: {}", user.getLogin());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        } else {
            user.setName(user.getName());
        }
        log.trace("Присвоили пользователю name: {}", user.getName());
        log.trace("Присвоили пользователю birthday: {}", user.getBirthday());
        users.put(user.getId(), user);
        log.trace("Пользователь создан: {}", user);
        log.info("Пользователь c id {}  и email {} - создан", user.getId(), user.getEmail());
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление пользователя");
        if (user.getId() == 0) {
            log.error("Id при изменении пользователя должен быть указан");
            throw new ConditionsNotMetException("Id при изменении пользователя должен быть указан");
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
            oldUser.setFriends(user.getFriends());
            log.trace("Обновили список друзей пользователя {}", oldUser);
            log.info("Обновленный пользователь c id {}  и email {}", oldUser.getId(), oldUser.getEmail());
            return oldUser;
        } else {
            log.error("Пользователь c id {} не найден.", user.getId());
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public String removeUser(User user) {
        if (users.containsKey(user.getId())) {
            log.trace("удаление фильма с id: {}", user.getId());
            users.remove(user.getId());
            return "Фильм с id " + user.getId() + " был удален";
        }
        throw new NotFoundException("Фильм с id = " + user.getId() + " не найден");
    }

    @Override
    public Collection<User> findAllUsers() {
        log.info("Получен запрос на получение всех пользователей");
        log.trace("Пользователи: {}", users.values());
        return users.values();
    }

    private long getNextUserId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public User getUserById(long id) {
        log.info("Получен запрос на получение пользоваетля с id: {}", id);
        if (users.containsKey(id)) {
            log.trace("Пользоваетль с id: {} найден", id);
            return users.get(id);
        }
        throw new NotFoundException("Пользователь с id = " + id + " не найден");
    }
}
