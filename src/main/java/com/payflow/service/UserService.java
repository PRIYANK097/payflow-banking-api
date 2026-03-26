package com.payflow.service;

import com.payflow.dto.request.RegisterUserRequest;
import com.payflow.dto.request.UpdateUserRequest;
import com.payflow.dto.response.UserResponse;
import com.payflow.entity.User;
import com.payflow.exception.DuplicateResourceException;
import com.payflow.exception.ResourceNotFoundException;
import com.payflow.mapper.UserMapper;
import com.payflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse registerUser(RegisterUserRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException(
                    "User already exists with email : " + request.getEmail()
            );
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public UserResponse updateUser(Long id , UpdateUserRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id : " + id
                ));

        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());

        User updateUser = userRepository.save(user);
        return userMapper.toResponse(updateUser);
    }

}
