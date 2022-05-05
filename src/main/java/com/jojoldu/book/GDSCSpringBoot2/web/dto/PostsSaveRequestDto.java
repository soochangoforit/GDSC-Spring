package com.jojoldu.book.GDSCSpringBoot2.web.dto;

import com.jojoldu.book.GDSCSpringBoot2.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * URL으로 부터 java code로 들어오는 역질렬화 과정에는 해당 dto에
 * 반드시 기본생성자 + getter 혹은 기본 생성자 + setter가 필요하다.
 */

/**
 * Entity 클래스와 거의 유사한 형태임에도 Dto 클래스를 추가로 생성했다.
 * 하지만, 절대로 Entity 클래스를 Request/Response 클래스로 사용해서는 안된다.
 *
 * 추가적으로 현재는 Save를 하기 위한 requestDto 라서 필드 값에 Long id 이런게 없다.
 * 개발을 하면서 , 합번이라도 DB에 들어갔다 나오는 그런 dto에 대해서는 Long id 를 갖도록 설계를 하자!!!!
 */
@Getter
@NoArgsConstructor // 기본 생성자
public class PostsSaveRequestDto {

    private String title;
    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Posts toEntity(){
        return Posts.builder().title(title).content(content).author(author).build();

    }


}
