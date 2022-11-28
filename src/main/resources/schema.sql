CREATE TABLE users (
    id IDENTITY,
    lname VARCHAR NOT NULL,
    pname VARCHAR,
    room INT NOT NULL,
    roles INT NOT NULL,
    isDeath BOOLEAN,
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
    roomPass INT NOT NULL,
    roopCount INT NOT NULL,
    wolfNum INT,
    winner INT NOT NULL,
    isActive BOOLEAN
);

CREATE TABLE settings (
    settingId IDENTITY,
    startCount INT NOT NULL
);
