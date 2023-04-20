drop table if exists loan_date;

create table loan_date(
  loan_id int not null,
  loan_start_date DATE not null,
  loan_end_date DATE not null,
  interest_free_date DATE not null COMMENT '免息时间',
  PRIMARY KEY(loan_id)
)