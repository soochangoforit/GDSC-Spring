package com.jojoldu.book.GDSCSpringBoot2.web.dto;

import com.jojoldu.book.GDSCSpringBoot2.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * List으로 반환되는 것들에는 content가 들어갈 필요가 없음으로
 * PostsListResponseDto 에는 아무거도 들어가지 않는다.
 */
@Getter
public class PostsListResponseDto {


    private Long id;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;

    public PostsListResponseDto(Posts entity) {
        this.id = entity.getId(); // 지금 보면 확실히 controller 랑 service 사이이 db_id가 확실히 같이 움직이는걸 확인할 수 있다.
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
    }

}
