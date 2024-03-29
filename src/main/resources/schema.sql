DROP TABLE IF EXISTS bookings, items, requests, users, comments;

CREATE TABLE IF NOT EXISTS users(
                                id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                name VARCHAR(255) NOT NULL,
                                email VARCHAR(255) NOT NULL,
                        CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests(
                                       id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                       description  VARCHAR(1024) NOT NULL,
                                       requestor_id  BIGINT NOT NULL,
                                       created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                        CONSTRAINT fk_requests_requestor_id FOREIGN KEY (requestor_id) REFERENCES users (id) ON DELETE CASCADE

);

CREATE TABLE IF NOT EXISTS items (
                                     id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
                                     description VARCHAR(1024) NOT NULL,
                                     is_available BOOLEAN NOT NULL,
                                     owner_id BIGINT NOT NULL,
                                     request_id BIGINT,
                        CONSTRAINT fk_items_owner_id FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE,
                        CONSTRAINT fk_items_request_id FOREIGN KEY (request_id) REFERENCES requests (id) ON DELETE CASCADE
);



CREATE TABLE IF NOT EXISTS bookings(
                                id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                start_date TIMESTAMP WITHOUT TIME ZONE,
                                end_date TIMESTAMP WITHOUT TIME ZONE,
                                item_id BIGINT,
                                booker_id BIGINT,
                                status VARCHAR(255),
                        CONSTRAINT fk_bookings_booker_id FOREIGN KEY (booker_id) REFERENCES users (id) ON DELETE CASCADE,
                        CONSTRAINT fk_bookings_item_id FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments(
                                    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                    text VARCHAR (10000),
                                    item_id BIGINT NOT NULL,
                                    author_id BIGINT NOT NULL,
                                    created   TIMESTAMP WITHOUT TIME ZONE,
                        CONSTRAINT fk_comments_item_id FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE,
                        CONSTRAINT fk_comments_author_id FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE INDEX bookings_start_end_date ON bookings (start_date, end_date);
CREATE INDEX requests_created_date ON requests (created);

