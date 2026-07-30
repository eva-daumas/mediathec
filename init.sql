-- ============================================================
-- CRÉATION ET SÉLECTION DE LA BASE DE DONNÉES
-- ============================================================
CREATE DATABASE IF NOT EXISTS mediathec_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE mediathec_db;


-- Suppression des tables si elles existent (pour un reset propre)
DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS game;
DROP TABLE IF EXISTS book;


-- ============================================================
-- TABLE : member (members-service)
-- ============================================================
CREATE TABLE IF NOT EXISTS member (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    username    VARCHAR(255)    NOT NULL UNIQUE,
    email       VARCHAR(255)    NOT NULL UNIQUE,
    password    VARCHAR(255),
    role        VARCHAR(255)    DEFAULT 'USER',
    created_at  DATETIME,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ============================================================
-- TABLE : book (book-service)
-- ============================================================
CREATE TABLE IF NOT EXISTS book (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    title       VARCHAR(255)    NOT NULL,
    author      VARCHAR(255),
    category    VARCHAR(255),
    description VARCHAR(255),
    cover_image VARCHAR(255),
    available   BIT(1)          DEFAULT 1,
    created_at  DATETIME,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ============================================================
-- TABLE : game (game-service)
-- ============================================================
CREATE TABLE IF NOT EXISTS game (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    title       VARCHAR(255)    NOT NULL,
    author      VARCHAR(255),
    category    VARCHAR(255),
    description VARCHAR(255),
    cover_image VARCHAR(255),
    available   BIT(1)          DEFAULT 1,
    created_at  DATETIME,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ============================================================
-- TABLE : movie (movie-service)
-- ============================================================
CREATE TABLE IF NOT EXISTS movie (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    title       VARCHAR(255)    NOT NULL,
    author      VARCHAR(255),
    category    VARCHAR(255),
    description VARCHAR(255),
    cover_image VARCHAR(255),
    available   BIT(1)          DEFAULT 1,
    created_at  DATETIME,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ============================================================
-- TABLE : loans (loan-service)
-- ============================================================
CREATE TABLE IF NOT EXISTS loans (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    member_id   BIGINT          NOT NULL,
    book_id     BIGINT,
    game_id     BIGINT,
    movie_id    BIGINT,
    loan_date   DATETIME,
    return_date DATETIME,
    status      VARCHAR(255)    DEFAULT 'BORROWED',
    PRIMARY KEY (id)
    FOREIGN KEY (member_id)
    REFERENCES member(id)
    FOREIGN KEY (book_id)
    REFERENCES book(id)
    FOREIGN KEY (game_id)
    REFERENCES game(id)
    FOREIGN KEY (movie_id)
    REFERENCES movie(id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;