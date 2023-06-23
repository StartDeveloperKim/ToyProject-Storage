package com.developer.smallRoom.dto.article.response;

import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.domain.article.Article;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ArticleResponse {

    private final Long articleId;
    private final String title;
    private final String subTitle;
    private final String content;
    private final String gitHubLink;
    private final String thumbnailUrl;
    private final String createAt;
    private List<String> tags;

    private final String membergithubId;
    private final Long memberId;

    private boolean updatable;

    public ArticleResponse(Article article) {
        this.articleId = article.getId();
        this.title = article.getTitle();
        this.subTitle = article.getSubTitle();
        this.content = article.getContent();
        this.gitHubLink = article.getGithubLink();
        this.thumbnailUrl = article.getThumbnailUrl();
        this.createAt = article.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy년MM월dd일"));
        this.membergithubId = article.getMember().getGitHubId();
        this.memberId = article.getMember().getId();
    }

    public void setUpdatable(MemberPrincipal memberPrincipal) {
        this.updatable = memberPrincipal != null && this.membergithubId.equals(memberPrincipal.getUsername());
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
