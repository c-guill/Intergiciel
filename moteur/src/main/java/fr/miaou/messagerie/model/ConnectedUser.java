package fr.miaou.messagerie.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "connected_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConnectedUser {

    @Id
    private Long userId;

    @Column(nullable = false)
    private Timestamp connectedAt;

    @Column
    private String instanceId;
}
