package com.nettruyen.comic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "content", columnDefinition = "TEXT")
    String content;

    @Column(name = "chapter_number")
    Integer chapterNumber;

    // Chỉ load thông tin của tất cả chapter khi cần
    // * Mỗi API chỉ làm một công việc nhất định, FE có thể gọi API nhiều lần *
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    StoryEntity story;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chapter")
    Set<CommentEntity> comments = new HashSet<>();
}
