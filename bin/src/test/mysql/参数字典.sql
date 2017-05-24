drop index code on cm_dict;

drop table if exists cm_dict;

/*==============================================================*/
/* Table: cm_dict                                               */
/*==============================================================*/
create table cm_dict
(
   id                   varchar(36) not null,
   code                 varchar(30) not null,
   name                 varchar(50) not null,
   type                 varchar(2) not null,
   status               varchar(2) not null,
   note                 varchar(256),
   primary key (id),
   key AK_cm_dict_key (code)
)
comment = "参数字典类型定义表";

/*==============================================================*/
/* Index: code                                                  */
/*==============================================================*/
create unique index code on cm_dict
(
   code
);

drop table if exists cm_dict_line;

/*==============================================================*/
/* Table: cm_dict_line                                          */
/*==============================================================*/
create table cm_dict_line
(
   id                   varchar(36) not null,
   code                 varchar(30),
   dict_id              varchar(36),
   dict_line_id         varchar(36) not null,
   name                 varchar(40) not null,
   value                varchar(40) not null,
   seq                  int,
   path                 varchar(36),
   layer                int,
   detail               char(1) default '1' comment '0、否 1、是',
   status               varchar(2),
   note                 varchar(256),
   primary key (id)
)
comment = "参数字典数据项定义表";
