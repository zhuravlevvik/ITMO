package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.Role;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.form.CommentCredentials;
import ru.itmo.wp.form.validator.CommentCredentialsValidator;
import ru.itmo.wp.security.AnyRole;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.CommentService;
import ru.itmo.wp.service.PostService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class PostPage extends Page {
    private final PostService postService;
    private final CommentService commentService;
    private final CommentCredentialsValidator commentCredentialsValidator;

    public PostPage(PostService postService, CommentService commentService, CommentCredentialsValidator commentCredentialsValidator) {
        this.postService = postService;
        this.commentService = commentService;
        this.commentCredentialsValidator = commentCredentialsValidator;
    }

    @InitBinder("postCommentForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(commentCredentialsValidator);
    }

    @Guest
    @GetMapping(value = {"/post", "/post/{stringId}"})
    public String postGet(@PathVariable(required = false) String stringId,
                          Model model,
                          HttpSession httpSession) {
        Long id = validateId(stringId);
        Post post = postService.findById(id);
        if (post == null) {
            putMessage(httpSession, "Unknown post-id");
            return "redirect:/";
        }
        model.addAttribute("post", post);
        model.addAttribute("comments", commentService.findAllByPostId(post.getId()));
        model.addAttribute("postCommentForm", new CommentCredentials());

        return "PostPage";
    }

    @AnyRole({Role.Name.WRITER, Role.Name.ADMIN})
    @PostMapping(value = {"/post/add-comment", "/post//add-comment", "/post/{stringId}/add-comment"})
    public String addComment(@PathVariable(required = false) String stringId,
                             @Valid @ModelAttribute("postCommentForm") CommentCredentials commentForm,
                             BindingResult bindingResult,
                             HttpSession httpSession) {
        Long id = validateId(stringId);
        Post post = postService.findById(id);
        if (post == null) {
            putMessage(httpSession,"Unknown post-id");
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            return "redirect:/";
        }

        Long userId = (Long) httpSession.getAttribute("userId");

        postService.addComment(post, commentForm, userId);

        return "redirect:/post/" + stringId;
    }

    private Long validateId(String stringId) {
        Long id = null;
        try {
            id = Long.parseLong(stringId);
        } catch (Exception ignored) {
            // do nothing
        }
        return id;
    }

}
