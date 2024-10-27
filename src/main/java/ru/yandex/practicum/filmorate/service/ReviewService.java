package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.reviewLike.ReviewLikeStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service("reviewService")
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final ReviewLikeStorage reviewLikeStorage;

    public Collection<Review> getAll(int filmId, int count) {
        return reviewStorage.getAll(filmId, count);
    }

    public Review create(Review review) {
        return reviewStorage.create(review);
    }

    public String remove(Long id) {
        return reviewStorage.remove(id);
    }

    public List<Review> getAllLikes() {
        return reviewLikeStorage.getAll();
    }

    public Review update(Review review) {
        return reviewStorage.update(review);
    }
}