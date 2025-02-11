package com.nettruyen.comic.mapper;

import com.nettruyen.comic.dto.request.UserCreationRequest;
import com.nettruyen.comic.dto.response.UserResponse;
import com.nettruyen.comic.entity.UserEntity;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Role sẽ được map ở service
    @Mapping(target = "roles", ignore = true)
    UserEntity toUserEntity(UserCreationRequest userCreationRequest);

    @Mapping(target = "roles", ignore = true)
    UserResponse toUserResponse(UserEntity userEntity);
}
