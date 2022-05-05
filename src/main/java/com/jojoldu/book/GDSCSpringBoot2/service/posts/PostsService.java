package com.jojoldu.book.GDSCSpringBoot2.service.posts;

import com.jojoldu.book.GDSCSpringBoot2.domain.posts.Posts;
import com.jojoldu.book.GDSCSpringBoot2.domain.posts.PostsRepository;
import com.jojoldu.book.GDSCSpringBoot2.web.dto.PostsResponseDto;
import com.jojoldu.book.GDSCSpringBoot2.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.GDSCSpringBoot2.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 정말 중요한 특징!!
 * Transactional 안에서 일어나는 조회, 수정, 삭제 같은 경우는 해당 entity가
 * 영속성 컨텍스트가 관리하게 된다.
 */
@RequiredArgsConstructor
@Service
public class PostsService {


    private final PostsRepository postsRepository;

    // RequiredArgsConstructor 에 의해서 생성자가 1개 생성되고, 생성자가 1개 이기 때문에 자동으로 Autowired가 붙게 되고
    // repository를 injection 받을 수 있다.

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    /**
     * service에 해당 하는 method는 주로 controller와 상호작용을 한다.
     * controller는 특별한 경우를 제외하고는 주로 id값을 service에 넘겨준다.
     * 따라서 service는 id값을 받을 수 있도록 , paramter를 설정해줘야 한다.
     *
     * posts.update는 하는 부분에 가령 content를 변경되지 않았더라도, method 파라미터 값에 넣어줌으로써
     * 변경 사항이 있는 상황에 고려해서 설계를 한다. 변경 되지 않는 값이 content에 들어가도 아무런 문제 X -> 변경 감지 기능 JPA가 알아서 실행
     *
     * update를 하고 Long으로 db_id값을 반환하는것을 확인할 수 있다 잘 봐두자!
     *
     * 추가적으로, controller를 제외 한 데이터의 처리(변경)가 발생하는 곳은 반드시 Transactional를 넣어줘야 한다.
     */
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    /**
     * 중요한 부분은 바로 Transactional 부분을 readOnlt = true 로 설정하는 것이다. 왜냐하면 update와 달리
     * findById로 데이터 변경(처리)가 발생하지 않고 조회만 하기 때문이다.
     *
     * 따라서 readOnly = true를 적용해준다.
     *
     * 그리고 db로 부터 찾은 entity를 responseDto에 담아서 반환한다. 절대 entity 그 자체를 내보내지 않는다.!!!
     *
     * 여기서 또 다른 중요한 특징!!
     * service도 controller랑 밀접한 연관이 있기 때문에 실제 db_id 값을 넘겨줌으로써, 다양하게 활용할 수 있도록 한다.
     *
     * PostsResponseDto 에는 controller에서 db_id 값을 보고 처리할 수 있도록, PostsResponseDto 에 Long id 필드값을 가지고 있다.
     *
     */
    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }


}
