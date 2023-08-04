package com.jstronkhorst.springit.service;

import com.jstronkhorst.springit.domain.Link;
import com.jstronkhorst.springit.domain.User;
import com.jstronkhorst.springit.domain.Vote;
import com.jstronkhorst.springit.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Optional<Vote> findByUserAndLink(User user, Link link) {
        return voteRepository.findByUserAndLink(user, link);
    }

    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    public void delete (Vote vote) {
        voteRepository.delete(vote);
    }
}
