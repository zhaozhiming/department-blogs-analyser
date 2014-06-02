package com.github.dba.repo.read;

import com.github.dba.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogReadRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from blogs b where b.blogId = :blogId and b.website = :website")
    Blog findByBlogIdAndWebsite(@Param("blogId") String blogId,
                                @Param("website") String website);

    @Query(value = "select b from blogs b where b.time <= :time and b.author.groupName = :groupName")
    List<Blog> topDetail(@Param("time") Long time, @Param("groupName") String groupName);

    @Query("select b from blogs b where b.time >= :time")
    List<Blog> findAfterTime(@Param("time") Long time);
}
