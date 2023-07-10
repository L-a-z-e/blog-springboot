package blog.laze.dto;

import blog.laze.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {

    private String title;
    private String content;

    public Article toEntity(String author){
        return Article.builder()
                .author(author)
                .title(title)
                .content(content)
                .build();
    }
}
