package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@EqualsAndHashCode(exclude = "reviewId")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    private long reviewId;

    private String content;

    private Boolean isPositive;

    private Integer userId;

    private Integer filmId;

    private Integer useful;
}
