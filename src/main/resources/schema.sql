CREATE TABLE IF NOT EXISTS movies(
    id SERIAL PRIMARY KEY,
    title VARCHAR(128) NOT NULL,
    genres VARCHAR(128) NOT NULL,
    plot TEXT NOT NULL
);
