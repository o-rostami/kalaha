package com.bol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
@Transactional
public interface BaseRepository<E, I> extends JpaSpecificationExecutor<E>, JpaRepository<E, I> {
}
