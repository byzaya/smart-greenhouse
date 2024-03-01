-- Создание таблицы пользователи
CREATE TABLE IF NOT EXISTS users
(
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы токены
CREATE TABLE IF NOT EXISTS tokens
(
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    token VARCHAR(255) NOT NULL,
    tokenType VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Создание таблицы конфигурации теплицы
CREATE TABLE IF NOT EXISTS configurations
(
    id SERIAL PRIMARY KEY,
    is_active BOOLEAN NOT NULL,
    is_auto BOOLEAN NOT NULL,
    watering_duration INT NOT NULL,
    min_humidity INT NOT NULL,
    max_humidity INT NOT NULL,
    min_temperature INT NOT NULL,
    max_temperature INT NOT NULL,
    min_light INT NOT NULL,
    max_light INT NOT NULL
);

-- Создание таблицы теплицы
CREATE TABLE IF NOT EXISTS greenhouses
(
    id SERIAL PRIMARY KEY,
    location VARCHAR(255) NOT NULL,
    greenhouse_name VARCHAR(50) NOT NULL,
    user_id INT NOT NULL,
    configuration_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (configuration_id) REFERENCES configurations(id)
);

-- Создание таблицы контроль за статусом доп факторов
CREATE TABLE IF NOT EXISTS control
(
    id SERIAL PRIMARY KEY,
    greenhouse_id INT NOT NULL,
    watering_enabled BOOLEAN NOT NULL,
    window_status VARCHAR(50) NOT NULL,
    light_enabled BOOLEAN NOT NULL,
    fan_enabled BOOLEAN NOT NULL,
    heater_enabled BOOLEAN NOT NULL,
    FOREIGN KEY (greenhouse_id) REFERENCES greenhouse(id)
);

-- Создание таблицы типы датчиков(влажности, освещенности, температуры)
CREATE TABLE IF NOT EXISTS sensor_type
(
    id SERIAL PRIMARY KEY,
    sensor_name VARCHAR(50) NOT NULL
);

-- Создание таблицы датчики
CREATE TABLE IF NOT EXISTS sensors
(
    id SERIAL PRIMARY KEY,
    sensor_number INT NOT NULL,
    sensor_type_id INT NOT NULL,
    greenhouse_id INT NOT NULL,
    is_active BOOLEAN NOT NULL,
    FOREIGN KEY (greenhouse_id) REFERENCES greenhouse(id),
    FOREIGN KEY (sensor_type_id) REFERENCES sensor_type(id)
);
