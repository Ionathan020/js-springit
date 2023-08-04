package com.jstronkhorst.springit.controller;

import com.jstronkhorst.springit.domain.Link;
import com.jstronkhorst.springit.domain.User;
import com.jstronkhorst.springit.domain.Vote;
import com.jstronkhorst.springit.repository.LinkRepository;
import com.jstronkhorst.springit.repository.UserRepository;
import com.jstronkhorst.springit.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class VoteController {
    Logger log = LoggerFactory.getLogger(VoteController.class);

    private final VoteRepository voteRepository;
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;


    public VoteController(VoteRepository voteRepository, LinkRepository linkRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/vote/link/{linkID}/direction/{direction}/votecount/{voteCount}")
    public int vote(@PathVariable Long linkID, @PathVariable Short direction, @PathVariable int voteCount) {
        log.info("Start vote count: {} and direction{}", voteCount, direction);
        Optional<Link> optionalLink = linkRepository.findById(linkID);
        if (optionalLink.isEmpty()) {
            return voteCount;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> optionalUser = userRepository.findByEmail(auth.getName());
        if (optionalUser.isEmpty()) {
            return voteCount;
        }
        if (direction == null) {
            return voteCount;
        }
        Link link = optionalLink.get();
        User user = optionalUser.get();
        Optional<Vote> optionalVote = voteRepository.findByUserAndLink(user,link);
        if (optionalVote.isPresent()) {
            Vote existingVote = optionalVote.get();
            log.info("found existing vote with id: {} and direction{}", voteCount, existingVote.getDirection());
            if (optionalVote.get().getDirection().equals(direction)) {
                return voteCount;
            }
            int updatedVoteCount = voteCount - existingVote.getDirection();
            log.info("updated vote count: {}", updatedVoteCount);
            link.setVoteCount(updatedVoteCount);
            linkRepository.save(link);
            voteRepository.delete(optionalVote.get());
            voteCount = updatedVoteCount;
        }
        Vote vote = new Vote(direction, user, link);
        voteRepository.save(vote);
        int updatedVoteCount = voteCount + direction;
        log.info("updated vote count: {}", updatedVoteCount);
        link.setVoteCount(updatedVoteCount);
        linkRepository.save(link);
        return updatedVoteCount;
    }
}
