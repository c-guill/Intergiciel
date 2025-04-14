# Projet  Intergiciel + kubernetes : Messagerie Miaou


## Equipe :

Guillier Camille 22004270

Mekouar Doha 22301456

Gadiaga Sokhna Laye 22106253

Guarim Raphael 22304361


## Description
Ce projet est une application de messagerie en temps réel basée sur Spring Boot, avec un frontend en Thymeleaf et une base de données PostgreSQL. Il utilise Apache Kafka pour assurer une communication asynchrone et scalable entre les utilisateurs

Actuellement docker lance 2 le frontend sur 2 ports (8081 et 8082). Pour tester l'envoie des messages entre 2 chat, il faut lancer les 2 instances sur 2 navigateurs diffrents ou en navigation privée (en raison des cookies)

Pour se connecter en mode privé / broadcast, il ne faut rentrer aucun utilisateur dans le login sur la page d'accueil.

Ce tp concerne le TP2 sous kubernetes, tous les fichiers sont dans le répertoire "kubernetes". L'étape 1 du TP se trouve dans la branche "P1TP2"

## Prérequis
- Docker Desktop

## Installation et Configuration

### 1. Cloner le projet
```bash
git clone https://github.com/c-guill/Intergiciel.git
```

### 2. Lancer Docker

```bash
docker-compose up -d
```
