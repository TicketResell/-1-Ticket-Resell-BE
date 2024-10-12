package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.converter.UserConverter;
import com.teamseven.ticketresell.service.IAccountService;

import com.teamseven.ticketresell.dto.UserDTO;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IAccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter accountConverter;

    @Override
    public UserDTO login(String identifer, String password) {
        UserEntity user = userRepository.findByEmailOrUsername(identifer, password);
        if (user != null && password.equals(user.getPassword())) {
            return accountConverter.toDTO(user);
        }
        return null;
    }
    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserEntity createNewAccount(String username, String password) {
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(password);
        return userRepository.save(newUser);
    }
}
