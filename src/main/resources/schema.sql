CREATE TABLE users (
    id IDENTITY,
    room INT NOT NULL,
    pname VARCHAR,
    roles INT NOT NULL,
    isdeath boolean,
    vote INT
);

CREATE TABLE rolles (
    roll_id IDENTITY,
    roll_name VARCHAR NOT NULL,
    isVillager boolean
);

CREATE TABLE room (
    room_id IDENTITY,
    settng_id INT NOT NULL,
    room_name VARCHAR NOT NULL,
    room_pass INT NOT NULL,
    roop_count INT NOT NULL,
    winner boolean,
    isActive boolean
);
