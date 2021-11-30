package com.fyp.vasclinicserver.service;

import com.fyp.vasclinicserver.exceptions.VasException;
import com.fyp.vasclinicserver.mapper.UserMapper;
import com.fyp.vasclinicserver.model.User;
import com.fyp.vasclinicserver.payload.ProfileResponse;
import com.fyp.vasclinicserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ProfileResponse getProfile(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()){
            return userMapper.userToProfileResponse(user.get());
        }else {
            throw new VasException("User's profile cannot be found");
        }
    }


}
