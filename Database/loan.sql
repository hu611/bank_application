drop table if exists loan;

create table loan(
  loan_id int not null AUTO_INCREMENT,
  loan_type char(1) not null COMMENT 'STUDENT LOAN = 0, Car loan = 1, house loan = 2',
  prc_id  varchar(18) not null,
  payback_total decimal not null,
  monthly_payback decimal not null,
  interest_rate decimal not null,
  current_owe decimal not null COMMENT 'how much does the person currently owe',
  PRIMARY KEY(loan_id)
)