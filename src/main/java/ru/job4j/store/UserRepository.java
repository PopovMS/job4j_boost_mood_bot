package ru.job4j.store;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();

    Optional<User> findByClientId(Long id);

}