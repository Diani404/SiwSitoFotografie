-- Dati iniziali caricati da Hibernate a ogni avvio (spring.jpa.hibernate.ddl-auto=create)
-- Le sequenze vengono spostate a 1000 in coda al file, cosi' gli id espliciti non collidono con quelli generati dall'applicazione

-- eventi: il prezzo e' quello del pacchetto completo, l'unico prodotto in vendita per l'evento
-- (la copertina viene impostata dopo l'inserimento delle foto)
insert into event (id, title, event_type, event_date, location, description, price) values (1, 'Matrimonio di Anna e Marco', 'MATRIMONIO', '2026-06-13', 'Frascati (RM)', 'Una giornata di giugno piena di sole, dalla preparazione della sposa fino al taglio della torta in villa. Ho cercato di raccontare soprattutto i momenti spontanei: gli abbracci, le risate e le lacrime degli invitati.', 480.00);
insert into event (id, title, event_type, event_date, location, description, price) values (2, 'Matrimonio di Sara e Luca', 'MATRIMONIO', '2026-05-09', 'Bracciano (RM)', 'Cerimonia civile sul lago e ricevimento al tramonto. La luce di maggio ha reso tutto molto morbido: nel pacchetto ci sono la cerimonia, il servizio agli sposi e la festa.', 520.00);
insert into event (id, title, event_type, event_date, location, description, price) values (3, 'Prima comunione di Giulia', 'COMUNIONE', '2026-05-24', 'Roma, parrocchia di Santa Maria', 'La prima comunione di Giulia con la famiglia e i compagni di catechismo: la celebrazione in chiesa e il pranzo con i parenti.', 180.00);
insert into event (id, title, event_type, event_date, location, description, price) values (4, 'Comunioni di Sant''Antonio', 'COMUNIONE', '2026-05-17', 'Marino (RM)', 'Servizio per il gruppo di bambini della parrocchia di Sant''Antonio: ritratti singoli, foto di gruppo e tutta la celebrazione.', 150.00);
insert into event (id, title, event_type, event_date, location, description, price) values (5, 'Cresima di Matteo', 'CRESIMA', '2026-04-19', 'Albano Laziale (RM)', 'La cresima di Matteo in cattedrale, con il padrino e tutta la famiglia, piu'' qualche scatto del pranzo in agriturismo.', 160.00);
insert into event (id, title, event_type, event_date, location, description, price) values (6, 'Battesimo di Sofia', 'BATTESIMO', '2026-03-15', 'Roma, Trastevere', 'Il battesimo della piccola Sofia in una domenica di marzo: la chiesa, il chiostro, i nonni e gli amici di famiglia.', 170.00);

-- foto: fanno parte del pacchetto, non hanno un prezzo proprio (le immagini di prova stanno in static/images/demo)
insert into photo (id, title, description, image_url, event_id) values (1, 'I preparativi', 'Ritratto posato, formato orizzontale.', '/images/demo/matrimonio-01.jpg', 1);
insert into photo (id, title, description, image_url, event_id) values (2, 'L''arrivo in chiesa', 'Uno dei miei preferiti della giornata.', '/images/demo/matrimonio-02.jpg', 1);
insert into photo (id, title, description, image_url, event_id) values (3, 'Lo scambio degli anelli', 'Dettaglio in primo piano.', '/images/demo/matrimonio-03.jpg', 1);
insert into photo (id, title, description, image_url, event_id) values (4, 'Il primo bacio', null, '/images/demo/matrimonio-04.jpg', 1);
insert into photo (id, title, description, image_url, event_id) values (5, 'Riso e coriandoli', 'Scatto rubato durante la cerimonia.', '/images/demo/matrimonio-05.jpg', 1);
insert into photo (id, title, description, image_url, event_id) values (6, 'Il ritratto degli sposi', 'Luce naturale, nessun flash.', '/images/demo/matrimonio-06.jpg', 1);
insert into photo (id, title, description, image_url, event_id) values (7, 'Il brindisi', null, '/images/demo/matrimonio-07.jpg', 1);
insert into photo (id, title, description, image_url, event_id) values (8, 'Il primo ballo', 'Foto di gruppo: ideale per la stampa grande.', '/images/demo/matrimonio-08.jpg', 1);
insert into photo (id, title, description, image_url, event_id) values (9, 'Le damigelle', 'Ritratto posato, formato orizzontale.', '/images/demo/matrimonio-09.jpg', 1);
insert into photo (id, title, description, image_url, event_id) values (10, 'Il taglio della torta', 'Ritratto posato, formato orizzontale.', '/images/demo/matrimonio-10.jpg', 1);
insert into photo (id, title, description, image_url, event_id) values (11, 'Gli invitati in giardino', null, '/images/demo/matrimonio-11.jpg', 1);
insert into photo (id, title, description, image_url, event_id) values (12, 'L''ultimo scatto della sera', null, '/images/demo/matrimonio-12.jpg', 1);
insert into photo (id, title, description, image_url, event_id) values (13, 'I preparativi', null, '/images/demo/matrimonio-01.jpg', 2);
insert into photo (id, title, description, image_url, event_id) values (14, 'L''arrivo in chiesa', 'Uno dei miei preferiti della giornata.', '/images/demo/matrimonio-02.jpg', 2);
insert into photo (id, title, description, image_url, event_id) values (15, 'Lo scambio degli anelli', 'Ritratto posato, formato orizzontale.', '/images/demo/matrimonio-03.jpg', 2);
insert into photo (id, title, description, image_url, event_id) values (16, 'Il primo bacio', 'Uno dei miei preferiti della giornata.', '/images/demo/matrimonio-04.jpg', 2);
insert into photo (id, title, description, image_url, event_id) values (17, 'Riso e coriandoli', null, '/images/demo/matrimonio-05.jpg', 2);
insert into photo (id, title, description, image_url, event_id) values (18, 'Il ritratto degli sposi', 'Luce naturale, nessun flash.', '/images/demo/matrimonio-06.jpg', 2);
insert into photo (id, title, description, image_url, event_id) values (19, 'Il brindisi', 'Scatto rubato durante la cerimonia.', '/images/demo/matrimonio-07.jpg', 2);
insert into photo (id, title, description, image_url, event_id) values (20, 'Il primo ballo', 'Uno dei miei preferiti della giornata.', '/images/demo/matrimonio-08.jpg', 2);
insert into photo (id, title, description, image_url, event_id) values (21, 'Le damigelle', 'Luce naturale, nessun flash.', '/images/demo/matrimonio-09.jpg', 2);
insert into photo (id, title, description, image_url, event_id) values (22, 'Il taglio della torta', 'Foto di gruppo: ideale per la stampa grande.', '/images/demo/matrimonio-10.jpg', 2);
insert into photo (id, title, description, image_url, event_id) values (23, 'Gli invitati in giardino', 'Luce naturale, nessun flash.', '/images/demo/matrimonio-11.jpg', 2);
insert into photo (id, title, description, image_url, event_id) values (24, 'L''ultimo scatto della sera', 'Foto di gruppo: ideale per la stampa grande.', '/images/demo/matrimonio-12.jpg', 2);
insert into photo (id, title, description, image_url, event_id) values (25, 'L''ingresso in chiesa', null, '/images/demo/comunione-01.jpg', 3);
insert into photo (id, title, description, image_url, event_id) values (26, 'La benedizione', null, '/images/demo/comunione-02.jpg', 3);
insert into photo (id, title, description, image_url, event_id) values (27, 'Il momento della comunione', null, '/images/demo/comunione-03.jpg', 3);
insert into photo (id, title, description, image_url, event_id) values (28, 'Con mamma e papa''', null, '/images/demo/comunione-04.jpg', 3);
insert into photo (id, title, description, image_url, event_id) values (29, 'Il gruppo dei bambini', null, '/images/demo/comunione-05.jpg', 3);
insert into photo (id, title, description, image_url, event_id) values (30, 'Il ritratto sull''altare', 'Uno dei miei preferiti della giornata.', '/images/demo/comunione-06.jpg', 3);
insert into photo (id, title, description, image_url, event_id) values (31, 'Con i nonni', 'Dettaglio in primo piano.', '/images/demo/comunione-07.jpg', 3);
insert into photo (id, title, description, image_url, event_id) values (32, 'Il pranzo in famiglia', 'Scatto rubato durante la cerimonia.', '/images/demo/comunione-08.jpg', 3);
insert into photo (id, title, description, image_url, event_id) values (33, 'L''ingresso in chiesa', 'Luce naturale, nessun flash.', '/images/demo/comunione-01.jpg', 4);
insert into photo (id, title, description, image_url, event_id) values (34, 'La benedizione', 'Uno dei miei preferiti della giornata.', '/images/demo/comunione-02.jpg', 4);
insert into photo (id, title, description, image_url, event_id) values (35, 'Il momento della comunione', null, '/images/demo/comunione-03.jpg', 4);
insert into photo (id, title, description, image_url, event_id) values (36, 'Con mamma e papa''', 'Ritratto posato, formato orizzontale.', '/images/demo/comunione-04.jpg', 4);
insert into photo (id, title, description, image_url, event_id) values (37, 'Il gruppo dei bambini', 'Foto di gruppo: ideale per la stampa grande.', '/images/demo/comunione-05.jpg', 4);
insert into photo (id, title, description, image_url, event_id) values (38, 'Il ritratto sull''altare', null, '/images/demo/comunione-06.jpg', 4);
insert into photo (id, title, description, image_url, event_id) values (39, 'Con i nonni', 'Foto di gruppo: ideale per la stampa grande.', '/images/demo/comunione-07.jpg', 4);
insert into photo (id, title, description, image_url, event_id) values (40, 'Il pranzo in famiglia', null, '/images/demo/comunione-08.jpg', 4);
insert into photo (id, title, description, image_url, event_id) values (41, 'L''attesa fuori dalla cattedrale', null, '/images/demo/cresima-01.jpg', 5);
insert into photo (id, title, description, image_url, event_id) values (42, 'Con il padrino', 'Dettaglio in primo piano.', '/images/demo/cresima-02.jpg', 5);
insert into photo (id, title, description, image_url, event_id) values (43, 'L''unzione', null, '/images/demo/cresima-03.jpg', 5);
insert into photo (id, title, description, image_url, event_id) values (44, 'Il ritratto', 'Ritratto posato, formato orizzontale.', '/images/demo/cresima-04.jpg', 5);
insert into photo (id, title, description, image_url, event_id) values (45, 'La famiglia riunita', 'Dettaglio in primo piano.', '/images/demo/cresima-05.jpg', 5);
insert into photo (id, title, description, image_url, event_id) values (46, 'In agriturismo', 'Luce naturale, nessun flash.', '/images/demo/cresima-06.jpg', 5);
insert into photo (id, title, description, image_url, event_id) values (47, 'L''arrivo a Trastevere', 'Foto di gruppo: ideale per la stampa grande.', '/images/demo/battesimo-01.jpg', 6);
insert into photo (id, title, description, image_url, event_id) values (48, 'Il fonte battesimale', 'Uno dei miei preferiti della giornata.', '/images/demo/battesimo-02.jpg', 6);
insert into photo (id, title, description, image_url, event_id) values (49, 'Con i genitori', 'Dettaglio in primo piano.', '/images/demo/battesimo-03.jpg', 6);
insert into photo (id, title, description, image_url, event_id) values (50, 'La candela', 'Scatto rubato durante la cerimonia.', '/images/demo/battesimo-04.jpg', 6);
insert into photo (id, title, description, image_url, event_id) values (51, 'Il chiostro', 'Ritratto posato, formato orizzontale.', '/images/demo/battesimo-05.jpg', 6);
insert into photo (id, title, description, image_url, event_id) values (52, 'Con i nonni', 'Foto di gruppo: ideale per la stampa grande.', '/images/demo/battesimo-06.jpg', 6);

-- copertine (la foto piu' rappresentativa di ogni evento)
update event set cover_id = 2 where id = 1;
update event set cover_id = 14 where id = 2;
update event set cover_id = 26 where id = 3;
update event set cover_id = 34 where id = 4;
update event set cover_id = 42 where id = 5;
update event set cover_id = 48 where id = 6;

-- utenti: mario e' il fotografo (ADMIN, password mario123); gli altri sono clienti (password 'password')
insert into users (id, name, surname, email) values (1, 'Mario', 'Rossi', 'mario@mariofotografo.it');
insert into users (id, name, surname, email) values (2, 'Giulia', 'Bianchi', 'giulia.bianchi@example.com');
insert into users (id, name, surname, email) values (3, 'Luca', 'Verdi', 'luca.verdi@example.com');
insert into users (id, name, surname, email) values (4, 'Sara', 'Neri', 'sara.neri@example.com');
insert into users (id, name, surname, email) values (5, 'Anna', 'Conti', 'anna.conti@example.com');
insert into credentials (id, username, password, role, user_id) values (1, 'mario', '$2b$10$veBi3A.I6RtB3G2F.Z4n2ORXD/Nna9Hxzt0FiSzBKuxQpjGcyBAZ2', 'ADMIN', 1);
insert into credentials (id, username, password, role, user_id) values (2, 'giulia', '$2b$10$e.bVlpGeNRhHh9NN18JlVe11tLYUn7KrPTNP2NgtfpOkOURcZXVDO', 'USER', 2);
insert into credentials (id, username, password, role, user_id) values (3, 'luca', '$2b$10$e.bVlpGeNRhHh9NN18JlVe11tLYUn7KrPTNP2NgtfpOkOURcZXVDO', 'USER', 3);
insert into credentials (id, username, password, role, user_id) values (4, 'sara', '$2b$10$e.bVlpGeNRhHh9NN18JlVe11tLYUn7KrPTNP2NgtfpOkOURcZXVDO', 'USER', 4);
insert into credentials (id, username, password, role, user_id) values (5, 'anna', '$2b$10$e.bVlpGeNRhHh9NN18JlVe11tLYUn7KrPTNP2NgtfpOkOURcZXVDO', 'USER', 5);

-- ordini: un ordine puo' contenere piu' pacchetti (vedi purchase_event)
insert into purchase (id, purchase_date, status, total, user_id) values (1, '2026-06-20 10:15:00', 'COMPLETED', 180.00, 2);
insert into purchase (id, purchase_date, status, total, user_id) values (2, '2026-06-25 18:40:00', 'PENDING', 480.00, 3);
insert into purchase (id, purchase_date, status, total, user_id) values (3, '2026-07-02 09:05:00', 'CANCELLED', 160.00, 4);
insert into purchase (id, purchase_date, status, total, user_id) values (4, '2026-07-10 21:30:00', 'PENDING', 320.00, 3);

insert into purchase_event (purchase_id, event_id) values (1, 3);
insert into purchase_event (purchase_id, event_id) values (2, 1);
insert into purchase_event (purchase_id, event_id) values (3, 5);
insert into purchase_event (purchase_id, event_id) values (4, 4);
insert into purchase_event (purchase_id, event_id) values (4, 6);

-- le sequenze usate da Hibernate ripartono da 1000
alter sequence event_seq restart with 1000;
alter sequence photo_seq restart with 1000;
alter sequence purchase_seq restart with 1000;
alter sequence users_seq restart with 1000;
alter sequence credentials_seq restart with 1000;
