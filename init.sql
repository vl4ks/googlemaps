CREATE TABLE Cities (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    region VARCHAR(255),
    country VARCHAR(255) NOT NULL
);

CREATE TABLE Addresses (
    id SERIAL PRIMARY KEY,
    city_id INT NOT NULL,
    street VARCHAR(255) NOT NULL,
    building VARCHAR(50) NOT NULL,
    latitude DECIMAL(9, 6),
    longitude DECIMAL(9, 6),
    FOREIGN KEY (city_id) REFERENCES Cities(id) ON DELETE CASCADE
);

CREATE TABLE Categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_category_id INT,
    FOREIGN KEY (parent_category_id) REFERENCES Categories(id) ON DELETE SET NULL
);

CREATE TABLE Businesses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    phone VARCHAR(20),
    website VARCHAR(255),
    email VARCHAR(255),
    address_id INT NOT NULL,
    category_id INT NOT NULL,
    rating DECIMAL(3, 2) DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (address_id) REFERENCES Addresses(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES Categories(id) ON DELETE CASCADE
);

CREATE TABLE Users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Reviews (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    business_id INT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (business_id) REFERENCES Businesses(id) ON DELETE CASCADE
);

CREATE TABLE Images (
    id SERIAL PRIMARY KEY,
    business_id INT NOT NULL,
    url VARCHAR(255) NOT NULL,
    description TEXT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (business_id) REFERENCES Businesses(id) ON DELETE CASCADE
);

CREATE TABLE WorkingHours (
    id SERIAL PRIMARY KEY,
    business_id INT NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    opening_time TIME,
    closing_time TIME,
    is_closed BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (business_id) REFERENCES Businesses(id) ON DELETE CASCADE
);

INSERT INTO Cities (name, region, country) VALUES
('Тюмень', 'Тюменская область', 'Россия');

INSERT INTO Addresses (city_id, street, building, latitude, longitude) VALUES
(1, 'ул. Республики', '142', 57.152985, 65.541227),
(1, 'ул. Ленина', '49', 57.156634, 65.534200),
(1, 'ул. Первомайская', '10', 57.159000, 65.527500),
(1, 'ул. Малыгина', '74', 57.162000, 65.550000);

INSERT INTO Categories (name, parent_category_id) VALUES
('Рестораны', NULL),
('Кафе', 1),
('Магазины', NULL),
('Торговые центры', NULL),
('Культура и искусство', NULL);

INSERT INTO Businesses (name, description, phone, website, email, address_id, category_id, rating) VALUES
('ТЦ "Колумб"', 'Крупный торговый центр в центре Тюмени', '+73452567890', 'http://columb-tmn.ru', 'info@columb-tmn.ru', 1, 4, 4.6),
('Тюменская филармония', 'Концертный зал с классической музыкой', '+73452345678', 'http://philarmonia-tmn.ru', 'info@philarmonia-tmn.ru', 2, 5, 4.8),
('Кафе "Кофейня у реки"', 'Уютное кафе с видом на Туру', '+73452123456', 'http://coffee-river.ru', 'coffee@river.ru', 3, 2, 4.5),
('ТРЦ "Сити Молл"', 'Торгово-развлекательный центр', '+73452765432', 'http://citymall-tmn.ru', 'info@citymall-tmn.ru', 4, 4, 4.7);

INSERT INTO Users (username, email, password_hash) VALUES
('user1', 'user1@example.com', 'hash1'),
('user2', 'user2@example.com', 'hash2'),
('user3', 'user3@example.com', 'hash3');

INSERT INTO Reviews (user_id, business_id, rating, comment) VALUES
(1, 1, 5, 'Отличный торговый центр, много магазинов!'),
(2, 1, 4, 'Хорошо, но цены высокие.'),
(3, 2, 5, 'Прекрасная филармония, отличные концерты!'),
(1, 3, 4, 'Уютное кафе, но мало места.'),
(2, 4, 5, 'Лучший ТРЦ в Тюмени!');

INSERT INTO Images (business_id, url, description) VALUES
(1, 'http://columb-tmn.ru/image1.jpg', 'Фото ТЦ "Колумб"'),
(2, 'http://philarmonia-tmn.ru/image1.jpg', 'Фото филармонии'),
(3, 'http://coffee-river.ru/image1.jpg', 'Фото кафе "Кофейня у реки"'),
(4, 'http://citymall-tmn.ru/image1.jpg', 'Фото ТРЦ "Сити Молл"');

INSERT INTO WorkingHours (business_id, day_of_week, opening_time, closing_time, is_closed) VALUES
(1, 'Понедельник', '10:00', '22:00', FALSE),
(1, 'Вторник', '10:00', '22:00', FALSE),
(1, 'Среда', '10:00', '22:00', FALSE),
(1, 'Четверг', '10:00', '22:00', FALSE),
(1, 'Пятница', '10:00', '22:00', FALSE),
(1, 'Суббота', '10:00', '22:00', FALSE),
(1, 'Воскресенье', '10:00', '22:00', FALSE),

(2, 'Понедельник', '12:00', '20:00', FALSE),
(2, 'Вторник', '12:00', '20:00', FALSE),
(2, 'Среда', '12:00', '20:00', FALSE),
(2, 'Четверг', '12:00', '20:00', FALSE),
(2, 'Пятница', '12:00', '20:00', FALSE),
(2, 'Суббота', '12:00', '20:00', FALSE),
(2, 'Воскресенье', NULL, NULL, TRUE),

(3, 'Понедельник', '08:00', '23:00', FALSE),
(3, 'Вторник', '08:00', '23:00', FALSE),
(3, 'Среда', '08:00', '23:00', FALSE),
(3, 'Четверг', '08:00', '23:00', FALSE),
(3, 'Пятница', '08:00', '23:00', FALSE),
(3, 'Суббота', '08:00', '23:00', FALSE),
(3, 'Воскресенье', '08:00', '23:00', FALSE),

(4, 'Понедельник', '10:00', '22:00', FALSE),
(4, 'Вторник', '10:00', '22:00', FALSE),
(4, 'Среда', '10:00', '22:00', FALSE),
(4, 'Четверг', '10:00', '22:00', FALSE),
(4, 'Пятница', '10:00', '22:00', FALSE),
(4, 'Суббота', '10:00', '22:00', FALSE),
(4, 'Воскресенье', '10:00', '22:00', FALSE);