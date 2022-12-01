CREATE TABLE Groups
(
    idGroup SERIAL PRIMARY KEY,
    faculty TEXT,
    course INT,
    name TEXT UNIQUE,
    head TEXT
);

CREATE TABLE Students
(
    idStudent SERIAL PRIMARY KEY,
    idGroup INT,
    name TEXT,
    CONSTRAINT fk_group
      FOREIGN KEY(idGroup)
	    REFERENCES Groups(idGroup)
);

CREATE OR REPLACE PROCEDURE insert_data(a text, b text)
LANGUAGE SQL
AS $$
    INSERT INTO groups (name)
        VALUES (a);
    INSERT INTO groups (name)
        VALUES (b);
$$;

INSERT INTO Groups (name)
    VALUES ('g1');

CREATE OR REPLACE FUNCTION increment_course(gID integer)
RETURNS integer AS $$
BEGIN
    UPDATE groups SET faculty = faculty + 1 WHERE idGroup = gID;
END;
$$ LANGUAGE plpgsql;