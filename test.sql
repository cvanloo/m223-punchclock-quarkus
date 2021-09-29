/* Create admin account (pw: test) */
INSERT INTO user (id, accountname, passwdhash, role)
VALUES (0, 'admin', '$2a$10$d1ZG88VwmtAgE0zs0wVXR.J.wEofuZ7Adl2LnDHiugLx2z4iLhZLG', 'Admin');

/* Create user accounts (pws: test) */
INSERT INTO user (id, accountname, passwdhash, role)
VALUES 
(1, 'test', '$2a$10$d1ZG88VwmtAgE0zs0wVXR.J.wEofuZ7Adl2LnDHiugLx2z4iLhZLG', 'User'),
(2, 'hello', '$2a$10$d1ZG88VwmtAgE0zs0wVXR.J.wEofuZ7Adl2LnDHiugLx2z4iLhZLG', 'User');

/* Create facilities */
INSERT INTO facility (fulladdress, name)
VALUES
('Edenstrasse 20, 8045 Zuerich', 'ZLI'),
('Brunnenstrasse 1, 8604 Volketswil', 'Zetes');

/* Create Category */
INSERT INTO category (description, name)
VALUES
('Multi-user applikation objektorientiert implementieren.', 'UeK 233'),
('Einführung in Android App Entwicklung', 'Anrdoid');

/* Create entries */
INSERT INTO entry (checkin, checkout, user_id)
VALUES
('2021-09-29 03:00:00', '2021-09-29 12:45:00', 1),
('2021-09-22 04:45:00', '2021-09-22 14:45:00', 1),
('2021-09-30 04:55:00', '2021-09-30 12:45:00', 2);