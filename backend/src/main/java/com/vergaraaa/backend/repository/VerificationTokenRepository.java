package com.vergaraaa.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vergaraaa.backend.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

}
