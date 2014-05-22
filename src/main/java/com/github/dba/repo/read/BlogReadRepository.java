package com.github.dba.repo.read;

import com.github.dba.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlogReadRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from blogs b where b.blogId = :blogId and b.website = :website")
    Blog findByBlogIdAndWebsite(@Param("blogId") String blogId,
                                @Param("website") String website);
}
