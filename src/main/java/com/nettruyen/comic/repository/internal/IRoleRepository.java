package com.nettruyen.comic.repository.internal;

import com.nettruyen.comic.constant.RoleEnum;
import com.nettruyen.comic.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<RoleEntity, String> {

    // Optional không thể trả về null nên có thể dùng ifPresent để làm nhiều thứ hơn ở service
    Optional<RoleEntity> findByRoleName(RoleEnum roleName);
}
