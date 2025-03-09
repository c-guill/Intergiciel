package fr.miaou.messagerie.service;

import fr.miaou.messagerie.model.Contact;
import fr.miaou.messagerie.model.Message;
import fr.miaou.messagerie.model.User;
import fr.miaou.messagerie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Contact> getAllContacts(long userId) {
        List<User> users = userRepository.findAll();
        List<Contact> contacts = new ArrayList<>();

        for (User user : users) {
            if (user.getIdUser() == userId) continue;
            Optional<Message> message = this.messageService.getLastMessageByDiscussion(userId, user.getIdUser());
            Contact contact = Contact.builder()
                    .id(user.getIdUser())
                    .username(user.getNom()).build();
            message.ifPresent(value -> contact.setLastMessage(value.getContenu()));
            message.ifPresent(value -> contact.setDate(value.getDate()));
            contacts.add(contact);
        }

        return contacts;

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

    public Contact getcontactByUsername(String username) {
        Optional<User> user = this.userRepository.getUserByNom(username);
        return user.map(Contact::new).orElseGet(() -> new Contact(this.createUser(new User(null, username))));

    }
}
