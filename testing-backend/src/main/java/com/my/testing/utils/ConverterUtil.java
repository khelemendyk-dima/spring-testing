package com.my.testing.utils;

import com.my.testing.dtos.UserDTO;
import com.my.testing.models.User;
import com.my.testing.payload.requests.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConverterUtil {
    private final ModelMapper modelMapper;

    public User convertToUser(RegisterRequest user) {
        return modelMapper.map(user, User.class);
    }

    public User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}
