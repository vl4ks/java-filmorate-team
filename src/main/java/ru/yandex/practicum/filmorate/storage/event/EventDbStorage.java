package ru.yandex.practicum.filmorate.storage.event;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createEvent(Event event) {
        final String sql =
                "insert into events (user_id, timestamp, event_type, operation, entity_id) values (?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                event.getUserId(),
                event.getTimestamp(),
                event.getEventType().name(),
                event.getOperation().name(),
                event.getEntityId()
        );
    }

    @Override
    public Collection<Event> getEvents(Long userId) {
        final String sql = "select * from events where user_id = ?";
        return jdbcTemplate.query(sql, new EventMapper(), userId);
    }
}
