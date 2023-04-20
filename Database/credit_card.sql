drop table if exists credit_card;

create table credit_card(
  card_no varchar(16) unique not null,
  prc_id  varchar(18) not null,
  opening_date  date not null,
  balance DECIMAL not null,
  cash_advance  DECIMAL not null COMMENT '预借金额',
  quota DECIMAL not null,
  PRIMARY KEY(card_no),
  INDEX idx_prc_id (prc_id)
)