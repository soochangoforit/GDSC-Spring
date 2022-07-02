package com.jojoldu.book.GDSCSpringBoot2.config.auth.dto;


import com.jojoldu.book.GDSCSpringBoot2.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

// session에 user라는 이름으로 SessionUser 클래스 dto 객체르 저장
// sessionUser에는 인증된 사용자 정보만 필요
// 즉 세션 객체는 이미 DB에 저장된 ( 즉, 인증된 사용자 ) 만을 저장하고 있다.
// 무조건 session에 들어가기전에 Db에 해당 유저 정보가 들어가 있다.
@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }

}
