package com.nettruyen.comic.repository.internal;

import com.nettruyen.comic.entity.GenerateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGenerateRepository extends JpaRepository<GenerateEntity, String> {
    GenerateEntity findByName(String name);

    GenerateEntity findByCode(String code);
}
