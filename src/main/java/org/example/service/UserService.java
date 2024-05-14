package org.example.service;

import org.example.enumators.UserRole;
import org.example.exception.DataNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;


public class UserService extends BaseService<User, UserRepository>{

    public UserService(UserRepository repository) {
        super(repository);
    }

    public User findByChatId(Long chatId) {
        Optional<User> userOptional = repository.findByChatId(chatId);

        return userOptional.orElseThrow(() -> {
            return new DataNotFoundException("user with this chat {} is not found");
        });
    }

    public void update(User updated) {
        ArrayList<User> all = repository.getAll();
        Integer i = 0;
        for (User user : all) {
            if (Objects.equals(user.getId(), updated.getId())) {
                all.set(i, updated);
                break;
            }
            i++;
        }
        repository.writeData(all);
    }

    public void updateRole(User updated, UserRole role) {
        ArrayList<User> all = repository.getAll();
        for (User user : all) {
            if (Objects.equals(user.getId(), updated.getId())) {
                user.setRole(role);
                break;
            }
        }
        repository.writeData(all);
    }
}
