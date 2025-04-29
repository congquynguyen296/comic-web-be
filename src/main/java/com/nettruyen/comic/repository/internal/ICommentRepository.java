package com.nettruyen.comic.repository.internal;

import com.nettruyen.comic.entity.ChapterEntity;
import com.nettruyen.comic.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICommentRepository extends JpaRepository<CommentEntity, String> {

    List<CommentEntity> findByParentOrderByCreatedAtAsc(CommentEntity parent);

    List<CommentEntity> findByChapterAndParentIsNullOrderByCreatedAtDesc(ChapterEntity chapterEntity);
}
