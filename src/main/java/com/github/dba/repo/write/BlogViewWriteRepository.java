package com.github.dba.repo.write;

import com.github.dba.model.BlogView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogViewWriteRepository extends JpaRepository<BlogView, Long> {

}
