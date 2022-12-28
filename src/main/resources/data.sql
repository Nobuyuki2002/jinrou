-- INSERT INTO rooms (settingId, roomName, roomPass, roopCount,wolfNum, winner, isActive) VALUES (1, 'PUBLIC', 123, -1, 0, 0, true);

INSERT INTO settings ( startCount ) VALUES (4);
-- INSERT INTO users (room,pname,roles,isdeath) VALUES (1,NULL,1,true);
-- INSERT INTO users (room,pname,roles,isdeath) VALUES (1,NULL,1,true);
-- INSERT INTO users (room,pname,roles,isdeath) VALUES (1,NULL,1,true);
-- INSERT INTO users (room,pname,roles,isdeath) VALUES (1,NULL,2,true);

INSERT INTO roles ( rolName, isVillager ) VALUES ( 'VILLAGER', 1 );
INSERT INTO roles ( rolName, isVillager ) VALUES ( 'WOLF', 2 );
INSERT INTO roles ( rolName, isVillager ) VALUES ( 'USER', 0 );
INSERT INTO roles ( rolName, isVillager ) VALUES ( 'DIVINER', 4 );

INSERT INTO loginUsers ( username, passwd, authorities ) VALUES ( 'user1', '$2y$10$86DzBg8hq6Ml4/v9yi6d7OIofNZW5tzOLSAXivVgkfkM7obVddvxe', 'ROLE_USER' );
INSERT INTO loginUsers ( username, passwd, authorities ) VALUES ( 'user2', '$2y$10$iqpjkTb1y68osRlQyCEO0.wC.//7u0X5Dknit1UlxkEmHzbYC8unm', 'ROLE_USER' );
INSERT INTO loginUsers ( username, passwd, authorities ) VALUES ( 'user3', '$2y$10$jP9DogZEoPUeLCBbW3Dkgeqj2Vav.8ZyaAQ2rBU6X1XD6rr2ZvBgm', 'ROLE_USER' );
INSERT INTO loginUsers ( username, passwd, authorities ) VALUES ( 'user4', '$2y$10$NSVV834mL49wuMi9U9jruOGIMkZyVusmFrXpVbjHCFDzoL34/fAy.', 'ROLE_USER' );
INSERT INTO loginUsers ( username, passwd, authorities ) VALUES ( 'user5', '$2y$10$558FfrY45/XzoldSHiE21eSHMyC2BcnNzeUMcGHDzfqnkY5nJs8xa', 'ROLE_USER' );
INSERT INTO loginUsers ( username, passwd, authorities ) VALUES ( 'user6', '$2y$10$XKbHjR0PpHzZp9sRO2DXQuOZ6LpzDtN1ENen3ytGfX63dBmaEzPn2', 'ROLE_USER' );
INSERT INTO loginUsers ( username, passwd, authorities ) VALUES ( 'user7', '$2y$10$NJSQ6ZAOXrzU/AGccRJbquO4AF6XMlpgN8CjCU9iiEFbwHToEPz6i', 'ROLE_USER' );
INSERT INTO loginUsers ( username, passwd, authorities ) VALUES ( 'user8', '$2y$10$rUnFGKcFOIKP7RlcmCwVoeKAs37O2t.Vc8.yz3xNaYcH3sywyolS2', 'ROLE_USER' );
