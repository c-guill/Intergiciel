package fr.miaou.messagerie.service;

import fr.miaou.messagerie.model.User;
import fr.miaou.messagerie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User userDetails) {
        return userRepository.findById(userDetails.getIdUser()).map(user -> {
            user.setNom(userDetails.getNom());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
