package com.nettruyen.comic.entity;

import com.nettruyen.comic.constant.StatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "tbl_chapter")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChapterEntity extends AbstractEntity {

    @Column(name = "title")
    String title;

    @Lob    // Text trong db
    @Column(name = "content")
    String content;

    @Column(name = "chapter_number")
    String chapterNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "story_id")
    StoryEntity story;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chapter")
    Set<CommentEntity> comments = new HashSet<>();
}
