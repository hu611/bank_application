drop table if exists debit_plan;

create table debit_plan (
  plan_id int not null auto_increment,
  plan_title varchar(10) not null,
  plan_desc varchar(255) not null,
  freeze_amount int not null COMMENT '冻结资金一定是整数',
  interest_rate float not null,
  duration_month int not null,
  duration_year int not null,
  start_date date not null,
  expire_date date not null,
  PRIMARY KEY(plan_id)
)