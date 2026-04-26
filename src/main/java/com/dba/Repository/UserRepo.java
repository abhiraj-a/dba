package com.dba.Repository;


import com.dba.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User , UUID> {
    Optional<User> findByClerkId(String clerkId);

    Optional<User> findByEmail(String email);

    List<User> findAllByDeletedTrue();
}
