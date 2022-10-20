package org.zhurko.blog.util;

import org.zhurko.blog.model.Label;
import org.zhurko.blog.model.Post;
import org.zhurko.blog.model.PostStatus;
import org.zhurko.blog.model.Writer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetUtils {

    private ResultSetUtils() {
    }

    public static Label mapResultSetToLabel(ResultSet rs) throws SQLException {
        return new Label(rs.getLong("id"), rs.getString("name"));
    }

    public static Post mapResultSetToPost(ResultSet resultSet) throws SQLException {
        return new Post(
                resultSet.getLong("id"),
                resultSet.getString("content"),
                resultSet.getTimestamp("created"),
                resultSet.getTimestamp("updated"),
                PostStatus.valueOf(resultSet.getString("post_status")));
    }

    public static Writer mapResultSetToWriter(ResultSet resultSet) throws SQLException {
        return new Writer(
                resultSet.getLong("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name")
        );
    }
}
