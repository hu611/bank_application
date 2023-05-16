drop table if exists api_request_record;

create table api_request_record(
  record_id int not null auto_increment,
  prc_id varchar(18) not null,
  company_name varchar(40) not null,
  company_desc varchar(255) not null,
  start_date date not null,
  result varchar(20) not null,
  PRIMARY KEY(record_id)
)