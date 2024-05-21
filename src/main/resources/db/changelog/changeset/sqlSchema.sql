CREATE TABLE sensor_type
(
    id          SERIAL PRIMARY KEY,
    sensor_name VARCHAR(50) NOT NULL
);

CREATE TABLE topics
(
    id         SERIAL PRIMARY KEY,
    topic_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE receive_logs
(
    id           SERIAL PRIMARY KEY,
    validity     BOOLEAN      NOT NULL,
    receive_time TIMESTAMP(6) NOT NULL,
    topic_id     BIGINT       NOT NULL,
    message      VARCHAR(255) NOT NULL,
    CONSTRAINT fkoi3vjyxlo32yxw85h67ju5r05 FOREIGN KEY (topic_id) REFERENCES topics (id)
);

CREATE TABLE send_logs
(
    id            SERIAL PRIMARY KEY,
    command       INTEGER      NOT NULL,
    reply         BOOLEAN      NOT NULL,
    send_time     TIMESTAMP(6) NOT NULL,
    topic_id      BIGINT       NOT NULL,
    greenhouse_id BIGINT       NOT NULL,
    constraint fkkujgmlito6k4tuyw5y73dp8tt FOREIGN KEY (greenhouse_id) references greenhouse (id),
    CONSTRAINT fkerxe1yxejdm5lbe2bwb9x37b5 FOREIGN KEY (topic_id) REFERENCES topics (id)
);

CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL,
    CONSTRAINT users_role_check CHECK ((role)::text = ANY
                                       ((ARRAY ['USER'::CHARACTER VARYING, 'ADMIN'::CHARACTER VARYING, 'MANAGER'::CHARACTER VARYING])::text[]))
);

CREATE TABLE greenhouse
(
    id              SERIAL PRIMARY KEY,
    user_id         INTEGER      NOT NULL,
    greenhouse_name VARCHAR(50)  NOT NULL,
    location        VARCHAR(255) NOT NULL,
    CONSTRAINT fk5t6yei2vlibhbtilhijjodvwi FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE configurations
(
    id              SERIAL PRIMARY KEY,
    is_active       BOOLEAN NOT NULL,
    is_auto         BOOLEAN NOT NULL,
    max_light       INTEGER NOT NULL,
    max_temperature INTEGER NOT NULL,
    min_light       INTEGER NOT NULL,
    min_temperature INTEGER NOT NULL,
    greenhouse_id   BIGINT  NOT NULL UNIQUE,
    CONSTRAINT fk1gfx57yj6uh041wgamg1am4kq FOREIGN KEY (greenhouse_id) REFERENCES greenhouse (id)
);

CREATE TABLE control
(
    id             SERIAL PRIMARY KEY,
    fan_enabled    BOOLEAN NOT NULL,
    heater_enabled BOOLEAN NOT NULL,
    light_enabled  BOOLEAN NOT NULL,
    window_status  INTEGER NOT NULL,
    greenhouse_id  BIGINT  NOT NULL UNIQUE,
    CONSTRAINT fkd74h8ms8g31c1l1gx1qk3ew5a FOREIGN KEY (greenhouse_id) REFERENCES greenhouse (id)
);

CREATE TABLE seedbeds
(
    id                 SERIAL PRIMARY KEY,
    is_auto            BOOLEAN     NOT NULL,
    max_humidity       INTEGER     NOT NULL,
    min_humidity       INTEGER     NOT NULL,
    watering_duration  INTEGER     NOT NULL,
    watering_enabled   BOOLEAN     NOT NULL,
    watering_frequency INTEGER     NOT NULL,
    greenhouse_id      BIGINT      NOT NULL,
    seedbed_name       VARCHAR(50) NOT NULL,
    CONSTRAINT fkpyuakexhwe3m6lvkuuxah6n43 FOREIGN KEY (greenhouse_id) REFERENCES greenhouse (id)
);

CREATE TABLE sensors
(
    id             SERIAL PRIMARY KEY,
    is_active      BOOLEAN NOT NULL,
    sensor_number  INTEGER NOT NULL,
    greenhouse_id  BIGINT  NOT NULL,
    sensor_type_id BIGINT  NOT NULL,
    CONSTRAINT fkkujgmlito6k4tuyw5y73dp8tt FOREIGN KEY (greenhouse_id) REFERENCES greenhouse (id),
    CONSTRAINT fk8fpn04m9aagposrvfs0aro3bj FOREIGN KEY (sensor_type_id) REFERENCES sensor_type (id)
);

CREATE TABLE humidity
(
    id             SERIAL PRIMARY KEY,
    value          INTEGER      NOT NULL,
    receive_log_id BIGINT       NOT NULL,
    receive_time   TIMESTAMP(6) NOT NULL,
    seedbed_id     BIGINT       NOT NULL,
    sensor_id      BIGINT       NOT NULL,
    CONSTRAINT fk_jggwfuwy7ssweduh6jio8xsm6 FOREIGN KEY (receive_log_id) REFERENCES receive_logs (id),
    CONSTRAINT fkhswlxxbvkbdw2upph6fr3oomr FOREIGN KEY (seedbed_id) REFERENCES seedbeds (id),
    CONSTRAINT fkhi07a7eptdld5jbr9wrer35j5 FOREIGN KEY (sensor_id) REFERENCES sensors (id)
);

CREATE TABLE light
(
    id             SERIAL PRIMARY KEY,
    value          INTEGER      NOT NULL,
    greenhouse_id  BIGINT       NOT NULL,
    receive_log_id BIGINT       NOT NULL,
    receive_time   TIMESTAMP(6) NOT NULL,
    sensor_id      BIGINT       NOT NULL,
    CONSTRAINT fkcb3ot2efo9o4xjmaondlrkvto FOREIGN KEY (greenhouse_id) REFERENCES greenhouse (id),
    CONSTRAINT fk_ewbf2w49nl7fjxmj9cgkurj8h FOREIGN KEY (receive_log_id) REFERENCES receive_logs (id),
    CONSTRAINT fk1h3sqa2l1w2sih7ts5hi73klx FOREIGN KEY (sensor_id) REFERENCES sensors (id)
);

CREATE TABLE temperature
(
    id             SERIAL PRIMARY KEY,
    value          INTEGER      NOT NULL,
    greenhouse_id  BIGINT       NOT NULL,
    receive_log_id BIGINT       NOT NULL,
    receive_time   TIMESTAMP(6) NOT NULL,
    sensor_id      BIGINT       NOT NULL,
    CONSTRAINT fkdu4oinr23xd9pm7mrmbc0fa27 FOREIGN KEY (greenhouse_id) REFERENCES greenhouse (id),
    CONSTRAINT fk_jducmk8rypbc8b1qn8unflr68 FOREIGN KEY (receive_log_id) REFERENCES receive_logs (id),
    CONSTRAINT fkabbpr52o315gjtqvpd9xfbepq FOREIGN KEY (sensor_id) REFERENCES sensors (id)
);

CREATE TABLE tokens
(
    id         SERIAL PRIMARY KEY,
    expired    BOOLEAN,
    revoked    BOOLEAN,
    user_id    INTEGER,
    token      VARCHAR(255) UNIQUE,
    token_type VARCHAR(255),
    CONSTRAINT fk2dylsfo39lgjyqml2tbe0b0ss FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT tokens_token_type_check CHECK ((token_type)::text = 'BEARER'::text)
);
