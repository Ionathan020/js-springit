package com.jstronkhorst.springit.bootstrap;

import com.jstronkhorst.springit.comment.Comment;
import com.jstronkhorst.springit.link.Link;
import com.jstronkhorst.springit.user.Role;
import com.jstronkhorst.springit.user.User;
import com.jstronkhorst.springit.comment.CommentRepository;
import com.jstronkhorst.springit.user.RoleRepository;
import com.jstronkhorst.springit.link.LinkService;
import com.jstronkhorst.springit.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserService userService;
    private final LinkService linkService;
    private final CommentRepository commentRepository;

    public DatabaseLoader(RoleRepository roleRepository, UserService userService,
                          LinkService linkService, CommentRepository commentRepository) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.linkService = linkService;
        this.commentRepository = commentRepository;
    }

    @Override
    public void run(String... args) {
        //User and roles
        User master = addUsersAndRolesAndReturnMaster();

        Map<String, String> links = new HashMap<>();
        links.put("Securing Spring Boot APIs and SPAs with OAuth 2.0", "https://auth0.com/blog/securing-spring-boot-apis-and-spas-with-oauth2/?utm_source=reddit&utm_medium=sc&utm_campaign=springboot_spa_securing");
        links.put("Easy way to detect Device in Java Web Application using Spring Mobile - Source code to download from GitHub", "https://www.opencodez.com/java/device-detection-using-spring-mobile.htm");
        links.put("Tutorial series about building microservices with SpringBoot (with Netflix OSS)", "https://medium.com/@marcus.eisele/implementing-a-microservice-architecture-with-spring-boot-intro-cdb6ad16806c");
        links.put("Detailed steps to send encrypted email using Java / Spring Boot - Source code to download from GitHub", "https://www.opencodez.com/java/send-encrypted-email-using-java.htm");
        links.put("Build a Secure Progressive Web App With Spring Boot and React", "https://dzone.com/articles/build-a-secure-progressive-web-app-with-spring-boo");
        links.put("Building Your First Spring Boot Web Application - DZone Java", "https://dzone.com/articles/building-your-first-spring-boot-web-application-ex");
        links.put("Building Microservices with Spring Boot Fat (Uber) Jar", "https://jelastic.com/blog/building-microservices-with-spring-boot-fat-uber-jar/");
        links.put("Spring Cloud GCP 1.0 Released", "https://cloud.google.com/blog/products/gcp/calling-java-developers-spring-cloud-gcp-1-0-is-now-generally-available");
        links.put("Simplest way to Upload and Download Files in Java with Spring Boot - Code to download from Github", "https://www.opencodez.com/uncategorized/file-upload-and-download-in-java-spring-boot.htm");
        links.put("Add Social Login to Your Spring Boot 2.0 app", "https://developer.okta.com/blog/2018/07/24/social-spring-boot");
        links.put("File download example using Spring REST Controller", "https://www.jeejava.com/file-download-example-using-spring-rest-controller/");

        links.forEach((k, v) -> {
            Link link = new Link(k, v, master);
            linkService.save(link);
            master.addLink(link);
            Comment spring = new Comment("Thank you for this link related to Spring Boot. I love it, great post!", link, master);
            Comment security = new Comment("I love that you're talking about Spring Security",link, master);
            Comment pwa = new Comment("What is this Progressive Web App thing all about? PWAs sound really cool.",link, master);
            Comment[] comments = {spring,security,pwa};
            for(Comment comment : comments) {
                commentRepository.save(comment);
                link.addComment(comment);
            }
            master.addComments(List.of(comments));
        });
        userService.save(master);
    }

    private User addUsersAndRolesAndReturnMaster() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String secret = "{bcrypt}" + encoder.encode("password");

        Role userRole = new Role("ROLE_USER");
        roleRepository.save(userRole);
        Role adminRole = new Role("ROLE_ADMIN");
        roleRepository.save(adminRole);

        User user = new User("user@gmail.com",secret,true);
        user.addRole(userRole);
        userService.save(user);

        User admin = new User("admin@gmail.com",secret,true);
        admin.addRole(adminRole);
        userService.save(admin);

        User master = new User("master@gmail.com",secret,true);
        master.addRoles(new HashSet<>(Arrays.asList(userRole,adminRole)));
        userService.save(master);
        return master;
    }
}