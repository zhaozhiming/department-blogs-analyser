package com.github.dba.repo.write;

import com.github.dba.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BlogWriteRepository extends JpaRepository<Blog, Long> {

    @Transactional
    @Modifying
    @Query("update blogs b set b.title = :title, b.view = :view, b.comment = :comment, " +
            "b.author.groupName = :groupName, b.author.name = :name where b.id = :id")
    int updateBlogFor(@Param("id") Long id, @Param("title") String title,
                      @Param("view") int view, @Param("comment") int comment,
                      @Param("groupName") String groupName, @Param("name") String name);
}
