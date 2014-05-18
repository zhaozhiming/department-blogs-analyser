package com.github.dba.repo;

import com.github.dba.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query("select b from blogs b where b.blogId = :blogId and b.website = :website")
    Blog findByBlogIdAndWebsite(@Param("blogId") String blogId,
                                @Param("website") String website);

}
