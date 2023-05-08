drop table if exists credit_card;

create table credit_card(
  card_no varchar(16) unique not null,
  prc_id  varchar(18) not null,
  opening_date  date not null,
  balance DECIMAL(18,6) not null COMMENT '这个月欠的钱',
  unpaid_min_repayment DECIMAL(18,6) not null COMMENT '前期最低还款额',
  interest_amount DECIMAL(18,6) not null,
  cash_advance  DECIMAL(18,6) not null COMMENT '预借金额',
  quota DECIMAL(18,6) not null,
  pin_num varchar(4) not null,
  last_bill_date date not null,
  late_fee DECIMAL(18,6) NOT null COMMENT '滞纳金',
  version int not null default 0 COMMENT '乐观锁',
  PRIMARY KEY(card_no),
  INDEX idx_prc_id (prc_id)
);

INSERT INTO bank_application.credit_card (card_no, prc_id, opening_date, balance, unpaid_min_repayment,interest_amount, cash_advance, quota, last_bill_date) VALUES ('3223', '123456789012345678', '2023-04-13', -231.232300,0, 91.100000, 200.443000, 0.555500, '2023-05-01');
