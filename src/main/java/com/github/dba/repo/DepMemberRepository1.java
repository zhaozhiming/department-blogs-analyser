package com.github.dba.repo;

import com.github.dba.model.DepMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepMemberRepository1 extends JpaRepository<DepMember, Long> {

    @Query("select d from dep_members d where d.memberShort = :memberShort")
    DepMember findByMemberShort(@Param("memberShort") String memberShort);

}
