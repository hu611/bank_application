drop table if exists api_key;

create table api_key(
  prc_id varchar(18) not null,
  api_key varchar(30) not null,
  last_update date not null,
  PRIMARY KEY(api_key)
)