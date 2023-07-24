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
INSERT OR IGNORE INTO terms(code, title) VALUES ("21F", "2021 Fall");
INSERT OR IGNORE INTO terms(code, title) VALUES ("22W", "2022 Winter");
INSERT OR IGNORE INTO terms(code, title) VALUES ("22F", "2022 Fall");
INSERT OR IGNORE INTO terms(code, title) VALUES ("23W", "2023 Winter");
INSERT OR IGNORE INTO terms(code, title) VALUES ("23SP", "2023 Spring");
INSERT OR IGNORE INTO terms(code, title) VALUES ("23F", "2023 Fall");
INSERT OR IGNORE INTO terms(code, title) VALUES ("24W", "2024 Winter");
INSERT OR IGNORE INTO terms(code, title) VALUES ("24SP", "2024 Spring");
INSERT OR IGNORE INTO courses(crn, term, code, title, instructor) VALUES (4059, "21F", "CP312", "Algorithm Analysis/Design I", "Dr. Angele Foley");
INSERT OR IGNORE INTO courses(crn, term, code, title, instructor) VALUES (3523, "22W", "CP264", "Data Structures II", "Dr. Hongbing Fan");
INSERT OR IGNORE INTO courses(crn, term, code, title, instructor) VALUES (3312, "22F", "CP601", "Sem. in Tech. Entrepreneurship", "Christina Sookram");
INSERT OR IGNORE INTO courses(crn, term, code, title, exclusions, instructor) VALUES (2203, "23W", "CP669", "iOS Application Programming", "CP469", "Dr. Chinh Hoang");
INSERT OR IGNORE INTO courses(crn, term, code, title, exclusions, instructor) VALUES (748, "23SP", "CP670", "Android Appl. Programming", "CP470", "Dr. Abdul-Rahman Malwood-Yunis");
INSERT OR IGNORE INTO courses(crn, term, code, title, instructor) VALUES (2654, "23F", "CP630", "Enterprise Computing", "Dr. Hongbing Fan");
INSERT OR IGNORE INTO courses(crn, term, code, title, instructor) VALUES (2417, "24W", "CP600", "Practical Algorithm Design", "Dr. Masoomeh Rudafshani");
INSERT OR IGNORE INTO courses(crn, term, code, title) VALUES (742, "24SP", "CP631", "Advanced Parallel Programming");
INSERT OR IGNORE INTO registrations(crn, term, student, grade) VALUES(4059, "21F", "cons3250", "A+");
INSERT OR IGNORE INTO registrations(crn, term, student, grade) VALUES(3523, "22W", "cons3250", "A+");
INSERT OR IGNORE INTO registrations(crn, term, student, grade) VALUES(3312, "22F", "cons3250", "A+");
INSERT OR IGNORE INTO registrations(crn, term, student, grade) VALUES(2203, "23W", "cons3250", "A-");
INSERT OR IGNORE INTO registrations(crn, term, student) VALUES(748, "23SP", "cons3250");
INSERT OR IGNORE INTO registrations(crn, term, student) VALUES(2654, "23F", "cons3250");
INSERT OR IGNORE INTO registrations(crn, term, student) VALUES(2417, "24W", "cons3250");
INSERT OR IGNORE INTO registrations(crn, term, student) VALUES(742, "24SP", "cons3250");