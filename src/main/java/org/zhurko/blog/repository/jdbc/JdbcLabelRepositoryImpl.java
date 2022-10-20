package org.zhurko.blog.repository.jdbc;

import org.zhurko.blog.model.Label;
import org.zhurko.blog.repository.LabelRepository;
import org.zhurko.blog.util.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.zhurko.blog.util.ResultSetUtils.mapResultSetToLabel;

public class JdbcLabelRepositoryImpl implements LabelRepository {

    private static final String SELECT_ALL_LABELS = "SELECT * FROM labels ORDER BY id ASC";
    private static final String SAVE_LABEL = "INSERT INTO labels (name) VALUES (?)";
    private static final String FIND_LABEL_BY_NAME = "SELECT * FROM labels WHERE name = ?";
    private static final String GET_LABEL_BY_ID = "SELECT * FROM labels WHERE id = ?";
    private static final String DELETE_LABEL_BY_ID = "DELETE FROM labels WHERE id = ?";
    private static final String UPDATE_LABEL = "UPDATE labels SET name = ? WHERE id = ?";


    @Override
    public Label save(Label label) {
        if (findByName(label.getName()) != null) {
            return label;
        }

        Label savedLabel = null;

        try (PreparedStatement prepStatement = JdbcUtils.getPreparedStatementWithGeneratedKeys(SAVE_LABEL)) {
            prepStatement.setString(1, label.getName());
            prepStatement.executeUpdate();

            ResultSet generatedKeys = prepStatement.getGeneratedKeys();
            while (generatedKeys.next()) {
                savedLabel = getById(generatedKeys.getLong(1));
            }
        } catch (SQLException ex) {
            System.out.printf("Cannot save '%s' to DB.%n", label.getName());
        }

        return savedLabel;
    }


    @Override
    public Label getById(Long id) {
        Label label = null;

        try (PreparedStatement prepStatement = JdbcUtils.getPreparedStatement(GET_LABEL_BY_ID)) {
            prepStatement.setLong(1, id);
            ResultSet resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                label = mapResultSetToLabel(resultSet);
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read data from DB.");
        }

        return label;
    }


    @Override
    public List<Label> getAll() {
        return getAllLabelsInternal();
    }

    @Override
    public Label update(Label label) {
        Label updatedLabel = null;

        try (PreparedStatement prepStatement = JdbcUtils.getPreparedStatement(UPDATE_LABEL)) {
            prepStatement.setString(1, label.getName());
            prepStatement.setLong(2, label.getId());
            int updateResult = prepStatement.executeUpdate();

            if (updateResult == 1) {
                updatedLabel = new Label(label.getId(), label.getName());
            }
        } catch (SQLException ex) {
            System.out.println("Cannot save data to DB.");
        }

        return updatedLabel;
    }

    @Override
    public void deleteById(Long id) {
        try (PreparedStatement prepStatement = JdbcUtils.getPreparedStatement(DELETE_LABEL_BY_ID)) {
            prepStatement.setLong(1, id);
            prepStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Cannot save data to DB.");
        }
    }

    private List<Label> getAllLabelsInternal() {
        List<Label> labels = new ArrayList<>();

        try (PreparedStatement prepStatement = JdbcUtils.getPreparedStatement(SELECT_ALL_LABELS)) {
            ResultSet resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                Label label = mapResultSetToLabel(resultSet);
                labels.add(label);
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read data from DB.");
        }

        if (labels.isEmpty()) {
            return Collections.emptyList();
        } else {
            return labels;
        }
    }

    @Override
    public Label findByName(String name) {
        Label label = null;

        try (PreparedStatement prepStatement = JdbcUtils.getPreparedStatement(FIND_LABEL_BY_NAME)) {
            prepStatement.setString(1, name);
            ResultSet resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                label = mapResultSetToLabel(resultSet);
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read data from DB.");
        }

        return label;
    }
}
