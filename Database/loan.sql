drop table if exists loan;

create table loan(
  loan_id int not null AUTO_INCREMENT,
  loan_type int not null COMMENT '0:car, 1:house, 2:student',
  prc_id  varchar(18) not null,
  payback_total decimal not null,
  monthly_payback decimal not null,
  interest_rate decimal not null,
  current_owe decimal not null COMMENT 'how much does the person currently owe',
  loan_start_date DATE not null,
  loan_end_date DATE not null,
  PRIMARY KEY(loan_id),
  INDEX idx_prc_id (prc_id)
)