DROP TABLE IF EXISTS todo;

CREATE TABLE todo (
    `id` bigint(20) primary key auto_increment,
    `title` varchar(250),
    `order` int default null,
    `completed` bool default false
)