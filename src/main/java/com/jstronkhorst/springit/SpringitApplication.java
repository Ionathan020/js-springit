package com.jstronkhorst.springit;

import com.jstronkhorst.springit.domain.Comment;
import com.jstronkhorst.springit.domain.Link;
import com.jstronkhorst.springit.repository.CommentRepository;
import com.jstronkhorst.springit.repository.LinkRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringitApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(LinkRepository linkRepository, CommentRepository commentRepository) {
        return args -> {
            Link link = new Link("This is the first posted link automatically created by the system.", "https://www.google.nl");
            linkRepository.save(link);
            Comment comment = new Comment("Let's spam the shit out of this site!", link);
            commentRepository.save(comment);
            link.addComment(comment);
        };
    }
}
