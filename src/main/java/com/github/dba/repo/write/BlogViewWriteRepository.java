package com.github.dba.repo.write;

import com.github.dba.model.BlogView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BlogViewWriteRepository extends JpaRepository<BlogView, Long> {

    @Transactional
    @Modifying
    @Query(value = "delete FROM blog_views bv WHERE bv.blogTime < :time")
    void clear(@Param("time") long time);
}
