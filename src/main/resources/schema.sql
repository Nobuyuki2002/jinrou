CREATE TABLE users (
    id IDENTITY,
    lname VARCHAR,
    pname VARCHAR,
    room INT NOT NULL,
    roles INT NOT NULL,
    isDeath BOOLEAN,
    isDivined BOOLEAN,
    jobVote INT,
    killVote INT
);

CREATE TABLE roles (
    rolId IDENTITY,
    rolName VARCHAR NOT NULL,
    isVillager INT NOT NULL
);

CREATE TABLE rooms (
    roomId IDENTITY,
    settingId INT NOT NULL,
    roomName VARCHAR NOT NULL,
    roomPass VARCHAR NOT NULL,
    roopCount INT NOT NULL,
    wolfNum INT,
    divinerNum INT,
    winner INT NOT NULL,
    isActive BOOLEAN
);

CREATE TABLE settings (
    settingId IDENTITY,
    startCount INT NOT NULL
);

/**
 * Spring security user table
***/
CREATE TABLE loginUsers (
  userId IDENTITY,
  username VARCHAR NOT NULL,
  passwd VARCHAR NOT NULL,
  authorities VARCHAR
);
