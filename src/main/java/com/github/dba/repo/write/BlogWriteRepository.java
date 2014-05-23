package com.github.dba.repo.write;

import com.github.dba.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogWriteRepository extends JpaRepository<Blog, Long> {

}
