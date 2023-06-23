-- auto-generated definition
create table user
(
    id            bigint auto_increment comment 'id'
        primary key,
    username      varchar(256)                       null comment '用户名',
    user_account  varchar(256)                       null comment '账号',
    profile       varchar(512)                       null comment '个人简介',
    avatar_url    varchar(1024)                      null comment '头像url',
    gender        tinyint                            null comment '性别',
    user_password varchar(256)                       null comment '密码',
    phone         varchar(128)                       null comment '手机号码',
    email         varchar(512)                       null comment '邮箱',
    user_status   int      default 0                 not null comment '0-正常,1-异常',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    is_deleted    tinyint  default 0                 not null comment '逻辑删除',
    user_role     int      default 0                 not null comment '权限字段
0 - 普通用户
1 - 管理员',
    planet_code   varchar(512)                       null comment '星球编号',
    tags          varchar(1024)                      null comment '标签json列表',
    constraint user_account
        unique (user_account)
) comment '用户表';

create table team
(
    id            bigint primary key auto_increment comment 'id',
    `name`        varchar(256)                       null comment '队伍名',
    `description` varchar(512),
    max_num        int                                null default 1 comment '队伍最大人数',
    expire_time   datetime                           null comment '队伍过期时间',
    `status`      int      default 0                 null comment '0-公开 1-私密 2-加密',
    `password`    varchar(256)                       null comment '密码',
    user_id       bigint                             null comment '用户id(队长Id)',
    `create_time` datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    is_deleted    tinyint  default 0                 not null comment '逻辑删除'
) comment '队伍表';

create table user_team
(
    id            bigint primary key auto_increment comment 'id',
    team_id       bigint comment '队伍id',
    user_id       bigint comment '用户id(队长Id)',
    join_time     datetime comment '加入时间',
    `create_time` datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    is_deleted    tinyint  default 0                 not null comment '逻辑删除'
) comment '用户_队伍表';


# 将所有的用户密码设置为12345678
update user
set user_password = 'd0dc7ac2309a402ec77a9be6f528c09c';

select * from team;

select count(*)
from user;

use user_center;
alter table user
    add column tags varchar(1024) null comment '标签列表'