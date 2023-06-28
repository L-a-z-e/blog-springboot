package blog.laze.service;

import blog.laze.domain.Article;
import blog.laze.dto.AddArticleRequest;
import blog.laze.dto.UpdateArticleRequest;
import blog.laze.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor // final이 붙거나 NotNull이 붙은 필드의 생성자 추가
@Service
public class BlogService
{
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request){

        return blogRepository.save(request.toEntity());

    }

    public List<Article> findAll(){

        return blogRepository.findAll();

    }

    public Article findById(long id){

        return blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found: " + id));

    }

    public void delete(long id){
        blogRepository.deleteById(id);
    }
@Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }

}
