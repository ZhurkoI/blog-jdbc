package org.zhurko.blog.controller;

import org.zhurko.blog.model.Post;
import org.zhurko.blog.model.Writer;
import org.zhurko.blog.repository.PostRepository;
import org.zhurko.blog.repository.WriterRepository;
import org.zhurko.blog.repository.sql.SqlWriterRepositoryImpl;
import org.zhurko.blog.repository.sql.SqlPostRepositoryImpl;

import java.util.List;
import java.util.Set;

public class WriterController {

    private final WriterRepository writerRepo = new SqlWriterRepositoryImpl();
    private final PostRepository postRepo = new SqlPostRepositoryImpl();

    public List<Writer> getAll() {
        return writerRepo.getAll();
    }

    public Writer getWriterById(Long id) {
        return writerRepo.getById(id);
    }


    public Writer saveNewWriter(String firstName, String lastName) {
        return writerRepo.save(new Writer(firstName, lastName));
    }

    public void deleteById(Long id) {
        writerRepo.deleteById(id);
    }

    public Writer updateWriter(Long id, String firstName, String lastName) {
        Writer writer = writerRepo.getById(id);
        writer.setFirstName(firstName);
        writer.setLastName(lastName);
        return writerRepo.update(writer);
    }

    public Writer addPost(Long writerId, Long postId) {
        Writer writer = writerRepo.getById(writerId);
        Post post = postRepo.getById(postId);
        writer.getPosts().add(post);
        return writerRepo.update(writer);
    }

    public Writer removePost(Long writerId, Long postId) {
        Writer writer = writerRepo.getById(writerId);
        Set<Post> posts = writer.getPosts();
        Post post = postRepo.getById(postId);
        posts.remove(post);
        writer.setPosts(posts);
        return writerRepo.update(writer);
    }
}
