package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.form.CommentCredentials;
import ru.itmo.wp.repository.CommentRepository;
import ru.itmo.wp.repository.PostRepository;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final UserService userService;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, UserService userService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreationTimeDesc();
    }

    public Post findById(Long id) {
        return id == null ? null : postRepository.findById(id).orElse(null);
    }

    public void addComment(Post post, CommentCredentials commentForm, long userId) {
        Comment comment = new Comment();
        comment.setText(commentForm.getText());
        post.addComment(comment);
        comment.addPost(post);
        comment.setUser(userService.findById(userId));
        commentRepository.save(comment);
    }
}
