drop table if exists kafka_offset;

create table kafka_offset(
  partition_id int not null,
  topic varchar(25) not null,
  offset int not null,
  primary key(partition_id,topic)
)