drop table if exists loan_bill;

create table loan_bill(
  loan_id int not null,
  owe_date  date not null,
  owe_amount  DECIMAL not null,
  bill_total  DECIMAL not null,
  prc_id  varchar(18) not null,
  bill_name   varchar(255) not null,
  paid  int not null COMMENT '1: paid 2: unpaid',
  PRIMARY KEY(loan_id),
  INDEX idx_prc_id(prc_id),
  INDEX idx_paid(paid)
)