INSERT INTO rr_users(user_id, first_name, last_name, email, password) VALUES(nextval('rr_users_seq'), 'Geddy', 'Lee', 'geddy@mail.com', '$2a$10$LF5Rf9lRXyZQ69FR7EF9Puqp.KomXyFeQyQAT6vEQ2mhJvy9MsU3G');
INSERT INTO rr_users(user_id, first_name, last_name, email, password) VALUES(nextval('rr_users_seq'), 'Jimmy', 'Page', 'jimmy@mail.com', '$2a$10$DcNMEVuyNGLVB5A7ZQrLve49b4eaZpo6abU3Gkpj87k34V/MzgED6');
INSERT INTO rr_users(user_id, first_name, last_name, email, password) VALUES(nextval('rr_users_seq'), 'John', 'Bonham', 'john@mail.com', '$2a$10$nBHcdD1OZfmQkZ7WlesWQOv4bQm1.6OQhp.hyHm3D.Fx2S/VVDXBO');

INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), 1, 'Books about Wine', 'Books on wine from various regions of Europe and North America.');
INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), 1, 'Baseball Books', 'My favorite books about baseball.');
INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), 2, 'English & Misc. Architecture', 'Books on historical architecture of the British Isles.');
INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), 2, 'Occult', 'Aleister Crowley and other freakshows that influenced Led Zeppelin.');
INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), 2, 'Books About Led Zeppelin', 'A complete collection of books about my favorite band, Led Zeppelin.');
INSERT INTO rr_shelves (shelf_id, user_id, title, description) VALUES(NEXTVAL('rr_shelves_seq'), 3, 'Drinking, Farming, and Farm Equipment', 'All the books I own.');

INSERT INTO rr_saved_books (book_id, shelf_id, user_id, isbn, ol_key, title, author, user_note, saved_date) VALUES(NEXTVAL('rr_saved_books_seq'), 1, 1, '9780312342517', 'OL6032280W', 'Sideways', 'Rex Pickett', 'Kind of about wine.', 1704342375323);
INSERT INTO rr_saved_books (book_id, shelf_id, user_id, isbn, ol_key, title, author, user_note, saved_date) VALUES(NEXTVAL('rr_saved_books_seq'), 1, 1, '9781838956653', 'OL35727480W', 'Vines in a Cold Climate', 'Henry Jeffreys', null, 1704342378300);
INSERT INTO rr_saved_books (book_id, shelf_id, user_id, isbn, ol_key, title, author, user_note, saved_date) VALUES(NEXTVAL('rr_saved_books_seq'), 2, 1, '9780803290723', 'OL26795525W', 'From the Dugouts to the Trenches', 'Jim Leeke', null, 1704341378400);
INSERT INTO rr_saved_books (book_id, shelf_id, user_id, isbn, ol_key, title, author, user_note, saved_date) VALUES(NEXTVAL('rr_saved_books_seq'), 3, 2, '9780470868089', 'OL8208787W', 'Philip Webb: Pioneer of Arts & Crafts Architecture', 'Sheila Kirk', 'A great resource for info on the red house.', 1704341375550);
INSERT INTO rr_saved_books (book_id, shelf_id, user_id, isbn, ol_key, title, author, user_note, saved_date) VALUES(NEXTVAL('rr_saved_books_seq'), 3, 2, '9780395171202', 'OL113116W', 'The Cecils of Hatfield House', 'David Lord Cecil', 'Book received from John Paul Jones.', 1704341377500);
INSERT INTO rr_saved_books (book_id, shelf_id, user_id, isbn, ol_key, title, author, user_note, saved_date) VALUES(NEXTVAL('rr_saved_books_seq'), 3, 2, '9780300101775', null, 'Bath', 'Michael Forsyth', null, 1704341387500);
INSERT INTO rr_saved_books (book_id, shelf_id, user_id, isbn, ol_key, title, author, user_note, saved_date) VALUES(NEXTVAL('rr_saved_books_seq'), 4, 2, '9781556438998', 'OL15633392W', 'Perdurabo: The Life of Aleister Crowley', 'Richard Kaczynski', null, 1704341387905);
INSERT INTO rr_saved_books (book_id, shelf_id, user_id, isbn, ol_key, title, author, user_note, saved_date) VALUES(NEXTVAL('rr_saved_books_seq'), 4, 2, '9780143131687', 'OL20156281W', 'The Kybalion: Centenary Edition', 'Three Initiates', null, 1704341387905);
INSERT INTO rr_saved_books (book_id, shelf_id, user_id, isbn, ol_key, title, author, user_note, saved_date) VALUES(NEXTVAL('rr_saved_books_seq'), 5, 2, '9781905662326', 'OL8563330W', 'Jimmy Page by Jimmy Page', 'Jimmy Page', 'The greatest book ever printed, by me.', 1704341388805);
INSERT INTO rr_saved_books (book_id, shelf_id, user_id, isbn, ol_key, title, author, user_note, saved_date) VALUES(NEXTVAL('rr_saved_books_seq'), 5, 2, '9780399562426', 'OL25773811W', 'Led Zeppelin: The Biography', 'Bob Spitz', 'A wonderful story.', 1704341388966);
INSERT INTO rr_saved_books (book_id, shelf_id, user_id, isbn, ol_key, title, author, user_note, saved_date) VALUES(NEXTVAL('rr_saved_books_seq'), 5, 2, '9780307985750', 'OL16558410W', 'Light and Shade: Conversations with Jimmy Page', 'Brad Tolinski', 'Wonderful conversations.', 1704341388777);
INSERT INTO rr_saved_books (book_id, shelf_id, user_id, isbn, ol_key, title, author, user_note, saved_date) VALUES(NEXTVAL('rr_saved_books_seq'), 6, 3, '9781846685538', 'OL25732707W', 'A Natural History Of The Hedgerow', 'John Wright', null, 1704341377951);