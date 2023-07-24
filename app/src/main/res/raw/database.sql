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