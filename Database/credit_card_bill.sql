drop table if exists credit_card_bill;

create table credit_card_bill(
  bill_id int not null auto_increment,
  owe_date  date not null,
  owe_amount  DECIMAL not null,
  bill_total  DECIMAL not null,
  prc_id  varchar(18) not null,
  bill_name   varchar(255) not null,
  paid  int not null COMMENT '1: paid 2: unpaid',
  PRIMARY KEY(bill_id),
  INDEX idx_prc_id(prc_id),
  INDEX idx_paid(paid)
);

INSERT INTO credit_card_bill (owe_date, owe_amount, bill_total, prc_id, bill_name, paid)
VALUES
('2023-04-01', 1000, 2000, '123456789012345678', 'April Bill', 1),
('2023-03-01', 500, 1500, '123456789012345678', 'March Bill', 1),
('2023-02-01', 800, 1800, '123456789012345678', 'February Bill', 2),
('2023-01-01', 900, 1900, '123456789012345678', 'January Bill', 2);