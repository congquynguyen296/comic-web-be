package com.nettruyen.comic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "tbl_generate")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenerateEntity extends AbstractEntity {

    @Column(name = "name")
    String name;

    @Column(name = "code")
    String code;

    @Column(name = "description")
    String description;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "generates")
    Set<StoryEntity> stories = new HashSet<>();
}
