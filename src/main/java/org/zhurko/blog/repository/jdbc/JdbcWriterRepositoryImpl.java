package org.zhurko.blog.repository.jdbc;

import org.zhurko.blog.model.Post;
import org.zhurko.blog.model.Writer;
import org.zhurko.blog.repository.WriterRepository;
import org.zhurko.blog.util.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.zhurko.blog.util.ResultSetUtils.mapResultSetToPost;
import static org.zhurko.blog.util.ResultSetUtils.mapResultSetToWriter;

public class JdbcWriterRepositoryImpl implements WriterRepository {

    private static final String SAVE_WRITER = "INSERT INTO writers (first_name, last_name) VALUES (?, ?)";
    private static final String GET_ALL_WRITERS = "SELECT * FROM writers ORDER BY id ASC";
    private static final String GET_POSTS_CREATED_BY_WRITER = "SELECT * FROM posts " +
            "WHERE writer_id = ? AND post_status NOT LIKE 'DELETED'";
    private static final String GET_WRITER_BY_ID = "SELECT * FROM writers WHERE id = ?";
    private static final String UPDATE_WRITER = "UPDATE writers SET first_name = ?, last_name = ? WHERE id = ?";
    private static final String ADD_POST_TO_WRITER = "UPDATE posts SET writer_id = ? WHERE id = ?";
    private static final String REMOVE_POST_FROM_WRITER = "UPDATE posts SET writer_id = NULL WHERE id = ?";
    private static final String DELETE_WRITER_BY_ID = "DELETE FROM writers WHERE id = ?";

    @Override
    public Writer getById(Long id) {
        Writer writer = null;

        try (PreparedStatement prepStatement = JdbcUtils.getPreparedStatement(GET_WRITER_BY_ID)) {
            prepStatement.setLong(1, id);
            ResultSet resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                writer = mapResultSetToWriter(resultSet);
                Set<Post> posts = getPostsCreatedByWriter(writer);
                writer.setPosts(posts);
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read data from DB.");
        }

        return writer;
    }

    @Override
    public List<Writer> getAll() {
        return getAllWritersInternal();
    }

    @Override
    public Writer save(Writer writer) {
        Writer savedWriter = null;

        try (PreparedStatement prepStatement = JdbcUtils.getPreparedStatementWithGeneratedKeys(SAVE_WRITER)) {
            prepStatement.setString(1, writer.getFirstName());
            prepStatement.setString(2, writer.getLastName());
            prepStatement.executeUpdate();

            ResultSet generatedKeys = prepStatement.getGeneratedKeys();
            while (generatedKeys.next()) {
                savedWriter = getById(generatedKeys.getLong(1));
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read data from DB.");
        }

        return savedWriter;
    }

    @Override
    public Writer update(Writer writer) {
        Writer updatedWriter = null;

        Set<Post> existentPostsOfWriters = getPostsCreatedByWriter(writer);

        try (PreparedStatement updateWriter = JdbcUtils.getPreparedStatement(UPDATE_WRITER);
             PreparedStatement addPost = JdbcUtils.getPreparedStatement(ADD_POST_TO_WRITER);
             PreparedStatement removePost = JdbcUtils.getPreparedStatement(REMOVE_POST_FROM_WRITER)) {

            JdbcUtils.getConnection().setAutoCommit(false);

            updateWriter.setString(1, writer.getFirstName());
            updateWriter.setString(2, writer.getLastName());
            updateWriter.setLong(3, writer.getId());
            updateWriter.executeUpdate();

            if (writer.getPosts().size() > existentPostsOfWriters.size()) {
                writer.getPosts().removeAll(existentPostsOfWriters);
                Iterator<Post> iterator = writer.getPosts().iterator();
                Post newPost = iterator.next();
                addPost.setLong(1, writer.getId());
                addPost.setLong(2, newPost.getId());
                addPost.executeUpdate();
            } else if (writer.getPosts().size() < existentPostsOfWriters.size()) {
                existentPostsOfWriters.removeAll(writer.getPosts());
                Post extraPost = existentPostsOfWriters.iterator().next();
                removePost.setLong(1, extraPost.getId());
                removePost.executeUpdate();
            }
            JdbcUtils.getConnection().commit();
            updatedWriter = getById(writer.getId());
        } catch (SQLException ex) {
            System.out.println("Cannot save data to DB.");
        }

        return updatedWriter;
    }

    @Override
    public void deleteById(Long id) {
        try (PreparedStatement prepStatement = JdbcUtils.getPreparedStatement(DELETE_WRITER_BY_ID)) {
            prepStatement.setLong(1, id);
            prepStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Cannot save data to DB.");
        }
    }

    private List<Writer> getAllWritersInternal() {
        List<Writer> writers = new ArrayList<>();

        try (PreparedStatement prepStatement = JdbcUtils.getPreparedStatement(GET_ALL_WRITERS)) {
            ResultSet resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                Writer writer = mapResultSetToWriter(resultSet);
                Set<Post> posts = getPostsCreatedByWriter(writer);
                writer.setPosts(posts);
                writers.add(writer);
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read data from DB.");
        }

        if (writers.isEmpty()) {
            return Collections.emptyList();
        } else {
            return writers;
        }
    }

    private Set<Post> getPostsCreatedByWriter(Writer writer) {
        Set<Post> posts = new HashSet<>();

        try (PreparedStatement prepStatement = JdbcUtils.getPreparedStatement(GET_POSTS_CREATED_BY_WRITER)) {
            prepStatement.setLong(1, writer.getId());
            ResultSet resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                Post post = mapResultSetToPost(resultSet);
                posts.add(post);
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read data from DB.");
        }

        return posts;
    }
}
