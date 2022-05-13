package com.jojoldu.book.GDSCSpringBoot2.config.auth.dto;

import com.jojoldu.book.GDSCSpringBoot2.domain.user.Role;
import com.jojoldu.book.GDSCSpringBoot2.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

// 해당 클래스는 DTO이다. 구글 연동 로그인 시 가져오는 사용자의 연동주체자, pk , 유저 정보를 담는다.
@Getter
public class OAuthAttributes {


    private Map<String, Object> attributes; // 구글 연동 로그인 확인시 가져오는 각종 정보들이 map 형태로 저장됨???
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    // OAuth2User에서 반환되는 사용자 정보는 Map이기 때문에 값을 하나하나를 변환해야만 한다.
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }

        return ofGoogle(userNameAttributeName, attributes); // 2번째 user 식별자 pk와 뒤에는 유저 정보
    }

    // 구글 서비스에서 가져오는 데이터를 담는 dto 생성 메소드 , 서비스로부터 유저 정보를 가져와서 OAuthAttributes DTO를 반환하는 메소드
    // 구글로부터 가져온 각 정보들을 날것으로 3개 필드로 담고, 전체적으로 map으로 한번 담고 , pk역할을 하는것을 담는다.
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    // naver 같은 경우는 네이버로부터 날것의 정보가 올떄 response 라는 이름의 json으로 날라온다.
    // 네이버로부터 가져온 각 정보들을 날것으로 3개 필드로 담고, 전체적으로 map으로 한번 담고 , pk역할을 하는것을 담는다.
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    // DTO에 저장된 구글 연동된 정보를 Entity로 전환
    // 처음 연동 했을때만 Entity를 직접 생성하여 DB에 저장, 한번 연동 되어서 가입된 유저는 Entity로 변환 X
    // 가입할때의 기본 권한을 User로 주기 위해서 role 빌더 값에는 Role.Guest를 사용한다.
    // entity를 저장혹은 upate가 DB에 일어나면 다음으로 Session클래스를 생성한다.
    // session에 생서된 user를 넣는다.
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }


}
