package fr.miaou.messagerie.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.miaou.messagerie.model.ConnectedUser;
import fr.miaou.messagerie.model.Contact;
import fr.miaou.messagerie.model.Message;
import fr.miaou.messagerie.model.User;
import fr.miaou.messagerie.repository.ConnectedUserRepository;
import fr.miaou.messagerie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private ConnectedUserRepository connectedUserRepository;

    // Identifiant unique de cette instance (utile pour le debug multi-replica)
    private final String instanceId = UUID.randomUUID().toString().substring(0, 8);

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Contact> getAllContacts(long userId) {
        List<User> users = userRepository.findAll();
        List<Contact> contacts = new ArrayList<>();
        List<Long> connectedUserIds = connectedUserRepository.findAll()
                .stream()
                .map(ConnectedUser::getUserId)
                .toList();

        for (User user : users) {
            if (user.getIdUser() == userId || !connectedUserIds.contains(user.getIdUser())) continue;
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

    public Contact getcontactByUsername(String username) {
        Optional<User> user = this.userRepository.getUserByNom(username);

        return user.map(Contact::new).orElseGet(() -> new Contact(this.createUser(new User(null, username))));
    }

    @Transactional
    public void disconnectUser(Long id) {
        this.connectedUserRepository.deleteById(id);
        this.producerService.manageUser(false, id.toString());
    }

    @Transactional
    public void connectUser(Long id) {
        try {
            Optional<User> user = this.getUserById(id);
            if (user.isPresent() && !this.connectedUserRepository.existsById(id)) {
                Contact contact = getcontactByUsername(user.get().getNom());
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(contact);

                // Sauvegarder en base de données (partagé entre tous les réplicas)
                ConnectedUser connectedUser = ConnectedUser.builder()
                        .userId(id)
                        .connectedAt(new Timestamp(System.currentTimeMillis()))
                        .instanceId(this.instanceId)
                        .build();
                this.connectedUserRepository.save(connectedUser);

                this.producerService.manageUser(true, json);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

