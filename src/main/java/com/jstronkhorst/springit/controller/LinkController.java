package com.jstronkhorst.springit.controller;

import com.jstronkhorst.springit.domain.Comment;
import com.jstronkhorst.springit.domain.Link;
import com.jstronkhorst.springit.domain.User;
import com.jstronkhorst.springit.repository.CommentRepository;
import com.jstronkhorst.springit.repository.LinkRepository;
import com.jstronkhorst.springit.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class LinkController {

    private static final Logger logger = LoggerFactory.getLogger(LinkController.class);

    private final LinkRepository linkRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public LinkController(LinkRepository linkRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("links", linkRepository.findAll());
        return "link/list";
    }

    @GetMapping("/link/{id}")
    public String read(@PathVariable Long id,Model model) {
        Optional<Link> link = linkRepository.findById(id);
        if( link.isPresent() ) {
            Link currentLink = link.get();
            Comment comment = new Comment();
            comment.setLink(currentLink);
            model.addAttribute("comment",comment);
            model.addAttribute("link",currentLink);
            model.addAttribute("success", model.containsAttribute("success"));
            return "link/view";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/link/submit")
    public String newLinkForm(Model model) {
        model.addAttribute("link",new Link());
        return "link/submit";
    }

    @PostMapping("/link/submit")
    public String createLink(@Valid Link link, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.info("Validation errors where found while submitting a new link.");
            model.addAttribute("link", link);
            return "link/submit";
        } else {
            linkRepository.save(link);
            logger.info("Link {} is saved.", link.getTitle());
            redirectAttributes
                    .addAttribute("id", link.getId())
                    .addFlashAttribute("success", true);
            return "redirect:/link/{id}";
        }
    }

    @Secured({"ROLE_USER"})
    @PostMapping("/link/comments")
    public String addComment(@Valid Comment comment, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> optionalUser = userRepository.findByEmail(auth.getName());
        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }
        comment.setUser(optionalUser.get());
        if(bindingResult.hasErrors() ) {
            logger.error("Something went wrong.");
        } else {
            logger.info("New Comment Saved!");
            commentRepository.save(comment);
        }
        return "redirect:/link/" + comment.getLink().getId();
    }
}
