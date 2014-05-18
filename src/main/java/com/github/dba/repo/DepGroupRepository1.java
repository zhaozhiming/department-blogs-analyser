package com.github.dba.repo;

import com.github.dba.model.DepGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepGroupRepository1 extends JpaRepository<DepGroup, Long> {

    DepGroup findByGroupShort(String groupShort);

}
