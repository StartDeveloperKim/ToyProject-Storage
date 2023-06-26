package com.developer.smallRoom.view.like;

import com.developer.smallRoom.application.auth.jwt.JwtProperties;
import com.developer.smallRoom.application.auth.jwt.TokenAuthenticationFilter;
import com.developer.smallRoom.application.auth.jwt.TokenProvider;
import com.developer.smallRoom.application.auth.jwt.refreshToken.RefreshTokenRepository;
import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.like.ArticleLike;
import com.developer.smallRoom.domain.like.ArticleLikeRepository;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import com.developer.smallRoom.factory.ArticleFactory;
import com.developer.smallRoom.factory.MemberFactory;
import com.developer.smallRoom.jwt.JwtFactory;
import com.developer.smallRoom.jwt.TestJwtProperties;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleLikeControllerTest {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleLikeRepository articleLikeRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TokenProvider tokenProvider;
    @MockBean
    private JwtProperties jwtProperties;
    private final TestJwtProperties testJwtProperties = new TestJwtProperties();

    private Member member;
    private Article article;
    private String accessToken;

    /*
     * TODO :: 테스트코드 리팩토링 필요함
     *  - TestJwtProperties 만들어서 사용하기
     *  - MemberFactory, ArticleFactory 작성해서 중복코드 삭제하기
     * */

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new TokenAuthenticationFilter(tokenProvider, refreshTokenRepository)).build();
        articleRepository.deleteAll();
        memberRepository.deleteAll();
        articleLikeRepository.deleteAll();

        given(jwtProperties.getIssuer()).willReturn(testJwtProperties.getIssuer());
        given(jwtProperties.getSecretKey()).willReturn(testJwtProperties.getSecretKey());

        member = memberRepository.save(MemberFactory.getMemberDefaultValue());
        article = articleRepository.save(ArticleFactory.getDefaultValueArticle(member));
        accessToken = JwtFactory.builder()
                .subject(member.getGitHubId())
                .member(member)
                .build().createToken(testJwtProperties);
    }

    @DisplayName("로그인한 Member는 좋아요를 누를 수 있다.")
    @Test
    void saveLikeLoginMember() throws Exception {
        //given
        Long articleId = article.getId();
        //when
        ResultActions result = mvc.perform(post("/api/like/" + articleId)
                .cookie(getAccessTokenCookie(accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.result").value(true));
    }

    @DisplayName("로그인한 Member는 좋아요를 취소할 수 있다.")
    @Test
    void removeLikeLoginMember() throws Exception {
        //given
        articleLikeRepository.save(new ArticleLike(article, member));
        Long articleId = article.getId();
        //when
        ResultActions result = mvc.perform(delete("/api/like/" + articleId)
                .cookie(getAccessTokenCookie(accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.result").value(true));
    }

    private Cookie getAccessTokenCookie(String accessToken) {
        return new Cookie("access_token", accessToken);
    }

}