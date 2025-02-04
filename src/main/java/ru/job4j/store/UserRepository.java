package ru.job4j.store;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();

    User findByClientId(Long clientId);

    public Object save(User user);
}