package com.nettruyen.comic.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nettruyen.comic.constant.RoleEnum;
import com.nettruyen.comic.dto.request.authentication.RoleCreationRequest;
import com.nettruyen.comic.dto.response.ApiResponse;
import com.nettruyen.comic.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleController {

    IRoleService roleService;


    @PostMapping
    ApiResponse<RoleEnum> createRole(@RequestBody RoleCreationRequest request) {

        return ApiResponse.<RoleEnum>builder()
                .code(200)
                .result(roleService.createRole(request))
                .build();
    }
}
