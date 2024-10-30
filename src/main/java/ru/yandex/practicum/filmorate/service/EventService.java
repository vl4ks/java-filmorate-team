package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventStorage eventStorage;
    private final UserStorage userStorage;

    public Collection<Event> getEvents(Long userId) {
        Optional.ofNullable(userStorage.getUserById(userId))
                .orElseThrow(() -> new NotFoundUserException("Пользователь с id=" + userId + " не найден"));
        return eventStorage.getEvents(userId);

    }

    public void createEvent(Long userId, EventType eventType, EventOperation eventOperation, Long entityId) {
        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(userId)
                .eventType(eventType)
                .operation(eventOperation)
                .entityId(entityId)
                .build();
        try {
            eventStorage.createEvent(event);
        } catch (DataAccessException e) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }
}
