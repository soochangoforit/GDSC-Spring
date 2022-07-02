package com.jojoldu.book.GDSCSpringBoot2.config.auth;

import com.jojoldu.book.GDSCSpringBoot2.config.auth.dto.OAuthAttributes;
import com.jojoldu.book.GDSCSpringBoot2.config.auth.dto.SessionUser;
import com.jojoldu.book.GDSCSpringBoot2.domain.user.User;
import com.jojoldu.book.GDSCSpringBoot2.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;


@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;


    // 파라미터로 넘어오는 userRequest에는
    // 어떤 서비스로부터 로그인을 했는지에 대한 id값(일종의 서비스를 구분하고자 하는 key)이 나와있고
    // 로그인 한 사용자의 고유한 key 값을 가지고 있으며
    // 서비스로부터 제공받은 로그인에 성공한 사용자의 정보가 OAuth2User라는 Class 안에 Map 형식으로
    // 사용자의 정보가 key - value 형식으로 담겨져 있다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 구글 로그인시 로그인에 성공하면 구글 서비스로부터 request 형식으로 사용자의 정보를 제공한다.
        // 해당 대리자는 OAuth2UserService의 기본 대리자 이며
        // OAuth2UserRequest 가 들어오면 OAuth2User 를 반환할 수 있는 loadUser 메소드를 갖고 있다.
        // 제네릭을 이용해서 직접 요청과 응답의 dataType을 지정할 수 있다.
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

        // 구글 서비스로부터 온 request에서 대리자(delegate)의 loadUser 메소드를 통해 , 로그인에 성공한 사용자의 정보를
        // OAuth2User의 Data Type으로 반환한다. (해당 유저의 정보가 담겨있다.)
        // OAuth2User의 Class는 필드로 해당 유저의 정보가 담겨 있는데 Map 형식으로 key-value로 생성되어 있다.
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // registrationId 현재 로그인 진행 중인 서비스를 구분하는 코드. - 네이버 추가 로그인 연동시 네이버 로그인인지, 구글 로그인인지 구분하기 위해 사용
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 진행시 키가 되는 필그값을 이야기한다. pk와 같은 의미이다. (구글의 경우 기본적인 코드 "sub" , 네이버 카카오등은 지원 X , naver ,google 로그인 동시 지원할때 사용
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService를 통해 가져온 Oauth2user의 정보를?? attribute를 담을 클래스이다. 이후 네이버 등 다른 소셜 로그인도 이 클래스 사용
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes); // 실제 DB에 구글 로그인을 통해서 얻은 정보를 저장 ,반환 값은 저장된 user 정보를 담고 있는 entity

        // sessionUser 세션에 사용자 정보를 저장하기 위한 DTO 클래스 - User Class를 활용해서 넣는다. session에 넣는 시점에는 로그인한 해당 유저의
        // ROLE 정보를 담지 않고 기본 정보만 담아서 session에 저장한는 역할
        // session에 저장할때는 dB에 무조건 해당 유저 정보가 들어간 상태에서 관리를 하게 된다.
        // 세션에 저장할때는 절대 User 클래스 객체 자체를 넣으면 안된다.
        // session에는 반드시 직렬화(Serializable)가 구현된 객체를 넣어야 한다.
        httpSession.setAttribute("user", new SessionUser(user));


        // 이거의 역할이 뭘까???
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes

                        .getAttributes(),
                attributes.getNameAttributeKey());
    }


    // 구글 사용자 정보가 업데이트 되었을대 대비하여 생성
    // 사진 , 이메일 변경이 되면 dto 인  OAuthAttributes 를 Entity로 변환하여 repository에 저장
    // 이미 Oauth 연동이 되어서 Email이 DB에 있으면 Update 하고 , 처음 연동한다고 하면 DB에 해당 정보를 저장
    private User saveOrUpdate(OAuthAttributes attributes) {

        //반환되는 User에는 db_id값을 제외한 name , email , picture, Role이 담겨져 있다.
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }

}
