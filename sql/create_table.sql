-- auto-generated definition
create table user
(
    id            bigint auto_increment comment 'id'
        primary key,
    username      varchar(256)                       null comment '用户名',
    user_account  varchar(256)                       null comment '账号',
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
    constraint user_account
        unique (user_account)
);

