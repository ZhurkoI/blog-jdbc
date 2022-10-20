package org.zhurko.blog.service;

import org.zhurko.blog.model.Post;
import org.zhurko.blog.repository.PostRepository;

import java.util.List;

public class PostService {

    private final PostRepository postRepo;

    public PostService(PostRepository postRepo) {
        this.postRepo = postRepo;
    }

    public Post save(Post post) {
        return postRepo.save(post);
    }

    public List<Post> getAll() {
        return postRepo.getAll();
    }

    public Post getById(Long id) {
        return postRepo.getById(id);
    }

    public void deleteById(Long id) {
        postRepo.deleteById(id);
    }

    public Post update(Post post) {
        return postRepo.update(post);
    }
}
