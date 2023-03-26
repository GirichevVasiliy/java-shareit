DELETE FROM users;
ALTER TABLE users ALTER COLUMN ID RESTART WITH 1;
DELETE FROM bookings;
ALTER TABLE bookings ALTER COLUMN ID RESTART WITH 1;
DELETE FROM comments;
ALTER TABLE comments ALTER COLUMN ID RESTART WITH 1;
DELETE FROM items;
ALTER TABLE items ALTER COLUMN ID RESTART WITH 1;
DELETE FROM requests;
ALTER TABLE requests ALTER COLUMN ID RESTART WITH 1;

INSERT INTO users(name, email) VALUES ('user1', 'y1@email.ru');
INSERT INTO users(name, email) VALUES ('user2', 'y@2email.ru');
INSERT INTO users(name, email) VALUES ('user3', 'y@3email.ru');
INSERT INTO users(name, email) VALUES ('user4', 'y@4email.ru');
INSERT INTO users(name, email) VALUES ('user5', 'y@5email.ru');

INSERT INTO requests(description, requestor_id, created) VALUES ('req1', 1, '2023-03-12 17:40:00.000000');
INSERT INTO requests(description, requestor_id, created) VALUES ('req2', 1, '2023-03-12 17:45:00.000000');
INSERT INTO requests(description, requestor_id, created) VALUES ('req3', 1, '2023-03-12 17:45:00.000000');
INSERT INTO requests(description, requestor_id, created) VALUES ('req4', 2, '2023-03-12 17:45:00.000000');
INSERT INTO requests(description, requestor_id, created) VALUES ('req5', 2, '2023-03-12 17:45:00.000000');
INSERT INTO requests(description, requestor_id, created) VALUES ('req6', 3, '2023-03-12 17:45:00.000000');



INSERT INTO items(name, description, is_available, owner_id, request_id) VALUES ('item1', 'item1Desc', true, 5, 1);
INSERT INTO items(name, description, is_available, owner_id, request_id) VALUES ('item2', 'item2Desc', true, 5, 2);
INSERT INTO items(name, description, is_available, owner_id, request_id) VALUES ('item3', 'item3Desc', true, 5, 3);
INSERT INTO items(name, description, is_available, owner_id, request_id) VALUES ('item4', 'item4Desc', true, 5, 1);
INSERT INTO items(name, description, is_available, owner_id, request_id) VALUES ('item5', 'item4Desc', true, 5, 1);
INSERT INTO items(name, description, is_available, owner_id, request_id) VALUES ('item6', 'item4Desc', false, 4, 6);
INSERT INTO items(name, description, is_available, owner_id, request_id) VALUES ('item7', 'item4Desc', true, 4, null);
INSERT INTO items(name, description, is_available, owner_id, request_id) VALUES ('item8', 'item4Desc', true, 4, null);

INSERT INTO bookings(start_date, end_date, item_id, booker_id, status) VALUES ('2023-05-12 17:00:00.000000', '2023-05-12 17:05:00.000000', 1, 4, 'APPROVED');
INSERT INTO bookings(start_date, end_date, item_id, booker_id, status) VALUES ('2023-05-13 17:00:00.000000', '2023-05-13 17:05:00.000000', 2, 4, 'APPROVED');
INSERT INTO bookings(start_date, end_date, item_id, booker_id, status) VALUES ('2023-05-14 17:00:00.000000', '2023-05-14 17:05:00.000000', 3, 4, 'APPROVED');
INSERT INTO bookings(start_date, end_date, item_id, booker_id, status) VALUES ('2023-05-15 17:00:00.000000', '2023-05-15 17:05:00.000000', 4, 4, 'APPROVED');
INSERT INTO bookings(start_date, end_date, item_id, booker_id, status) VALUES ('2022-05-12 17:00:00.000000', '2022-05-12 17:05:00.000000', 1, 4, 'APPROVED');

INSERT INTO COMMENTS(text, item_id, author_id, created) VALUES ('comment1', 1, 4, '2023-05-12 18:00:00.000000');
INSERT INTO COMMENTS(text, item_id, author_id, created) VALUES ('comment2', 2, 4, '2023-05-19 19:00:00.000000');
INSERT INTO COMMENTS(text, item_id, author_id, created) VALUES ('comment3', 3, 4, '2023-05-13 20:00:00.000000');