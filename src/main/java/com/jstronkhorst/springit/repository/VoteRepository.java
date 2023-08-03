package com.jstronkhorst.springit.repository;

import com.jstronkhorst.springit.domain.Link;
import com.jstronkhorst.springit.domain.User;
import com.jstronkhorst.springit.domain.Vote;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.function.LongUnaryOperator;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserAndLink(@NonNull User user, @NonNull Link link);
}
