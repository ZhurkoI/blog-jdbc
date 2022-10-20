package org.zhurko.blog.model;

import java.util.HashSet;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Writer writer1 = (Writer) o;
        return Objects.equals(id, writer1.id) && Objects.equals(firstName, writer1.firstName)
                && Objects.equals(lastName, writer1.lastName) && Objects.equals(posts, writer1.posts)
                && Objects.equals(writer, writer1.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, posts, writer);
    }
}
