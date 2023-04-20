drop table if exists house_loan;

create table house_loan(
  loan_id int not null AUTO_INCREMENT,
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