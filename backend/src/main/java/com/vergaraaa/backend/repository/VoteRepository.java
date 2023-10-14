package com.vergaraaa.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vergaraaa.backend.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

}
