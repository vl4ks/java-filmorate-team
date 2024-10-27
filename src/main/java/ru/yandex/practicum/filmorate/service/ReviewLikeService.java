package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.reviewLike.ReviewLikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Slf4j
@Service("reviewLikeService")
@RequiredArgsConstructor
public class ReviewLikeService {

    private final UserStorage userStorage;
    private final ReviewStorage reviewStorage;
    private final ReviewLikeStorage reviewLikeStorage;

    public void addLike(int reviewId, int userId) {
        reviewLikeStorage.addLike(reviewId, userId);
    }

    public void addDislike(int reviewId, int userId) {
        reviewLikeStorage.addDislike(reviewId, userId);
    }

    public void removeLike(int reviewId, int userId) {
        reviewLikeStorage.removeLike(reviewId, userId);
    }

    public void removeDislike(int reviewId, int userId) {
        reviewLikeStorage.removeDislike(reviewId, userId);
    }
}