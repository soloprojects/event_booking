CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CHECK (LENGTH(password) >= 8)
);

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    token_type VARCHAR(50) NOT NULL DEFAULT 'BEARER',
    revoked BOOLEAN NOT NULL DEFAULT false,
    expired BOOLEAN NOT NULL DEFAULT false,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
);


CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    available_attendees_count INT NOT NULL,
    event_description TEXT,
    category VARCHAR(100) NOT NULL
);

-- Insert Roles
INSERT INTO roles (name, description) VALUES ('ROLE_ADMIN', 'ADMIN');
INSERT INTO roles (name, description) VALUES ('ROLE_USER', 'USER');
INSERT INTO roles (name, description) VALUES ('ROLE_MANAGER', 'MANAGER');
INSERT INTO roles (name, description) VALUES ('ROLE_EDITOR', 'EDITOR');
INSERT INTO roles (name, description) VALUES ('ROLE_VIEWER', 'VIEWER');

-- Insert Events
INSERT INTO events (name, date, available_attendees_count, event_description, category)
VALUES ('Tech Conference 2024', '2024-09-15', 500, 'Annual tech conference for developers', 'CONFERENCE');

INSERT INTO events (name, date, available_attendees_count, event_description, category)
VALUES ('Web Development Workshop', '2024-08-10', 100, 'Hands-on workshop on modern web development', 'CONCERT');

INSERT INTO events (name, date, available_attendees_count, event_description, category)
VALUES ('Marketing Seminar', '2024-07-20', 50, 'Seminar focusing on latest trends in digital marketing', 'GAME');

INSERT INTO events (name, date, available_attendees_count, event_description, category)
VALUES ('Local Meetup', '2024-06-30', 30, 'Monthly meetup of local tech enthusiasts', 'CONFERENCE');

INSERT INTO events (name, date, available_attendees_count, event_description, category)
VALUES ('Webinar Series Launch', '2024-07-05', 200, 'Launch of a series of webinars on industry topics', 'GAME');