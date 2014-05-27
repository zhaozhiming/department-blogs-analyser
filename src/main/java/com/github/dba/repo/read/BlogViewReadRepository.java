package com.github.dba.repo.read;

import com.github.dba.model.BlogView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogViewReadRepository extends JpaRepository<BlogView, Long> {

    @Query(value = "SELECT bv FROM blog_views bv WHERE bv.blogID = :blogId")
    List<BlogView> findByBlogId(@Param("blogId") Long blogId);
}
