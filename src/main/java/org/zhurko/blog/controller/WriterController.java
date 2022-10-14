package org.zhurko.blog.controller;

import org.zhurko.blog.model.Post;
import org.zhurko.blog.model.Writer;
import org.zhurko.blog.repository.jdbc.JdbcPostRepositoryImpl;
import org.zhurko.blog.repository.jdbc.JdbcWriterRepositoryImpl;
import org.zhurko.blog.service.PostService;
import org.zhurko.blog.service.WriterService;

import java.util.List;
import java.util.Set;

public class WriterController {

    private static final String BLANK_INPUT_ERROR_MESSAGE = "Name cannot be zero-length or contain only spaces.";

    private final WriterService writerService = new WriterService(new JdbcWriterRepositoryImpl());
    private final PostService postService = new PostService(new JdbcPostRepositoryImpl());

    public List<Writer> getAll() {
        return writerService.getAll();
    }

    public Writer getWriterById(Long id) {
        return writerService.getById(id);
    }

    public Writer save(String firstName, String lastName) {
        if (firstName.isBlank() || lastName.isBlank()) {
            System.out.println(BLANK_INPUT_ERROR_MESSAGE);
            return null;
        }

        return writerService.save(new Writer(firstName, lastName));
    }

    public void deleteById(Long id) {
        writerService.deleteById(id);
    }

    public Writer updateWriter(Long id, String firstName, String lastName) {
        if (firstName.isBlank() || lastName.isBlank()) {
            System.out.println(BLANK_INPUT_ERROR_MESSAGE);
            return null;
        }

        Writer writer = writerService.getById(id);
        writer.setFirstName(firstName);
        writer.setLastName(lastName);
        return writerService.update(writer);
    }

    public Writer addPost(Long writerId, Long postId) {
        Writer writer = writerService.getById(writerId);
        Post post = postService.getById(postId);
        writer.getPosts().add(post);
        return writerService.update(writer);
    }

    public Writer removePost(Long writerId, Long postId) {
        Writer writer = writerService.getById(writerId);
        Set<Post> posts = writer.getPosts();
        Post post = postService.getById(postId);
        posts.remove(post);
        writer.setPosts(posts);
        return writerService.update(writer);
    }
}
