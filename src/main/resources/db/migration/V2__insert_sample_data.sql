-- Insert user types
INSERT INTO user_types (id, name) VALUES
    ('77777777-7777-7777-7777-777777777777', 'CUSTOMER'),
    ('88888888-8888-8888-8888-888888888888', 'OWNER');

-- Insert sample users (owners and customers)
INSERT INTO users (id, name, email, login, password, last_modified_date, user_type_id) VALUES
    ('11111111-1111-1111-1111-111111111111', 'João Silva', 'joao.silva@example.com', 'joaosilva', '$2a$10$xVxWQLm0y3u3QUv9TO9kFe9vxmVX4nJiGpO2m2QrZ5x0EQM/WrY7y', NOW(), '88888888-8888-8888-8888-888888888888'), -- OWNER
    ('22222222-2222-2222-2222-222222222222', 'Maria Souza', 'maria.souza@example.com', 'mariasouza', '$2a$10$xVxWQLm0y3u3QUv9TO9kFe9vxmVX4nJiGpO2m2QrZ5x0EQM/WrY7y', NOW(), '88888888-8888-8888-8888-888888888888'), -- OWNER
    ('33333333-3333-3333-3333-333333333333', 'Pedro Oliveira', 'pedro@example.com', 'pedrooliveira', '$2a$10$xVxWQLm0y3u3QUv9TO9kFe9vxmVX4nJiGpO2m2QrZ5x0EQM/WrY7y', NOW(), '77777777-7777-7777-7777-777777777777'), -- CUSTOMER
    ('44444444-4444-4444-4444-444444444444', 'Ana Santos', 'ana@example.com', 'anasantos', '$2a$10$xVxWQLm0y3u3QUv9TO9kFe9vxmVX4nJiGpO2m2QrZ5x0EQM/WrY7y', NOW(), '77777777-7777-7777-7777-777777777777'); -- CUSTOMER

-- Insert addresses for users
INSERT INTO addresses (id, street, city, state, zip_code, country, user_id) VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Av. Paulista, 1000', 'São Paulo', 'SP', '01310-100', 'Brasil', '11111111-1111-1111-1111-111111111111'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Rua Augusta, 500', 'São Paulo', 'SP', '01304-000', 'Brasil', '22222222-2222-2222-2222-222222222222'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Av. Rio Branco, 156', 'Rio de Janeiro', 'RJ', '20040-901', 'Brasil', '33333333-3333-3333-3333-333333333333'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Rua das Flores, 100', 'Curitiba', 'PR', '80020-190', 'Brasil', '44444444-4444-4444-4444-444444444444');

-- Insert restaurant addresses
INSERT INTO addresses (id, street, city, state, zip_code, country) VALUES
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Av. Brigadeiro Faria Lima, 1500', 'São Paulo', 'SP', '01451-001', 'Brasil'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Av. Nove de Julho, 3000', 'São Paulo', 'SP', '01407-000', 'Brasil');

-- Insert restaurants
INSERT INTO restaurants (id, name, address_id, cuisine_type, owner_id, created_at, updated_at) VALUES
    ('55555555-5555-5555-5555-555555555555', 'Cantina Italiana', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'ITALIAN', '11111111-1111-1111-1111-111111111111', NOW(), NOW()),
    ('66666666-6666-6666-6666-666666666666', 'Sushi Express', 'ffffffff-ffff-ffff-ffff-ffffffffffff', 'JAPANESE', '22222222-2222-2222-2222-222222222222', NOW(), NOW());

-- Insert business hours for Cantina Italiana
INSERT INTO business_hours (id, restaurant_id, day_of_week, opening_time, closing_time, is_closed) VALUES
    ('a1a1a1a1-a1a1-a1a1-a1a1-a1a1a1a1a1a1', '55555555-5555-5555-5555-555555555555', 'MONDAY', '11:00:00', '22:00:00', false),
    ('a2a2a2a2-a2a2-a2a2-a2a2-a2a2a2a2a2a2', '55555555-5555-5555-5555-555555555555', 'TUESDAY', '11:00:00', '22:00:00', false),
    ('a3a3a3a3-a3a3-a3a3-a3a3-a3a3a3a3a3a3', '55555555-5555-5555-5555-555555555555', 'WEDNESDAY', '11:00:00', '22:00:00', false),
    ('a4a4a4a4-a4a4-a4a4-a4a4-a4a4a4a4a4a4', '55555555-5555-5555-5555-555555555555', 'THURSDAY', '11:00:00', '22:00:00', false),
    ('a5a5a5a5-a5a5-a5a5-a5a5-a5a5a5a5a5a5', '55555555-5555-5555-5555-555555555555', 'FRIDAY', '11:00:00', '23:00:00', false),
    ('a6a6a6a6-a6a6-a6a6-a6a6-a6a6a6a6a6a6', '55555555-5555-5555-5555-555555555555', 'SATURDAY', '11:00:00', '23:00:00', false),
    ('a7a7a7a7-a7a7-a7a7-a7a7-a7a7a7a7a7a7', '55555555-5555-5555-5555-555555555555', 'SUNDAY', null, null, true);

-- Insert business hours for Sushi Express
INSERT INTO business_hours (id, restaurant_id, day_of_week, opening_time, closing_time, is_closed) VALUES
    ('b1b1b1b1-b1b1-b1b1-b1b1-b1b1b1b1b1b1', '66666666-6666-6666-6666-666666666666', 'MONDAY', '12:00:00', '23:00:00', false),
    ('b2b2b2b2-b2b2-b2b2-b2b2-b2b2b2b2b2b2', '66666666-6666-6666-6666-666666666666', 'TUESDAY', '12:00:00', '23:00:00', false),
    ('b3b3b3b3-b3b3-b3b3-b3b3-b3b3b3b3b3b3', '66666666-6666-6666-6666-666666666666', 'WEDNESDAY', '12:00:00', '23:00:00', false),
    ('b4b4b4b4-b4b4-b4b4-b4b4-b4b4b4b4b4b4', '66666666-6666-6666-6666-666666666666', 'THURSDAY', '12:00:00', '23:00:00', false),
    ('b5b5b5b5-b5b5-b5b5-b5b5-b5b5b5b5b5b5', '66666666-6666-6666-6666-666666666666', 'FRIDAY', '12:00:00', '00:00:00', false),
    ('b6b6b6b6-b6b6-b6b6-b6b6-b6b6b6b6b6b6', '66666666-6666-6666-6666-666666666666', 'SATURDAY', '12:00:00', '00:00:00', false),
    ('b7b7b7b7-b7b7-b7b7-b7b7-b7b7b7b7b7b7', '66666666-6666-6666-6666-666666666666', 'SUNDAY', '12:00:00', '22:00:00', false);

-- Insert menu items for Cantina Italiana
INSERT INTO menu_items (id, name, description, price, available_for_takeout, photo_path, restaurant_id, created_at, updated_at) VALUES
    ('c1c1c1c1-c1c1-c1c1-c1c1-c1c1c1c1c1c1', 'Spaghetti Carbonara', 'Spaghetti with creamy sauce, pancetta, eggs and parmesan cheese. A classic Italian pasta dish.', 38.90, true, '/images/spaghetti-carbonara.jpg', '55555555-5555-5555-5555-555555555555', NOW(), NOW()),
    ('c2c2c2c2-c2c2-c2c2-c2c2-c2c2c2c2c2c2', 'Lasagna Bolognese', 'Traditional lasagna with layers of fresh pasta, bolognese sauce, bechamel and parmesan cheese.', 45.90, true, '/images/lasagna-bolognese.jpg', '55555555-5555-5555-5555-555555555555', NOW(), NOW()),
    ('c3c3c3c3-c3c3-c3c3-c3c3-c3c3c3c3c3c3', 'Risotto ai Funghi', 'Creamy risotto with wild mushrooms, white wine, parmesan cheese and fresh herbs.', 42.90, true, '/images/risotto-funghi.jpg', '55555555-5555-5555-5555-555555555555', NOW(), NOW()),
    ('c4c4c4c4-c4c4-c4c4-c4c4-c4c4c4c4c4c4', 'Tiramisu', 'Classic Italian dessert with layers of coffee-soaked ladyfingers and mascarpone cream.', 25.90, true, '/images/tiramisu.jpg', '55555555-5555-5555-5555-555555555555', NOW(), NOW());

-- Insert menu items for Sushi Express
INSERT INTO menu_items (id, name, description, price, available_for_takeout, photo_path, restaurant_id, created_at, updated_at) VALUES
    ('d1d1d1d1-d1d1-d1d1-d1d1-d1d1d1d1d1d1', 'Sushi Combo', 'Assorted selection of fresh nigiri and maki sushi, including salmon, tuna, shrimp, and vegetarian options.', 79.90, true, '/images/sushi-combo.jpg', '66666666-6666-6666-6666-666666666666', NOW(), NOW()),
    ('d2d2d2d2-d2d2-d2d2-d2d2-d2d2d2d2d2d2', 'Sashimi Deluxe', 'Premium selection of thinly sliced fresh fish, including salmon, tuna, yellowtail, and octopus.', 89.90, true, '/images/sashimi-deluxe.jpg', '66666666-6666-6666-6666-666666666666', NOW(), NOW()),
    ('d3d3d3d3-d3d3-d3d3-d3d3-d3d3d3d3d3d3', 'Tempura Udon', 'Traditional Japanese udon noodle soup with tempura shrimp and vegetables.', 45.90, true, '/images/tempura-udon.jpg', '66666666-6666-6666-6666-666666666666', NOW(), NOW()),
    ('d4d4d4d4-d4d4-d4d4-d4d4-d4d4d4d4d4d4', 'Mochi Ice Cream', 'Sweet rice dough filled with ice cream. Assorted flavors including green tea, mango, and strawberry.', 18.90, true, '/images/mochi-ice-cream.jpg', '66666666-6666-6666-6666-666666666666', NOW(), NOW()); 