package com.jojoldu.book.GDSCSpringBoot2.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * web으로 부터 수정하고자 하는 값을 담은 dto를 service로 보내는 역할
 *
 * 현재 요구사항을 유추를 해본다면 제목과 내용이 수정 가능성이 있다.
 *
 * 현재 PostsUpdateRequestDto 에는 db_id 값을 가지는 Long id값이 없다.
 * 왜냐하면 개발자 설계 맘대로 update를 할때 id와 dto를 따로 구분했기 때문이다.
 * PostsUpdateRequestDto 에 Long id를 넣어서 설계를 해도 큰 상관은 없다.
 */
@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private String title;
    private String content;

    @Builder
    public PostsUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}