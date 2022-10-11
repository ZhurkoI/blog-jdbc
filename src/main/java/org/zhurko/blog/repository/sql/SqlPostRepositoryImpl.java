package org.zhurko.blog.repository.sql;

import org.zhurko.blog.model.Label;
import org.zhurko.blog.model.Post;
import org.zhurko.blog.model.PostStatus;
import org.zhurko.blog.repository.PostRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SqlPostRepositoryImpl implements PostRepository {

    private static final String INSERT_POST = "INSERT INTO posts (content, created, updated, post_status) " +
            "VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_NOT_DELETED_POSTS = "SELECT * FROM posts WHERE post_status NOT LIKE 'DELETED'";
    private static final String GET_POST_BY_ID = "SELECT * FROM posts WHERE id = ? AND post_status NOT LIKE 'DELETED'";
    private static final String GET_POST_LABELS = "SELECT DISTINCT labels.id, labels.name " +
            "FROM posts LEFT JOIN post_label ON posts.id = post_label.post_id " +
            "LEFT JOIN labels ON post_label.label_id = labels.id " +
            "WHERE posts.id = (?) " +
            "ORDER BY labels.id;";
    private static final String DELETE_POST_BY_ID = "UPDATE posts SET updated = ?, post_status = ? WHERE id = ?";
    private static final String UPDATE_POST = "UPDATE posts SET content = ?, updated = ?, post_status = ? WHERE id = ?";
    private static final String ADD_LABEL_TO_POST = "INSERT INTO post_label (post_id, label_id) VALUES (?, ?)";
    private static final String REMOVE_LABEL_FROM_POST = "DELETE FROM post_label WHERE post_id = ? AND label_id = ?";

    @Override
    public Post getById(Long id) {
        Post post = null;

        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(GET_POST_BY_ID)) {
            prepStatement.setLong(1, id);
            ResultSet resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                post = new Post(
                        resultSet.getLong("id"),
                        resultSet.getString("content"),
                        resultSet.getTimestamp("created"),
                        resultSet.getTimestamp("updated"),
                        PostStatus.valueOf(resultSet.getString("post_status"))
                );
                Set<Label> labels = getLabelsRelatedToPost(post);
                post.setLabels(labels);
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read data from DB.");
        }

        return post;
    }

    @Override
    public List<Post> getAll() {
        return getAllPostsInternal();
    }

    @Override
    public Post save(Post post) {
        Post savedPost = null;
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(INSERT_POST,
                     Statement.RETURN_GENERATED_KEYS)) {
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            prepStatement.setString(1, post.getContent());
            prepStatement.setTimestamp(2, timestamp);
            prepStatement.setTimestamp(3, timestamp);
            prepStatement.setString(4, PostStatus.UNDER_REVIEW.name());
            prepStatement.executeUpdate();

            ResultSet resultSet = prepStatement.getGeneratedKeys();
            while (resultSet.next()) {
                savedPost = new Post(
                        resultSet.getLong(1),
                        post.getContent(),
                        date,
                        date,
                        PostStatus.UNDER_REVIEW
                );
            }
        } catch (SQLException ex) {
            System.out.printf("Cannot save '%s' to DB.%n", post.getContent());
        }

        return savedPost;
    }

    @Override
    public Post update(Post post) {
        Post updatedPost = null;

        Set<Label> linkedLabels = getLabelsRelatedToPost(post);

        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement updatePost = connection.prepareStatement(UPDATE_POST);
             PreparedStatement addLabel = connection.prepareStatement(ADD_LABEL_TO_POST);
             PreparedStatement removeLabel = connection.prepareStatement(REMOVE_LABEL_FROM_POST)) {
            connection.setAutoCommit(false);

            Timestamp timestamp = new Timestamp(new Date().getTime());
            updatePost.setString(1, post.getContent());
            updatePost.setTimestamp(2, timestamp);
            updatePost.setString(3, PostStatus.ACTIVE.name());
            updatePost.setLong(4, post.getId());
            updatePost.executeUpdate();

            if (post.getLabels().size() > linkedLabels.size()) {
                post.getLabels().removeAll(linkedLabels);
                Iterator<Label> iterator = post.getLabels().iterator();
                Label newLabel = iterator.next();
                addLabel.setLong(1, post.getId());
                addLabel.setLong(2, newLabel.getId());
                addLabel.executeUpdate();
            } else if (post.getLabels().size() < linkedLabels.size()) {
                linkedLabels.removeAll(post.getLabels());
                Iterator<Label> iterator = linkedLabels.iterator();
                Label extraLabel = iterator.next();
                removeLabel.setLong(1, post.getId());
                removeLabel.setLong(2, extraLabel.getId());
                removeLabel.executeUpdate();
            }
            connection.commit();
            updatedPost = getById(post.getId());
        } catch (SQLException ex) {
            System.out.println("Cannot save data to DB.");
        }

        return updatedPost;
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(DELETE_POST_BY_ID)) {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            prepStatement.setTimestamp(1, timestamp);
            prepStatement.setString(2, PostStatus.DELETED.name());
            prepStatement.setLong(3, id);
            prepStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Cannot save data to DB.");
        }
    }

    private List<Post> getAllPostsInternal() {
        List<Post> posts = new ArrayList<>();

        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(GET_ALL_NOT_DELETED_POSTS)) {
            ResultSet resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                Post post = new Post(
                        resultSet.getLong("id"),
                        resultSet.getString("content"),
                        resultSet.getTimestamp("created"),
                        resultSet.getTimestamp("updated"),
                        PostStatus.valueOf(resultSet.getString("post_status"))
                );
                Set<Label> labels = getLabelsRelatedToPost(post);
                post.setLabels(labels);
                posts.add(post);
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read data from DB.");
        }

        if (posts.isEmpty()) {
            return Collections.emptyList();
        } else {
            return posts;
        }
    }

    private Set<Label> getLabelsRelatedToPost(Post post) {
        Set<Label> labels = new HashSet<>();

        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(GET_POST_LABELS)) {
            prepStatement.setLong(1, post.getId());
            ResultSet resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("name") != null) {
                    labels.add(new Label(resultSet.getLong("id"), resultSet.getString("name")));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read data from DB.");
        }

        return labels;
    }
}