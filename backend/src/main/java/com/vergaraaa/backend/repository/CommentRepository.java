package com.vergaraaa.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vergaraaa.backend.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
