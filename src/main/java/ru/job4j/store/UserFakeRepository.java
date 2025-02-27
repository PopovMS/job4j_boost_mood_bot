package ru.job4j.store;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.model.User;
import ru.job4j.store.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserFakeRepository extends CrudRepositoryFake<User, Long> implements UserRepository {

    public List<User> findAll() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public List<User> findByClientId(Long clientId) {
        return memory.values().stream()
                .filter(user -> user.getClientId() == clientId)
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        User u = super.save(user);
        return u;
    }
}