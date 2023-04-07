drop table if exists user_notification;

create table user_notification (
  prc_id varchar(18) not null,
  email   varchar(18),
  cell_phone  varchar(18),
  PRIMARY KEY(prc_id)
);