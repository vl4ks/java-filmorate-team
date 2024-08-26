package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validators.MinimumDate;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@EqualsAndHashCode
public class Film {

    private long id;

    @NotBlank
    private String name;

    @Length(max = 200)
    private String description;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @PastOrPresent(message = "Дата не может быть в будущем")
    @MinimumDate
    private LocalDate releaseDate;

    @Min(1)
    private long duration;

}
