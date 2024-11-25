CREATE SCHEMA SE2224_20070006009;
USE SE2224_20070006009;

create table tasks (
id INT not null AUTO_INCREMENT,
task_name varchar(255) not null,
short_description varchar(255) not null,
priority INT,
deadline DATE not null,
reminder_image BOOLEAN not null,
entry_date DATE not null,
primary key (id)
);