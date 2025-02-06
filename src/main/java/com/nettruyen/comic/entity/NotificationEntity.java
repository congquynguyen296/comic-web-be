package com.nettruyen.comic.entity;


import com.nettruyen.comic.constant.NotificationTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity(name = "tbl_notification")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationEntity extends AbstractEntity {

    @Column(name = "content")
    @Lob
    String content;

    @Column(name = "is_read")
    boolean isRead;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    NotificationTypeEnum type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;
}
