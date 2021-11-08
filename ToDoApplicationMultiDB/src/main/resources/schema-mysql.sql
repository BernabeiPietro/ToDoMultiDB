
create table if not exists hibernate_sequence (next_val bigint) engine=MyISAM
insert into hibernate_sequence values ( 1 )
create table if not exists to_do (id bigint not null auto_increment, local_date_time TIMESTAMP, id_user bigint, primary key (id)) engine=MyISAM
create table if not exists todo_mapping (todo_map_id bigint not null, doit bit, todo_action varchar(255) not null, primary key (todo_map_id,todo_action)) engine=MyISAM
create table if not exists user (id bigint not null, email varchar(255), name varchar(255), primary key (id)) engine=MyISAM
alter table to_do add constraint FK48b17pwgdat3fcjnk29600xxc foreign key (id_user) references user (id)
alter table todo_mapping add constraint FK8b4lrb0tvjeqtlntg3cd76eou foreign key (todo_map_id) references to_do (id)
