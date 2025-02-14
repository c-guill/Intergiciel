package fr.miaou.messagerie.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class User {
    @Id
    private Long idUser;
    private String nom;

    public User() {}

    public User(Long idUser, String nom) {
        this.idUser = idUser;
        this.nom = nom;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", nom='" + nom + '\'' +
                '}';
    }
}
