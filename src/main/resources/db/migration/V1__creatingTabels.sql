CREATE TABLE mst_roles (
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE mst_users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT REFERENCES mst_roles(id) NOT NULL
);

CREATE TABLE mst_admin (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES mst_users(id) NOT NULL
);

CREATE TABLE mst_trainer (
    id BIGSERIAL PRIMARY KEY,
    age INT NOT NULL,
    experience VARCHAR(255) NOT NULL,
    user_id BIGINT REFERENCES mst_users(id) NOT NULL
);

CREATE TABLE mst_player (
    id BIGSERIAL PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    height INT NOT NULL,
    weight INT NOT NULL,
    image VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    user_id BIGINT REFERENCES mst_users(id) NOT NULL
);

CREATE TABLE mst_payment (
    id BIGSERIAL PRIMARY KEY,
    amount INT NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    payment_status BOOLEAN NOT NULL,
    user_id BIGINT REFERENCES mst_users(id) NOT NULL
);


CREATE TABLE mst_packages (
    id BIGSERIAL PRIMARY KEY,
    package_name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE mst_notification (
    id BIGSERIAL PRIMARY KEY,
    message VARCHAR(255) NOT NULL,
    time_stamp DATE NOT NULL,
    sender_id BIGINT REFERENCES mst_users(id) NOT NULL,
    receiver_id BIGINT REFERENCES mst_users(id) NOT NULL
);

CREATE TABLE mst_class (
    id BIGSERIAL PRIMARY KEY,
    schedule DATE NOT NULL,
    description VARCHAR(255) NOT NULL,
    class_name VARCHAR(255) NOT NULL,
    trainer_id BIGINT REFERENCES mst_trainer(id) NOT NULL
);

CREATE TABLE mst_class_enrollment (
    id BIGSERIAL PRIMARY KEY,
    class_id BIGINT REFERENCES mst_class(id) NOT NULL,
    player_id BIGINT  REFERENCES mst_player(id) NOT NULL,
    enrollment_date date not null ,
    package_id BIGINT REFERENCES mst_packages(id)
);