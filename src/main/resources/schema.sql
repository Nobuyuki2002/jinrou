CREATE TABLE users (
    id IDENTITY,
    room INT NOT NULL,
    pname VARCHAR NOT NULL,
    roles INT NOT NULL,
    isdeath boolean
);