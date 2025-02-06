package com.nettruyen.comic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "tbl_banner")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BannerEntity extends AbstractEntity {

    @Column(name = "image")
    String image;

    @Column(name = "link")
    String link;

    @Column(name = "priority")
    Integer priority;
}
