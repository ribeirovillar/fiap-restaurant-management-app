-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    last_modified_date TIMESTAMP NOT NULL,
    user_type SMALLINT
);

-- Create addresses table
CREATE TABLE IF NOT EXISTS addresses (
    id UUID PRIMARY KEY,
    street VARCHAR(100),
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    country VARCHAR(100),
    user_id UUID UNIQUE,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create restaurants table
CREATE TABLE IF NOT EXISTS restaurants (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address_id UUID,
    cuisine_type VARCHAR(50) NOT NULL,
    owner_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE CASCADE,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create business_hours table
CREATE TABLE IF NOT EXISTS business_hours (
    id UUID PRIMARY KEY,
    restaurant_id UUID NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    opening_time TIME,
    closing_time TIME,
    is_closed BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

-- Create menu_items table
CREATE TABLE IF NOT EXISTS menu_items (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    available_for_takeout BOOLEAN NOT NULL,
    photo_path VARCHAR(255) NOT NULL,
    restaurant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_menu_items_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);