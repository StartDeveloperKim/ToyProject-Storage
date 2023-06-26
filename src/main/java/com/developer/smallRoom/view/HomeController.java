package com.developer.smallRoom.view;

import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.application.member.LoginMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    @GetMapping("/")
    public String index(@LoginMember MemberPrincipal memberPrincipal) {
        log.info("memberInfo : {}", memberPrincipal!=null ? memberPrincipal.toString() : "not memberPrincipal");
        // TODO :: 좋아요 및 날짜 순으로 조회할 수 있도록 해야하기에 standard를 매개변수로 지정했다. 나중에 수정해야함
        // TODO :: 첫 Index 페이지는 0으로 고정 글의 개수가 늘어나면 8개씩 로딩으로 수정
        // TODO :: 이번주 인기 게시글을 조회하는 로직도 추가해야한다.

        return "index";
    }
}
