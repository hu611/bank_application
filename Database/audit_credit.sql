drop table if exists audit_credit;
create table audit_credit(
  audit_credit_id int not null auto_increment,
  prc_id varchar(18) not null,
  pic_folder_loc  varchar(100) not null,
  pin_num varchar(4) not null,
  primary key(audit_credit_id)
)