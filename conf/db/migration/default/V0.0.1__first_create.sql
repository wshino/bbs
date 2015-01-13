create table board (
  id serial not null primary key,
  title varchar(255)
);

create table comment (
  id serial not null primary key,
  text text,
  board_id bigint,
  created_at DATETIME
);
