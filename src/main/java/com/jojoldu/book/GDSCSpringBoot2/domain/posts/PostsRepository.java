package com.jojoldu.book.GDSCSpringBoot2.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository 달아줄 필요 x - Entity class와 기본 Entity Repository는 함께 위치해야 한다.
public interface PostsRepository extends JpaRepository<Posts, Long> {
}
