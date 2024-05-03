package vn.edu.tdtu.springcommerce.service;

import vn.edu.tdtu.springcommerce.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    List<User> getAllUser();
    User updateUser(User user);
    void removeUserById(Long id);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByName(String name);
    Optional<User> getUserByEmail(String email);
    User saveUser(User user);
}
