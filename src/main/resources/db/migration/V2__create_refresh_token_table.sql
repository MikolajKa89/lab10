CREATE TABLE refresh_tokens (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                token VARCHAR(255) NOT NULL UNIQUE,
                                expiry_date DATETIME NOT NULL,
                                user_id INTEGER NOT NULL,
                                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);