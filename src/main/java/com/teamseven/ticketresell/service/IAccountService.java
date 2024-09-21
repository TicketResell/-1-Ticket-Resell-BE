package com.teamseven.ticketresell.service;


import com.teamseven.ticketresell.dto.AccountDTO;
import com.teamseven.ticketresell.entity.UserEntity;

import java.util.List;

public interface IAccountService {
    AccountDTO login(String username, String password);
    public UserEntity findByUsername(String username) ;

    public UserEntity createNewAccount(String username, String password);

}
