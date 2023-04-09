drop table if exists bank_transaction;

create table bank_deposit(
  transaction_id int not null AUTO_INCREMENT,
  card_no varchar(16) not null,
  transaction_date date,
  transaction_amount DECIMAL,
  PRIMARY KEY(transaction_id)
)