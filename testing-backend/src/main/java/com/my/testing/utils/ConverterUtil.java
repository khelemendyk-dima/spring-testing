package com.my.testing.utils;

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

}
