drop table if exists late_fee;

create table late_fee(
  late_fee_id int not null auto_increment,
  prc_id varchar(18) not null,
  late_date date not null,
  late_fee_amount DECIMAL not null,
  PRIMARY KEY(late_fee_id)
)