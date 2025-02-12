package com.nettruyen.comic.service;

import com.nettruyen.comic.constant.RoleEnum;
import com.nettruyen.comic.dto.request.authentication.RoleCreationRequest;

public interface IRoleService {

    RoleEnum createRole(RoleCreationRequest role);

    RoleEnum updateRole(RoleEnum role);
}
