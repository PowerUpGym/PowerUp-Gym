CREATE TABLE mst_roles (
    id bigserial PRIMARY KEY,
    role VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE mst_users (
    id bigserial PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    user_name VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    role_id BIGINT,
    FOREIGN KEY (role_id) REFERENCES mst_roles(id)
);

CREATE TABLE mst_admin (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES mst_users(id) NOT NULL
);

CREATE TABLE mst_trainer (
    id bigserial PRIMARY KEY,
    age INT NOT NULL,
    experience VARCHAR(255) NOT NULL,
    user_id BIGINT,
    admin_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES mst_users(id),
    FOREIGN KEY (admin_id) REFERENCES mst_admin(id)
);
CREATE TABLE mst_packages (
    id  bigserial PRIMARY KEY,
    package_name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    duration INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    admin_id BIGINT,
    FOREIGN KEY (admin_id) REFERENCES mst_admin(id)
);
CREATE TABLE mst_player (
    id bigserial PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    height INT NOT NULL,
    weight INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    account_enabled BOOLEAN,
    package_id BIGINT,
    user_id BIGINT,
    admin_id BIGINT,
    FOREIGN KEY (package_id) REFERENCES mst_packages(id),
    FOREIGN KEY (user_id) REFERENCES mst_users(id),
    FOREIGN KEY (admin_id) REFERENCES mst_admin(id)
);

CREATE TABLE mst_payment (
    id  bigserial PRIMARY KEY,
    amount INT NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    payment_status BOOLEAN NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES mst_users(id)
);



CREATE TABLE mst_notification (
    id  bigserial PRIMARY KEY,
    message VARCHAR(255) NOT NULL,
    time_stamp DATE NOT NULL,
    sender_id BIGINT,
    receiver_id BIGINT,
    FOREIGN KEY (sender_id) REFERENCES mst_users(id),
    FOREIGN KEY (receiver_id) REFERENCES mst_users(id)
);

CREATE TABLE mst_class (
    id  bigserial PRIMARY KEY,
    schedule VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    class_name VARCHAR(255) NOT NULL,
    trainer_id BIGINT,
    admin_id BIGINT,
    FOREIGN KEY (trainer_id) REFERENCES mst_trainer(id),
    FOREIGN KEY (admin_id) REFERENCES mst_admin(id)
);

CREATE TABLE mst_class_enrollment (
    id  bigserial PRIMARY KEY,
    player_id BIGINT,
    class_id BIGINT,
    enrollment_date DATE,
    FOREIGN KEY (player_id) REFERENCES mst_player(id),
    FOREIGN KEY (class_id) REFERENCES mst_class(id)
);

