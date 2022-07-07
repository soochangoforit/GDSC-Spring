package com.jojoldu.book.GDSCSpringBoot2.config.auth;

import com.jojoldu.book.GDSCSpringBoot2.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                    //.antMatchers("/api/v1/**").hasRole(Role.USER.name()) //게시글 등록 과정에 /api 경로로 들어간다. USER가 아니면 못 한다.
                    .anyRequest().authenticated() // 나머지 예를 들어 로그인 하지 않고 /post/save 할 경우 , 인증이 되어 있지 않기 때문에 로그인하라고 창이 나온다 Oauth로
                .and()
                    .formLogin()
                    .loginPage("/test3")
                     .permitAll()
                .and()
                .logout()
                    .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                        .userService(customOAuth2UserService);
    }


}
