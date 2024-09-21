package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.service.IAccountService;

import com.teamseven.ticketresell.dto.AccountDTO;
import com.teamseven.ticketresell.converter.AccountConverter;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountConverter accountConverter;

    @Override
    public AccountDTO login(String emailAddress, String password) {
        UserEntity user = userRepository.findByEmailAddress(emailAddress);
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
