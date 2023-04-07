drop table if exists bank_user;

create table bank_user (
  prc_id varchar(18) not null,
  username varchar(18) not null,
  realname varchar(18) not null,
  salt  varchar(4) not null,
  gender bit not null,
  create_date date,
  last_update date,
  birthday  date,
  user_pic  varchar(255),
  primary key(prc_id)
);

INSERT INTO bank_user (prc_id, username, realname, salt, gender, cell_phone, create_date, last_update, birthday, user_pic)
VALUES ('320202020202', 'ad23', 'weiyanhu', '1234', 0, '13750008534', '2022-01-03', '2022-01-03', '2022-01-03', 'lpl');

INSERT INTO bank_user (prc_id, username, realname, salt, gender, cell_phone, create_date, last_update, birthday, user_pic)
VALUES ('320202020203', 'ad24', 'xiaoming', '5678', 1, '13750008535', '2022-02-04', '2022-02-04', '1999-01-01', 'sunny');

INSERT INTO bank_user (prc_id, username, realname, salt, gender, cell_phone, create_date, last_update, birthday, user_pic)
VALUES ('320202020204', 'ad25', 'xiaohong', '9101', 0, '13750008536', '2022-03-05', '2022-03-05', '2000-02-02', 'rainy');

