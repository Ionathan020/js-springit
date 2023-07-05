package com.jstronkhorst.springit.repository;

import com.jstronkhorst.springit.domain.Comment;
import com.jstronkhorst.springit.domain.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
