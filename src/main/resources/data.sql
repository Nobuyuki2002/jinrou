-- INSERT INTO rooms (settingId, roomName, roomPass, roopCount,wolfNum, winner, isActive) VALUES (1, 'PUBLIC', 123, -1, 0, 0, true);

INSERT INTO settings ( startCount ) VALUES (4);
-- INSERT INTO users (room,pname,roles,isdeath) VALUES (1,NULL,1,true);
-- INSERT INTO users (room,pname,roles,isdeath) VALUES (1,NULL,1,true);
-- INSERT INTO users (room,pname,roles,isdeath) VALUES (1,NULL,1,true);
-- INSERT INTO users (room,pname,roles,isdeath) VALUES (1,NULL,2,true);

INSERT INTO roles ( rolName, isVillager ) VALUES ( 'VILLAGER', 1 );
INSERT INTO roles ( rolName, isVillager ) VALUES ( 'WOLF', 2 );
INSERT INTO roles ( rolName, isVillager ) VALUES ( 'USER', 0 );
