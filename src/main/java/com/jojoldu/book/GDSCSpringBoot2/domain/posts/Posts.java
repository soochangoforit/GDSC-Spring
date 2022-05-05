package com.jojoldu.book.GDSCSpringBoot2.domain.posts;

import com.jojoldu.book.GDSCSpringBoot2.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity
@Getter @NoArgsConstructor
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }


    /**
     * Entity Class에서 setter를 만드는 대신,
     * update 될 가능성이 있는 필드를 가지는 Update Method를 사용한다.
     *
     * service 시점에서 비슷한 역할을 수행하는 update method가 만들어질것이며
     * 해당 service에서는 db_id 값을 가지고 있기 때문에 실제 Posts Data를 Db에서 가져오고
     *
     * Db으로 가져온 entity (Posts, 영속성 컨텍스트에 반영된) 에서 하위 update를 호출하여 해당 영속성 컨텍스트에서 관리되고 있는
     * 객체의 상태를 변경시켜 준다. -> 변경 감지 기능 발동! 실제 update 쿼리를 만들 필요 X
     *
     * 현 시점에서는 핵심!! entity에서 변경이 일어날 경우, update method는 변경이 될만 한 부분의 필드르 모두 포함하는 파라미터를 가지고
     * 객체의 상태를 this. 를 통해서 변경 시킨다.
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
