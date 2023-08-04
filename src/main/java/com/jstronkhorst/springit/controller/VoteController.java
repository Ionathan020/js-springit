package com.jstronkhorst.springit.controller;

import com.jstronkhorst.springit.domain.Link;
import com.jstronkhorst.springit.domain.User;
import com.jstronkhorst.springit.domain.Vote;
import com.jstronkhorst.springit.service.LinkService;
import com.jstronkhorst.springit.service.UserService;
import com.jstronkhorst.springit.service.VoteService;
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

    private final VoteService voteService;
    private final LinkService linkService;
    private final UserService userService;


    public VoteController(VoteService voteService, LinkService linkService, UserService userService) {
        this.voteService = voteService;
        this.linkService = linkService;
        this.userService = userService;
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/vote/link/{linkID}/direction/{direction}/votecount/{voteCount}")
    public int vote(@PathVariable Long linkID, @PathVariable Short direction, @PathVariable int voteCount) {
        log.info("Start vote count: {} and direction{}", voteCount, direction);
        Optional<Link> optionalLink = linkService.findById(linkID);
        if (optionalLink.isEmpty()) {
            return voteCount;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> optionalUser = userService.findByEmail(auth.getName());
        if (optionalUser.isEmpty()) {
            return voteCount;
        }
        if (direction == null) {
            return voteCount;
        }
        Link link = optionalLink.get();
        User user = optionalUser.get();
        Optional<Vote> optionalVote = voteService.findByUserAndLink(user,link);
        if (optionalVote.isPresent()) {
            Vote existingVote = optionalVote.get();
            log.info("found existing vote with id: {} and direction{}", voteCount, existingVote.getDirection());
            if (optionalVote.get().getDirection().equals(direction)) {
                return voteCount;
            }
            int updatedVoteCount = voteCount - existingVote.getDirection();
            log.info("updated vote count: {}", updatedVoteCount);
            link.setVoteCount(updatedVoteCount);
            linkService.save(link);
            voteService.delete(optionalVote.get());
            voteCount = updatedVoteCount;
        }
        Vote vote = new Vote(direction, user, link);
        voteService.save(vote);
        int updatedVoteCount = voteCount + direction;
        log.info("updated vote count: {}", updatedVoteCount);
        link.setVoteCount(updatedVoteCount);
        linkService.save(link);
        return updatedVoteCount;
    }
}
