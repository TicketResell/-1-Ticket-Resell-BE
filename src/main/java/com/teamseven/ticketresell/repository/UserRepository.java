package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);
//    UserEntity findByUsernameAndPassword(String username, String password);
    UserEntity findByEmailAddress(String emailAddress);
//    UserEntity findByEmailAddressAndPassword(String emailAddress, String password);
}
