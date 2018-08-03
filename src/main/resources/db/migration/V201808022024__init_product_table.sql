create table product(
	id bigint(20) PRIMARY KEY AUTO_INCREMENT,
	name varchar(50) not null,
	image_url varchar(100) not null,
	unit varchar(50) not null,
	price double not null
)charset utf8;