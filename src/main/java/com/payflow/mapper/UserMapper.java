package com.payflow.mapper;

import com.payflow.dto.response.UserResponse;
import com.payflow.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}
