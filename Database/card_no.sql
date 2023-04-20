drop table if exists card_info;

create table card_info COMMENT 'Debit card only database'(
  card_no varchar(16) not null,
  prc_id varchar(18) not null,
  opening_date date not null,
  balance DECIMAL not null,
  card_type char not null,
  pin_num varchar(4) not null,
  PRIMARY KEY(card_no)
)
