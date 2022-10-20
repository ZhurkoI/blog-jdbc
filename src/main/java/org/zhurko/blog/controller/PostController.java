package org.zhurko.blog.controller;

import org.zhurko.blog.model.Label;
import org.zhurko.blog.model.Post;
import org.zhurko.blog.repository.jdbc.JdbcLabelRepositoryImpl;
import org.zhurko.blog.repository.jdbc.JdbcPostRepositoryImpl;
import org.zhurko.blog.service.LabelService;
import org.zhurko.blog.service.PostService;

import java.util.List;
import java.util.Set;

public class PostController {

    private static final String BLANK_INPUT_ERROR_MESSAGE = "The post content cannot be zero-length or " +
            "contain only spaces.";

    private final PostService postService = new PostService(new JdbcPostRepositoryImpl());
    private final LabelService labelService = new LabelService(new JdbcLabelRepositoryImpl());

    public Post save(String input) {
        if (input.isBlank()) {
            System.out.println(BLANK_INPUT_ERROR_MESSAGE);
            return null;
        }

        return postService.save(new Post(input));
    }

    public List<Post> getAll() {
        return postService.getAll();
    }

    public Post getPostById(Long id) {
        return postService.getById(id);
    }

    public void deleteById(Long id) {
        postService.deleteById(id);
    }

    public Post updatePost(Long id, String newContent) {
        if (newContent.isBlank()) {
            System.out.println(BLANK_INPUT_ERROR_MESSAGE);
            return null;
        }

        Post post = postService.getById(id);
        post.setContent(newContent);
        return postService.update(post);
    }

    public Post addLabel(Long postId, Long labelId) {
        Post post = postService.getById(postId);
        Label label = labelService.getById(labelId);
        post.getLabels().add(label);
        return postService.update(post);
    }

    public Post removeLabel(Long postId, Long labelId) {
        Post post = postService.getById(postId);
        Set<Label> labels = post.getLabels();
        Label label = labelService.getById(labelId);
        labels.remove(label);
        post.setLabels(labels);
        return postService.update(post);
    }
}
