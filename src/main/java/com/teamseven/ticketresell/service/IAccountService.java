package com.teamseven.ticketresell.service;


import com.teamseven.ticketresell.dto.UserDTO;
import com.teamseven.ticketresell.entity.UserEntity;

public interface IAccountService {
    UserDTO login(String username, String password);
    public UserEntity findByUsername(String username) ;

    public UserEntity createNewAccount(String username, String password);

}
