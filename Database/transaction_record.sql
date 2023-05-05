drop table if exists transaction_record;

create table transaction_record (
  transaction_id int not null AUTO_INCREMENT,
  encoded_transaction varchar(255),
  transaction_date  date,
  transaction_type  varchar(10),
  transaction_result varchar(255),
  PRIMARY KEY(transaction_id)
)