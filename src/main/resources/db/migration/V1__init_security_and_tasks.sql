-- Tabela użytkowników (tak jak było)
CREATE TABLE users (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255),
                       role VARCHAR(50) DEFAULT 'ROLE_USER'
);

-- NOWOŚĆ: Tabela zadań (spełnia wymóg klucza obcego user_id)
CREATE TABLE tasks (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       title VARCHAR(100) NOT NULL,
                       description TEXT,
                       is_completed BOOLEAN DEFAULT 0,
                       user_id INTEGER NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES users(id)
);