package ru.job4j.store;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.User;

import java.util.List;
import java.util.stream.Collectors;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();

    default List<User> findByClientId(Long clientId) {
        return findAll().stream()
                .filter(user -> user.getClientId() == clientId)
                .collect(Collectors.toList());
    }

}