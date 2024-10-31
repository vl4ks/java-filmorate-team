package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.reviewLike.ReviewLikeStorage;

@Slf4j
@Service("reviewLikeService")
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeStorage reviewLikeStorage;

    public Review addLike(int reviewId, int userId) {
        log.info("Добавляем лайк отзыву {}, от пользователя {}", reviewId, userId);
        Review review = reviewLikeStorage.addLike(reviewId, userId);
        log.info("Лайк добавлен: {}", review);
        return review;
    }

    public Review addDislike(int reviewId, int userId) {
        log.info("Добавляем дизлайк отзыву {}, от пользователя {}", reviewId, userId);
        Review review = reviewLikeStorage.addDislike(reviewId, userId);
        log.info("Дизлайк добавлен: {}", review);
        return review;
    }

    public Review removeLike(int reviewId, int userId) {
        log.info("Удаляем лайк отзыву {}, от пользователя {}", reviewId, userId);
        Review review = reviewLikeStorage.removeLike(reviewId, userId);
        log.info("Лайк удален: {}", review);
        return review;
    }

    public Review removeDislike(int reviewId, int userId) {
        log.info("Удаляем дизлайк отзыву {}, от пользователя {}", reviewId, userId);
        Review review = reviewLikeStorage.removeDislike(reviewId, userId);
        log.info("Дизлайк удален: {}", review);
        return review;
    }
}