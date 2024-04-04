-- Создание таблицы пользователи
CREATE TABLE IF NOT EXISTS users
(
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
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
    user_id INT NOT NULL,
    is_active BOOLEAN NOT NULL,
    is_auto BOOLEAN NOT NULL,
    min_temperature INT NOT NULL,
    max_temperature INT NOT NULL,
    min_light INT NOT NULL,
    max_light INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
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

-- Создание таблицы грядки
CREATE TABLE IF NOT EXISTS seedbeds
(
    id SERIAL PRIMARY KEY,
    seedbed_name VARCHAR(50) NOT NULL,
    greenhouse_id INT NOT NULL,
    is_auto BOOLEAN NOT NULL,
    is_active BOOLEAN NOT NULL,
    watering_duration INT NOT NULL,
    watering_frequency INT NOT NULL,
    min_humidity INT NOT NULL,
    max_humidity INT NOT NULL,
    FOREIGN KEY (greenhouse_id) REFERENCES greenhouses(id)
);

-- Создание таблицы контроль за статусом доп факторов
CREATE TABLE IF NOT EXISTS control
(
    id SERIAL PRIMARY KEY,
    greenhouse_id INT NOT NULL,
    window_status INT NOT NULL,
    watering_enabled BOOLEAN NOT NULL,
    light_enabled BOOLEAN NOT NULL,
    fan_enabled BOOLEAN NOT NULL,
    heater_enabled BOOLEAN NOT NULL,
    FOREIGN KEY (greenhouse_id) REFERENCES greenhouses(id)
);

-- Создание таблицы типы датчиков(влажности, освещенности, температуры)
CREATE TABLE IF NOT EXISTS sensor_types
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
    FOREIGN KEY (greenhouse_id) REFERENCES greenhouses(id),
    FOREIGN KEY (sensor_type_id) REFERENCES sensor_types(id)
);

-- Создание таблицы темы логов
CREATE TABLE IF NOT EXISTS topics
(
    id SERIAL PRIMARY KEY,
    topic_name VARCHAR(50) NOT NULL
);

-- Создание таблицы отправленные логи (команды, которые необходимо сделать)
CREATE TABLE IF NOT EXISTS send_logs
(
    id SERIAL PRIMARY KEY,
    topic_id INT NOT NULL,
    reply BOOLEAN NOT NULL,
    command INT NOT NULL,
    send_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (topic_id) REFERENCES topics(id)
);

-- Создание таблицы принятые логи (показатели с датчиков)
CREATE TABLE IF NOT EXISTS receive_logs
(
    id SERIAL PRIMARY KEY,
    topic_id INT NOT NULL,
    message VARCHAR(255) NOT NULL,
    validity BOOLEAN NOT NULL,
    receive_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (topic_id) REFERENCES topics(id)
);

-- Создание таблицы уровень влажности
CREATE TABLE IF NOT EXISTS humidity
(
    id SERIAL PRIMARY KEY,
    receive_log_id INT NOT NULL,
    seedbed_id INT NOT NULL,
    value INT NOT NULL,
    receive_time TIMESTAMP NOT NULL,
    FOREIGN KEY (receive_log_id) REFERENCES receive_logs(id),
    FOREIGN KEY (seedbed_id) REFERENCES seedbeds(id)
);

-- Создание таблицы уровень освещенности
CREATE TABLE IF NOT EXISTS light
(
    id SERIAL PRIMARY KEY,
    receive_log_id INT NOT NULL,
    greenhouse_id INT NOT NULL,
    value INT NOT NULL,
    receive_time TIMESTAMP NOT NULL,
    FOREIGN KEY (receive_log_id) REFERENCES receive_logs(id),
    FOREIGN KEY (greenhouse_id) REFERENCES greenhouses(id)
);

-- Создание таблицы уровень температуры
CREATE TABLE IF NOT EXISTS temperature
(
    id SERIAL PRIMARY KEY,
    receive_log_id INT NOT NULL,
    greenhouse_id INT NOT NULL,
    value INT NOT NULL,
    receive_time TIMESTAMP NOT NULL,
    FOREIGN KEY (receive_log_id) REFERENCES receive_logs(id),
    FOREIGN KEY (greenhouse_id) REFERENCES greenhouses(id)
);
