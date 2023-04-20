drop table if exists credit_card_bill;

create table credit_card_bill(
  bill_id int not null auto_increment,
  owe_date  date not null,
  owe_amount  DECIMAL not null,
  bill_total  DECIMAL not null,
  prc_id  varchar(18) not null,
  bill_name   varchar(255) not null,
  paid  int not null COMMENT '1: paid 2: unpaid',
  PRIMARY KEY(bill_id),
  INDEX idx_prc_id(prc_id),
  INDEX idx_paid(paid)
)