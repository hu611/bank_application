drop table if exists credit_card_bill_payback_record;

/*
<-- 还钱记录 ->
*/
create table credit_card_bill_payback_record(
  record_id int not null AUTO_INCREMENT,
  bill_id int not null,
  payback_amount decimal not null,
  payback_date  date not null,
  PRIMARY KEY(record_id)
);