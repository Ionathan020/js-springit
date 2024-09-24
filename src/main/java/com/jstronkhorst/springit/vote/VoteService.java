package com.jstronkhorst.springit.vote;

import com.jstronkhorst.springit.link.Link;
import com.jstronkhorst.springit.user.User;
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
