package com.teamseven.ticketresell.repository;

import com.teamseven.ticketresell.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
//    UserEntity findByUsernameAndPassword(String username, String password);
    UserEntity findByEmail(String email);
//    UserEntity findByEmailAddressAndPassword(String emailAddress, String password);
    UserEntity findById(long userID);
    // Tìm người dùng bằng email hoặc username
    UserEntity findByEmailOrUsername(String email, String username);
    UserEntity findByPhone(String phone);
    List<UserEntity> findByIsAgencyFalse();

    List<UserEntity> findByRole(String role);

}
