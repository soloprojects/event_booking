package com.event.booking.service;

import com.event.booking.dto.AuthenticationResponse;
import com.event.booking.entity.User;
import com.event.booking.exception.BusinessException;
import com.event.booking.iservice.IUserService;
import com.event.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByEmail(String email) {

        return userRepository.findByEmail(email).orElseThrow(() -> new BusinessException("Email not found", HttpStatus.NOT_FOUND));
    }

}
