package com.jstronkhorst.springit.link;

import com.jstronkhorst.springit.comment.Comment;
import com.jstronkhorst.springit.user.User;
import com.jstronkhorst.springit.comment.CommentService;
import com.jstronkhorst.springit.user.UserService;
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

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
public class LinkController {

    private static final Logger logger = LoggerFactory.getLogger(LinkController.class);

    private final LinkService linkService;
    private final CommentService commentService;
    private final UserService userService;

    public LinkController(LinkService linkService, CommentService commentService, UserService userService) {
        this.linkService = linkService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("links", linkService.findAll());
        return "link/list";
    }

    @GetMapping("/link/{id}")
    public String read(@PathVariable Long id,Model model) {
        Optional<Link> link = linkService.findById(id);
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
            linkService.save(link);
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
        Optional<User> optionalUser = userService.findByEmail(auth.getName());
        if (optionalUser.isEmpty()) {
            return "redirect:/login";
        }
        comment.setUser(optionalUser.get());
        if(bindingResult.hasErrors() ) {
            logger.error("Something went wrong.");
        } else {
            logger.info("New Comment Saved!");
            commentService.save(comment);
        }
        return "redirect:/link/" + comment.getLink().getId();
    }
}
