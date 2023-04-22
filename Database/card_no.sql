drop table if exists card_info;

/*
Debit Card Only
*/
create table card_info(
  card_no varchar(16) not null,
  prc_id varchar(18) not null,
  opening_date date not null,
  balance DECIMAL(18,6) not null,
  card_type char not null,
  pin_num varchar(4) not null,
  PRIMARY KEY(card_no)
)
