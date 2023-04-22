drop table if exists daily_interest_amount_record;

create table daily_interest_amount_record(
  interest_amount decimal(18,6) not null,
  record_date date not null,
  prc_id varchar(18) not null,
  PRIMARY KEY (record_date,prc_id)
)