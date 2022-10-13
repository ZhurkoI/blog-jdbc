package org.zhurko.blog.controller;

import org.zhurko.blog.model.Label;
import org.zhurko.blog.model.Post;
import org.zhurko.blog.repository.LabelRepository;
import org.zhurko.blog.repository.PostRepository;

import java.util.List;
import java.util.Set;

public class PostController {

    private final PostRepository postRepo;
    private final LabelRepository labelRepo;

    public PostController(PostRepository postRepo, LabelRepository labelRepo) {
        this.postRepo = postRepo;
        this.labelRepo = labelRepo;
    }

    public Post saveNewPost(String input) {
        return postRepo.save(new Post(input));
    }

    public List<Post> getAll() {
        return postRepo.getAll();
    }

    public Post getPostById(Long id) {
        return postRepo.getById(id);
    }

    public void deleteById(Long id) {
        postRepo.deleteById(id);
    }

    public Post updatePost(Long id, String newContent) {
        Post post = postRepo.getById(id);
        post.setContent(newContent);
        return postRepo.update(post);
    }

    public Post addLabel(Long postId, Long labelId) {
        Post post = postRepo.getById(postId);
        Label label = labelRepo.getById(labelId);
        post.getLabels().add(label);
        return postRepo.update(post);
    }

    public Post removeLabel(Long postId, Long labelId) {
        Post post = postRepo.getById(postId);
        Set<Label> labels = post.getLabels();
        Label label = labelRepo.getById(labelId);
        labels.remove(label);
        post.setLabels(labels);
        return postRepo.update(post);
    }
}
