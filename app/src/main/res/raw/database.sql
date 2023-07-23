CREATE TABLE IF NOT EXISTS terms (
	code TEXT NOT NULL PRIMARY KEY,
	title TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS courses (
	crn INT NOT NULL,
	term TEXT NOT NULL,
	code TEXT NOT NULL,
	title TEXT NOT NULL,
	prerequisites TEXT,
	exclusions TEXT,
	instructor TEXT,
	daytime TEXT,
	PRIMARY KEY(crn, term),
	FOREIGN KEY(term) REFERENCES terms(code)
);

CREATE TABLE IF NOT EXISTS users (
	username TEXT NOT NULL PRIMARY KEY,
	last TEXT NOT NULL,
	first TEXT NOT NULL,
	userType TEXT NOT NULL,
	password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS registrations (
	crn INT NOT NULL,
	term TEXT NOT NULL,
	student TEXT NOT NULL,
	grade TEXT,
	PRIMARY KEY(crn, term, student),
	FOREIGN KEY(crn) REFERENCES courses(crn),
	FOREIGN KEY(term) REFERENCES terms(code),
	FOREIGN KEY(student) REFERENCES users(username)
);

INSERT OR IGNORE INTO users (username, last, first, userType, password) VALUES ("wluadmin", "Administrator", "WLU", "admin", "WLUAdmin!");
INSERT OR IGNORE INTO users (username, last, first, userType, password) VALUES ("cons3250", "Console", "Johnny", "student", "WLUStudent!");
INSERT OR IGNORE INTO terms(code, title) VALUES ("23SP", "2023 Spring");
INSERT OR IGNORE INTO courses(crn, term, code, title, exclusions, instructor) VALUES (12345, "23SP", "CP670", "Android Appl. Programming", "CP470", "Dr. Abdul-Rahman Malwood-Yunis");
INSERT OR IGNORE INTO registrations(crn, term, student) VALUES(12345, "23SP", "cons3250");