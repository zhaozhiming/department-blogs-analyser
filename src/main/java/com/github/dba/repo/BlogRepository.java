package com.github.dba.repo;

import com.github.dba.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query("select b from blogs b where b.blogId = :blogId and b.website = :website")
    Blog findByBlogIdAndWebsite(@Param("blogId") String blogId,
                                @Param("website") String website);

    @Transactional
    @Modifying
    @Query("update blogs b set b.title = :title, b.view = :view, b.comment = :comment, " +
            "b.author.groupName = :groupName, b.author.name = :name where b.id = :id")
    int updateBlogFor(@Param("id") Long id, @Param("title") String title,
                      @Param("view") int view, @Param("comment") int comment,
                      @Param("groupName") String groupName, @Param("name") String name);
}
