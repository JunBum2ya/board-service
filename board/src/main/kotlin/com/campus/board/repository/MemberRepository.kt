package com.campus.board.repository

import com.campus.board.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member,String> {
}