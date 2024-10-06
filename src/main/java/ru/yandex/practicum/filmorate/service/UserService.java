package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("userService")
public class UserService {

    private InMemoryUserStorage userStorage;


    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(User userFrom, User userTo) {
        if (userFrom.getFriends() == null) {
            userFrom.setFriends(new HashSet<>());
        }
        if (userTo.getFriends() == null) {
            userTo.setFriends(new HashSet<>());
        }
        if ((userFrom.getFriends().contains(userTo.getId())) | (userTo.getFriends().contains(userFrom.getId()))) {
            log.error("Пользователи в друзьях");
            throw new DuplicatedDataException("Пользователи уже в друзьях");
        }
        Set<Long> friendsFrom = userFrom.getFriends();
        Set<Long> friendsTo = userTo.getFriends();
        friendsFrom.add(userTo.getId());
        friendsTo.add(userFrom.getId());
        userFrom.setFriends(friendsFrom);
        userTo.setFriends(friendsTo);
        log.info("Пользователь {} добавлен в друзья к пользовалю {}", userFrom, userTo);

        return userFrom;
    }

    public User removeFriend(User userFrom, User userTo) {
        if (userFrom.getFriends() == null) {
            log.error("Пользователь не имеет друзей");
            userFrom.setFriends(new HashSet<>());
        }
        if (userTo.getFriends() == null) {
            log.error("Пользователь не имеет друзей");
            userTo.setFriends(new HashSet<>());
        }
        if ((userFrom.getFriends().contains(userTo.getId())) & (userTo.getFriends().contains(userFrom.getId()))) {
            userFrom.getFriends().remove(userTo.getId());
            userTo.getFriends().remove(userFrom.getId());
            log.info("Пользователь {} удален из друзей пользователя {}", userFrom, userTo);
            return userFrom;
        } else {
            log.error("Пользователи не в друзьях");
            throw new NotFoundUserException("Пользователи не в друзьях");
        }
    }

    public Collection<User> getMutualFriends(User userFrom, User userTo) {
        log.info("Общие друщья пользователя {} и {}", userFrom, userTo);
        HashSet<Long> userFriends = (HashSet<Long>) userFrom.getFriends();
        HashSet<Long> otherFriends = (HashSet<Long>) userTo.getFriends();
        HashSet<Long> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherFriends);
        log.trace("Общие друзья пользователя {}", commonFriends.retainAll(otherFriends));
        return commonFriends.stream()
                .map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toSet());
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public String removeUser(User user) {
        return userStorage.removeUser(user);
    }
}
