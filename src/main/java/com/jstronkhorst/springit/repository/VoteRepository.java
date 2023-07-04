package com.jstronkhorst.springit.repository;

import com.jstronkhorst.springit.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
