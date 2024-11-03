package ru.yandex.practicum.filmorate.storage.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataException;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.PreparedStatement;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createEvent(Event event) {
        final String sql =
                "insert into events (user_id, timestamp, event_type, operation, entity_id) values (?, ?, ?, ?, ?)";
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"event_id"});

                preparedStatement.setLong(1, event.getUserId());
                preparedStatement.setLong(2, event.getTimestamp());
                preparedStatement.setString(3, event.getEventType().name());
                preparedStatement.setString(4, event.getOperation().name());
                preparedStatement.setLong(5, event.getEntityId());
                return preparedStatement;
            }, generatedKeyHolder);
        } catch (DataAccessException e) {
            throw new DataException(String.format("Что-то пошло не так при создании евента: %s", e.getMessage()));
        }

        long eventId = generatedKeyHolder.getKey().longValue();
        event.setEventId(eventId);
        log.info(String.format("Создан новый евент: %s", event));
    }

    @Override
    public Collection<Event> getEvents(Long userId) {
        final String sql = "select * from events where user_id = ?";
        return jdbcTemplate.query(sql, new EventMapper(), userId);
    }
}
