package com.developer.smallRoom.domain.like;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ArticleLikeRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    private Member member;
    private Article article;

    @BeforeEach
    void setUp() {
        articleLikeRepository.deleteAll();
        articleRepository.deleteAll();
        memberRepository.deleteAll();

        member = memberRepository.save(Member.builder()
                .gitHubId("testId")
                .name("testName")
                .gitHubId("testImage")
                .build());
        article = articleRepository.save(Article.builder()
                .title("title")
                .content("content")
                .thumbnailUrl("thumbnail")
                .member(member)
                .build());
    }

    @DisplayName("existsArticleLikeByArticleAndMember() : Article과 Member로 좋아요가 있는지 찾는다.")
    @Test
    void existsArticleLikeByArticleAndMember() {
        //given
        savedArticleLike();
        //when
        boolean result = articleLikeRepository.existsArticleLikeByArticleAndMember(article, member);
        //then
        assertThat(result).isTrue();
    }

    @DisplayName("findByArticleAndMember() : Article과 Member로 좋아요엔티티를 찾는다.")
    @Test
    void findByArticleAndMember() {
        //given
        ArticleLike articleLike = savedArticleLike();
        //when
        Optional<ArticleLike> findArticleLike = articleLikeRepository.findByArticleAndMember(article, member);
        //then
        assertThat(articleLike.getId()).isEqualTo(findArticleLike.get().getId());
    }

    private ArticleLike savedArticleLike() {
        return articleLikeRepository.save(new ArticleLike(article, member));
    }
}