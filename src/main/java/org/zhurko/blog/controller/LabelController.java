package org.zhurko.blog.controller;

import org.zhurko.blog.model.Label;
import org.zhurko.blog.repository.jdbc.JdbcLabelRepositoryImpl;
import org.zhurko.blog.service.LabelService;

import java.util.List;

public class LabelController {

    private static final String BLANK_INPUT_ERROR_MESSAGE = "The label name cannot be zero-length or contain only spaces.";

    private final LabelService labelService = new LabelService(new JdbcLabelRepositoryImpl());

    public Label save(String input) {
        if (input.isBlank()) {
            System.out.println(BLANK_INPUT_ERROR_MESSAGE);
            return null;
        }

        return labelService.save(new Label(input));
    }

    public Label findLabelByName(String name) {
        return labelService.findByName(name);
    }

    public List<Label> getAll() {
        return labelService.getAll();
    }

    public void deleteLabelById(Long id) {
        labelService.deleteById(id);
    }

    public Label updateLabel(String existentName, String newName) {
        if (newName.isBlank()) {
            System.out.println(BLANK_INPUT_ERROR_MESSAGE);
            return null;
        }

        Label label = labelService.findByName(existentName);
        label.setName(newName);
        return labelService.update(label);
    }

    public Label getLabelById(Long id) {
        return labelService.getById(id);
    }
}
