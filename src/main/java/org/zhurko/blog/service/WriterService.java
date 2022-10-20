package org.zhurko.blog.service;

import org.zhurko.blog.model.Writer;
import org.zhurko.blog.repository.WriterRepository;

import java.util.List;

public class WriterService {

    private final WriterRepository writerRepo;

    public WriterService(WriterRepository writerRepo) {
        this.writerRepo = writerRepo;
    }

    public List<Writer> getAll() {
        return writerRepo.getAll();
    }

    public Writer getById(Long id) {
        return writerRepo.getById(id);
    }

    public Writer save(Writer writer) {
        return writerRepo.save(writer);
    }

    public void deleteById(Long id) {
        writerRepo.deleteById(id);
    }

    public Writer update(Writer writer) {
        return writerRepo.update(writer);
    }
}
