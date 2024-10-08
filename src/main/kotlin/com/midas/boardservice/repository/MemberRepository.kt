package com.midas.boardservice.repository;

import com.midas.boardservice.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, String> {
}