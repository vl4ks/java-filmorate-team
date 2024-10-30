package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendRequests.FriendRequestsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service("userService")
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final EventService eventService;
    private final FriendRequestsStorage friendRequestsStorage;

    public User createUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        User userToReturn = userStorage.createUser(user);
        return userToReturn;
    }

    public void createFriend(Long userId, Long friendId) {
        friendRequestsStorage.addFriend(userId, getUserById(friendId).getId());
        eventService.createEvent(userId, EventType.FRIEND, EventOperation.ADD, friendId);
    }

    public Collection<User> getUserFriends(Long userId) {
        return userStorage.getUserFriends(userId);
    }

    public void removeFriend(User userFrom, User userTo) {
        friendRequestsStorage.deleteFriend(userFrom.getId(), userTo.getId());
        eventService.createEvent(userFrom.getId(), EventType.FRIEND, EventOperation.REMOVE, userTo.getId());
    }

    public Collection<User> getMutualFriends(User userFrom, User userTo) {
        return userStorage.getMutualFriends(userFrom, userTo);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public String removeUser(User user) {
        return userStorage.removeUser(user);
    }

}