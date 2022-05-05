package com.jojoldu.book.GDSCSpringBoot2.web.dto;

import com.jojoldu.book.GDSCSpringBoot2.domain.posts.Posts;
import lombok.Getter;

/**
 * id 값을 통해서 게시글을 조회 하고자 하는 요청이 오면
 * 해당 결과 값을 반환해주는 dto
 * 결과 값을 반환하는 dto 는 내부적으로 id값을 포함해서 반환한다.
 *
 *반환 클래스는 전체 필드의 생성자 + (Posts Class) entity에서 보내는 Posts 정보에서 값을 가져오기 위해 getter 사용
 * entity로 부터 받은 데이터를 가지고 dto를 생성하여 controller로 반환
 */
@Getter
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}