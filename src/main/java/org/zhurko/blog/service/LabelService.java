package org.zhurko.blog.service;

import org.zhurko.blog.model.Label;
import org.zhurko.blog.repository.LabelRepository;

import java.util.List;

public class LabelService {

    private final LabelRepository labelRepo;

    public LabelService(LabelRepository labelRepo) {
        this.labelRepo = labelRepo;
    }

    public List<Label> getAll() {
        return labelRepo.getAll();
    }

    public Label save(Label label) {
        return labelRepo.save(label);
    }

    public void deleteById(Long id) {
        labelRepo.deleteById(id);
    }

    public Label findByName(String name) {
        return labelRepo.findByName(name);
    }

    public Label update(Label label) {
        return labelRepo.update(label);
    }

    public Label getById(Long id) {
        return labelRepo.getById(id);
    }
}
