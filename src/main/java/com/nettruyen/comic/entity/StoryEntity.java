package com.nettruyen.comic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nettruyen.comic.constant.StatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;


@Entity(name = "tbl_story")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoryEntity extends AbstractEntity {

    @Column(name = "title")
    String title;

    @Column(name = "author")
    String author;

    @Column(name = "code")
    String code;

    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    StatusEnum status;

    @Column(name = "cover_image")
    String coverImage;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "story")
    Set<ChapterEntity> chapters = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "story")
    Set<FavoriteEntity> favorites = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tbl_story_generate",
            joinColumns = @JoinColumn(name = "story_id"),
            inverseJoinColumns = @JoinColumn(name = "generate_id")
    )
    Set<GenerateEntity> generates = new HashSet<>();
}
