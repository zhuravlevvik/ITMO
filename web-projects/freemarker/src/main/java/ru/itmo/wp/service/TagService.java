package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Tag;
import ru.itmo.wp.repository.TagRepository;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag findByName(String name) {
        return name == null ? null : this.tagRepository.findByName(name);
    }

    public void save(Tag tag) {
        this.tagRepository.save(tag);
    }
}
