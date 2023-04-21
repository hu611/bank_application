/**
This is to get credit card for a user for each month
*/

drop table if exists credit_card_checkpoint;

create table credit_card_checkpoint (
  checkpoint_id int not null auto_increment,
  card_no varchar(16) unique not null,
  prc_id varchar(18) not null,
  interest_amount decimal not null,
  checkpoint_date date not null,
  balance DECIMAL not null,
  quota DECIMAL not null,
  PRIMARY KEY(checkpoint_id)
)