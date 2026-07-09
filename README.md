# Mediathec - Application de Gestion de Médiathèque

## Description

Mediathec est une application web complète de gestion de médiathèque. Elle permet aux membres de consulter un catalogue de livres, jeux et films, d'emprunter et de retourner des médias, et de gérer leur profil. Les administrateurs disposent d'une interface dédiée pour gérer les membres, les médias et les emprunts.

L'application est construite avec une architecture microservices, conteneurisée avec Docker, et utilise Spring Boot, Spring Cloud, Thymeleaf, MySQL et Consul.

## Fonctionnalités Principales

*   **Gestion des médias :** Ajout, modification, suppression de livres, jeux et films.
*   **Gestion des membres :** Inscription, connexion, modification du profil.
*   **Gestion des emprunts :** Emprunter et retourner des médias, visualiser son historique d'emprunts.
*   **Administration :** Tableau de bord centralisé avec statistiques, gestion des membres, des emprunts, et du catalogue.
*   **Sécurité :** Authentification des utilisateurs, chiffrement des mots de passe (BCrypt), protection des routes admin.

## Technologies Utilisées

*   **Back-end :** Java 17, Spring Boot 3, Spring Data JPA, Spring Security, Spring Cloud
*   **Front-end :** Thymeleaf, HTML5, CSS3, JavaScript Vanilla, Font Awesome
*   **Base de données :** MySQL 8.0
*   **Service Discovery :** HashiCorp Consul
*   **Conteneurisation :** Docker, Docker Compose

## Structure du Projet

Le projet est structuré en plusieurs microservices :

*   **[`web-app`](./web-app)** : Le point d'entrée de l'application. Gère l'interface utilisateur, la sécurité et agit comme un agrégateur de données en communiquant avec les autres microservices via des clients Feign.
*   **[`members-service`](./members-service)** : Gère les opérations CRUD sur les membres.
*   **[`book-service`](./book-service)** : Gère le catalogue des livres.
*   **[`game-service`](./game-service)** : Gère le catalogue des jeux.
*   **[`movie-service`](./movie-service)** : Gère le catalogue des films.
*   **[`loan-service`](./loan-service)** : Gère les emprunts (création, retour, historique).

## Arborescence du Répertoire
mediathec/
├── common-config.yml # Configuration partagée pour les microservices
├── docker-compose.yml # Fichier d'orchestration Docker
├── README.md # Ce fichier
├── book-service/ # Microservice pour les livres
│ ├── src/main/java/com/mediathec/bookService/
│ │ ├── BookServiceApplication.java
│ │ ├── controller/
│ │ ├── entity/
│ │ ├── repository/
│ │ └── service/
│ ├── src/main/resources/application.yml
│ └── pom.xml
├── game-service/ # Microservice pour les jeux
├── loan-service/ # Microservice pour les emprunts
│ ├── src/main/java/com/mediathec/loanService/
│ │ ├── client/ # Clients Feign pour appeler book, game, member, movie
│ │ ├── controller/
│ │ ├── dto/
│ │ ├── entity/
│ │ ├── model/ # Modèles utilisés par les clients Feign
│ │ ├── repository/
│ │ └── service/
│ └── src/main/resources/application.yml
├── members-service/ # Microservice pour les membres
├── movie-service/ # Microservice pour les films
└── web-app/ # Front-end et gateway
├── src/main/java/com/mediathec/webApp/
│ ├── WebAppApplication.java
│ ├── config/ # SecurityConfig.java
│ ├── controller/ # Contrôleurs MVC
│ ├── dto/ # Data Transfer Objects
│ ├── service/ # Services façade et clients Feign
│ └── service/client/ # Interfaces FeignClient
├── src/main/resources/
│ ├── application.yml
│ ├── static/
│ │ ├── css/
│ │ └── js/
│ └── templates/ # Pages HTML Thymeleaf
└── pom.xml


## Mise en Place

### Prérequis

*   Docker et Docker Compose
*   Java 17 (pour le développement local)

### Installation et Lancement

1.  **Cloner le dépôt :**
    ```bash
    git clone https://github.com/eva-daumas/mediathec.git
    cd mediathec

2. Construire les images Docker :
   À la racine du projet, exécutez la commande suivante pour builder tous les microservices :
   docker-compose build
   (Assurez-vous que les images evadaumas/... sont accessibles ou remplacez-les par vos propres tags dans le docker-compose.yml)
3. Démarrer les conteneurs :
   docker-compose up

4.Accéder à l'application :
Une fois tous les conteneurs démarrés, ouvrez votre navigateur et allez sur :
http://localhost:9011

Développement Local
Pour exécuter les services un par un en local (sans Docker), vous devez avoir une instance MySQL et Consul en cours d'exécution sur votre machine.
Vous pouvez lancer chaque service via son pom.xml avec la commande mvn spring-boot:run.
N'oubliez pas de configurer les fichiers application.yml avec les bonnes adresses localhost.

Base de données
Port exposé : 3308 (pour éviter les conflits avec une instance MySQL locale)

Nom de la base : mediathec_db

Utilisateur : root

Mot de passe : root

Le schéma de la base de données sera automatiquement mis à jour par Hibernate (via la propriété spring.jpa.hibernate.ddl-auto=update).


Auteurs
Eva Daumas

Licence
Ce projet est réalisé dans le cadre de l'examen DWWM 2026.




