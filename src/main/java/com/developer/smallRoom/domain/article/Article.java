package com.developer.smallRoom.domain.article;

import com.developer.smallRoom.domain.comment.Comment;
import com.developer.smallRoom.domain.like.ArticleLike;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    // TODO :: subTitle을 두어 메인페이지에서 해당프로젝트를 간단히 설명할 수 있도록하자

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @Column(name = "github_link")
    private String githubLink;

    @Column(name = "hit", nullable = false)
    private int hit;

    @CreatedDate
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // TODO :: 좋아요 수, 댓글 수 통계컬럼을 두고 성능을 최적화하자.
//    @Column(name = "like_count", nullable = false)
//    private int likeCount;
//
//    @Column(name = "comment_count", nullable = false)
//    private int commentCount;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ArticleLike> articleLikes = new ArrayList<>();

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Article(String title, String content, String thumbnailUrl, String githubLink, Member member) {
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.githubLink = githubLink;
        this.member = member;
        this.hit = 0;
//        this.likeCount = 0;
//        this.commentCount = 0;
    }

    public void update(ArticleUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.thumbnailUrl = request.getThumbnailUrl();
    }

    // TODO :: 어느정도 프론트엔드와 백엔드 모두가 구현되고 나면 Jmeter를 통해 성능을 측정해보고 최적화를 해보자
    public void increaseHit() {
        this.hit++;
    }

    public void addArticleLike(ArticleLike articleLike) {
        this.articleLikes.add(articleLike);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

//    public void addLikeCount() {
//        this.likeCount++;
//    }
//
//    public void addCommentCount() {
//        this.commentCount++;
//    }
}
