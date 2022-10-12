package org.zhurko.blog.model;

import java.util.HashSet;
import java.util.Set;

public class Writer {

    private Long id;
    private String firstName;
    private String lastName;
    private Set<Post> posts = new HashSet<>();
    private Writer writer;

    public Writer(Long id, String firstName, String lastName, Set<Post> posts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.posts = posts;
    }

    public Writer(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Writer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Writer(Writer writer) {
        this.id = writer.getId();
        this.firstName = writer.getFirstName();
        this.lastName = writer.getLastName();
        this.posts = writer.getPosts();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}
