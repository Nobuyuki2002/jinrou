INSERT INTO rooms (settingId, roomName, roomPass, roopCount, winner, isActive) VALUES (1,'PUBLIC',123,0, 0,false);

INSERT INTO settings ( startCount ) VALUES (2);
-- INSERT INTO users (room,pname,roles,isdeath) VALUES (1,NULL,1,true);
-- INSERT INTO users (room,pname,roles,isdeath) VALUES (1,NULL,1,true);
-- INSERT INTO users (room,pname,roles,isdeath) VALUES (1,NULL,1,true);
-- INSERT INTO users (room,pname,roles,isdeath) VALUES (1,NULL,2,true);

INSERT INTO roles ( rolName, isVillager ) VALUES ( 'USER', 0 );
INSERT INTO roles ( rolName, isVillager ) VALUES ( 'VILLAGER', 1 );
INSERT INTO roles ( rolName, isVillager ) VALUES ( 'WOLF', 2 );
