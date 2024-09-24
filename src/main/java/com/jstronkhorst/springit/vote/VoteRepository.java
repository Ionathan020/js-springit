package com.jstronkhorst.springit.vote;

import com.jstronkhorst.springit.link.Link;
import com.jstronkhorst.springit.user.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserAndLink(@NonNull User user, @NonNull Link link);
}
