package com.dba.Repository;

import com.dba.Entity.TriedEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TriedEmailRepo extends JpaRepository<TriedEmail, UUID> {
    boolean existsByEmailHash(String hash);
}
