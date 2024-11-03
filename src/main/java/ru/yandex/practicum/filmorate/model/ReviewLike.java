package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@EqualsAndHashCode(exclude = "id")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReviewLike {

    private long id;

    private Integer reaction;

    private Integer reviewId;

    private Integer userId;
}
