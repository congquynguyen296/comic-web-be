package com.nettruyen.comic.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "tbl_comment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentEntity extends AbstractEntity {

    @Column(name = "content")
    @Lob
    String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    ChapterEntity chapter;

    @ManyToOne
    @JoinColumn(name ="parent_id")
    CommentEntity parent;   // Mỗi một bình luận chỉ có 1 cha

    @Column(name = "depth", nullable = false, columnDefinition = "int default 0")
    Integer depth;

    public void setParent(CommentEntity parent) {
        this.parent = parent;
        this.depth = (parent != null) ? parent.getDepth() + 1 : 0;
    }
}
