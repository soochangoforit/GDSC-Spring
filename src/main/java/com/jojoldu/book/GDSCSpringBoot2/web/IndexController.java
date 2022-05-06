package com.jojoldu.book.GDSCSpringBoot2.web;

import com.jojoldu.book.GDSCSpringBoot2.service.posts.PostsService;
import com.jojoldu.book.GDSCSpringBoot2.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;


    /**
     * Model로 현재는 값이 들어와서 처리하는게 아니라 index로 Model 넣은 값을 뿌려주는 역할을 하는 Model이다.
     */
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("posts" , postsService.findAllDesc());
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
