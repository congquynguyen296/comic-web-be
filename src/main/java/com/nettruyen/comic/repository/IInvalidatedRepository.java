package com.nettruyen.comic.repository;

import com.nettruyen.comic.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInvalidatedRepository extends JpaRepository<InvalidatedToken, String> {

}
