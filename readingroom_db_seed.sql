\connect readingroomdb;

INSERT INTO rr_users(user_id, first_name, last_name, email, password) VALUES(nextval('rr_users_seq'), 'Geddy', 'Lee', 'geddy@mail.com', '$2a$10$LF5Rf9lRXyZQ69FR7EF9Puqp.KomXyFeQyQAT6vEQ2mhJvy9MsU3G');
INSERT INTO rr_users(user_id, first_name, last_name, email, password) VALUES(nextval('rr_users_seq'), 'Jimmy', 'Page', 'jimmy@mail.com', '$2a$10$DcNMEVuyNGLVB5A7ZQrLve49b4eaZpo6abU3Gkpj87k34V/MzgED6');
INSERT INTO rr_users(user_id, first_name, last_name, email, password) VALUES(nextval('rr_users_seq'), 'John', 'Bonham', 'john@mail.com', '$2a$10$nBHcdD1OZfmQkZ7WlesWQOv4bQm1.6OQhp.hyHm3D.Fx2S/VVDXBO');


INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), 1, 'Books about Wine', 'Books on wine from various regions of Europe and North America.');
INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), 1, 'Baseball Books', 'Books about baseball from all eras.');
INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), 2, 'English & Misc. Architecture', 'Books on historical architecture of the British Isles.');
INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), 2, 'Occult', 'Aleister Crowley and other freakshows that influenced Led Zeppelin.');
INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), 2, 'Books About Led Zeppelin', 'A complete collection of books about my favorite band, Led Zeppelin.');
INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), 3, 'Drinking, Farming, and Farm Equipment', 'All the books I own.');