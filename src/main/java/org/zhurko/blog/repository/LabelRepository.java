package org.zhurko.blog.repository;

import org.zhurko.blog.model.Label;

public interface LabelRepository extends GenericRepository<Label, Long> {
    Label findByName(String name);
}
