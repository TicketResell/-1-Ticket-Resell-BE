package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
//    UserEntity findByUsernameAndPassword(String username, String password);
    UserEntity findByEmail(String email);
//    UserEntity findByEmailAddressAndPassword(String emailAddress, String password);
    UserEntity findById(long userID);

}
