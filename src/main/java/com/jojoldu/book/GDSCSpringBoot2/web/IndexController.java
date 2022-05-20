package com.jojoldu.book.GDSCSpringBoot2.web;

import com.jojoldu.book.GDSCSpringBoot2.config.auth.dto.SessionUser;
import com.jojoldu.book.GDSCSpringBoot2.service.posts.PostsService;
import com.jojoldu.book.GDSCSpringBoot2.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;


    /**
     * Model로 현재는 값이 들어와서 처리하는게 아니라 index로 Model 넣은 값을 뿌려주는 역할을 하는 Model이다.
     */
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("posts" , postsService.findAllDesc());

        SessionUser  user = (SessionUser) httpSession.getAttribute("user"); // 세션은 단지 로그인 했을때, 자동 로그아웃을 위해?? 실제 유저가 뭘 했는지 유저가 수정 등록을 햇을때는 db_id가 들어간 DB에서 조회를 해서 처리해야한다.

        if(user != null) {
            model.addAttribute("userName", user.getName());
        }


        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }


    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }



}
