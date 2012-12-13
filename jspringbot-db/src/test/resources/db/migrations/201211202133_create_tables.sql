create table account (
  id bigint not null auto_increment,
  created_by varchar(64) not null,
  created_time datetime not null,
  last_updated_by varchar(64) not null,
  last_updated_time datetime,
  version int default 0,
  description text,
  name varchar(50) not null unique,
  status varchar(10) not null default 'IN_ACTIVE',
  primary key (id));
