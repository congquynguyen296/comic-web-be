package com.nettruyen.comic.entity;

import com.nettruyen.comic.constant.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "tbl_role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    RoleEnum roleName;

    @ManyToMany(mappedBy = "roles")
    Set<UserEntity> users = new HashSet<>();
}
