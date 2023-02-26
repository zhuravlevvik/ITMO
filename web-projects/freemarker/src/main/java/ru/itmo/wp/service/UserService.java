package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.Role;
import ru.itmo.wp.domain.Tag;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.form.PostCredentials;
import ru.itmo.wp.form.UserCredentials;
import ru.itmo.wp.repository.RoleRepository;
import ru.itmo.wp.repository.UserRepository;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final TagService tagService;

    /**
     * @noinspection FieldCanBeLocal, unused
     */
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, TagService tagService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.tagService = tagService;

        this.roleRepository = roleRepository;
        for (Role.Name name : Role.Name.values()) {
            if (!roleRepository.existsByName(name)) {
                roleRepository.save(new Role(name));
            }
        }
    }

    public User register(UserCredentials userCredentials) {
        User user = new User();
        user.setLogin(userCredentials.getLogin());
        userRepository.save(user);
        userRepository.updatePasswordSha(user.getId(), userCredentials.getLogin(), userCredentials.getPassword());
        return user;
    }

    public boolean isLoginVacant(String login) {
        return userRepository.countByLogin(login) == 0;
    }

    public User findByLoginAndPassword(String login, String password) {
        return login == null || password == null ? null : userRepository.findByLoginAndPassword(login, password);
    }

    public User findById(Long id) {
        return id == null ? null : userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAllByOrderByIdDesc();
    }

    public void writePost(User user, PostCredentials postCredentials) {
        Post post = new Post();
        post.setText(postCredentials.getText());
        post.setTitle(postCredentials.getTitle());
        post.setTags(parseTags(postCredentials.getTags()));
        user.addPost(post);
        post.setUser(user);
        userRepository.save(user);
    }

    private Set<Tag> parseTags(String tags) {
        Set<Tag> result = new HashSet<>();
        String[] splitTags = tags.split("\\s");
        for (String tagName : splitTags) {
            if (tagName.length() == 0) {
                continue;
            }
            Tag tag = tagService.findByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tagService.save(tag);
                result.add(tagService.findByName(tagName));
            } else {
                result.add(tag);
            }
        }
        return result;
    }
}
