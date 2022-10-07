package org.zhurko.blog.repository.sql;

import org.zhurko.blog.model.Label;
import org.zhurko.blog.repository.LabelRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlLabelRepositoryImpl implements LabelRepository {

    private static final String SELECT_ALL_LABELS = "SELECT * FROM labels";
    private static final String INSERT_LABEL = "INSERT INTO labels (name) VALUES (?)";
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
        try(Connection connection = ConnectionBuilder.getConnection();
            PreparedStatement prepStatement = connection.prepareStatement(INSERT_LABEL,
                    Statement.RETURN_GENERATED_KEYS)) {
            prepStatement.setString(1, label.getName());
            prepStatement.executeUpdate();

            ResultSet resultSet = prepStatement.getGeneratedKeys();
            while (resultSet.next()) {
                savedLabel = new Label(resultSet.getLong(1), label.getName());
            }
        } catch (SQLException ex) {
            System.out.printf("Cannot save '%s' to DB.%n", label.getName());
        }

        return savedLabel;
    }


    @Override
    public Label getById(Long id) {
        Label label = null;
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(GET_LABEL_BY_ID)) {
            prepStatement.setLong(1, id);
            ResultSet resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                label = new Label(resultSet.getLong("id"), resultSet.getString("name"));
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

        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(UPDATE_LABEL)) {
            prepStatement.setString(1, label.getName());
            prepStatement.setLong(2, label.getId());
            int updateResult = prepStatement.executeUpdate();

            // todo: так можем проверять то, что запись проапдейтилась?
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
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(DELETE_LABEL_BY_ID)) {
            prepStatement.setLong(1, id);
            prepStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Cannot save data to DB.");
        }
    }

    private List<Label> getAllLabelsInternal() {
        List<Label> labels = new ArrayList<>();

        try (Connection connection = ConnectionBuilder.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_LABELS);
            while (resultSet.next()) {
                Label label = new Label(
                        resultSet.getLong("id"),
                        resultSet.getString("name")
                );
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
        try (Connection connection = ConnectionBuilder.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(FIND_LABEL_BY_NAME)) {
            prepStatement.setString(1, name);
            ResultSet resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                label = new Label(resultSet.getLong("id"), resultSet.getString("name"));
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read data from DB.");
        }

        return label;
    }
}
