-- liquibase formatted sql

-- changeset kelvin:202403080836

CREATE TABLE `app_role`
(
    `id`   int PRIMARY KEY AUTO_INCREMENT,
    `name` varchar(45) NOT NULL UNIQUE
) engine = innodb;

CREATE TABLE `app_user`
(
    `id`          bigint PRIMARY KEY AUTO_INCREMENT,
    `email`       varchar(120) NOT NULL UNIQUE,
    `password`    varchar(255) NOT NULL,
    `name`        varchar(120),
    `status`      varchar(10)  NOT NULL,
    `app_role_id` int          not null,
    `created_at`  datetime     default CURRENT_TIMESTAMP,
    `updated_at`  datetime,
    FOREIGN KEY (app_role_id) REFERENCES app_role(id)
) engine = INNODB;


CREATE TABLE `lunch_plan`
(
    `id`           bigint PRIMARY KEY AUTO_INCREMENT,
    `uuid`         varchar(40) NOT NULL,
    `initiated_by` bigint      NOT NULL,
    `date`    date    NOT NULL,
    `description`   varchar(255) NOT NULL,
    `ended` tinyint(1) default 0,
    `created_at`   datetime    default CURRENT_TIMESTAMP,
    FOREIGN KEY (initiated_by) REFERENCES app_user(id),
    INDEX(uuid)
) engine = INNODB;


CREATE TABLE `lunch_plan_suggestion`
(
    `id`              bigint PRIMARY KEY AUTO_INCREMENT,
    `lunch_plan_id`      bigint      NOT NULL,
    `restaurant_name` varchar(40) not null,
    `suggested_by`    varchar(40)      NOT NULL,
    FOREIGN KEY (lunch_plan_id) REFERENCES lunch_plan(id)

) engine = INNODB;

