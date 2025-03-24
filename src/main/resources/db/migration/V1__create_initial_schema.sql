CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    user_type SMALLINT
);


CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    street VARCHAR(100),
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    country VARCHAR(100),
    user_id UUID UNIQUE,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users(id)
);