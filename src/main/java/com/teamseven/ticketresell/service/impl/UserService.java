package com.teamseven.ticketresell.service.impl;

import com.teamseven.ticketresell.converter.UserConverter;
import com.teamseven.ticketresell.service.IAccountService;
import java.util.regex.Pattern;
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
        UserEntity user;

        // Biểu thức chính quy để kiểm tra định dạng email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        // Kiểm tra xem identifer có phải là email hay username
        if (pattern.matcher(identifer).matches()) {
            // Nếu định dạng đúng, coi đó là email
            user = userRepository.findByEmail(identifer);
        } else {
            // Ngược lại, coi đó là username
            user = userRepository.findByUsername(identifer);
        }

        // Kiểm tra mật khẩu
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
    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Cập nhật hoặc lưu người dùng
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    // Xóa người dùng theo ID
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public String getUserRoleByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username);
        return user != null ? user.getRole() : null;
    }
}
