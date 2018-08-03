create table order_item(
	id bigint(20) primary key AUTO_INCREMENT,
	product_id bigint(20) not null,
	order_id bigint(20) not null,
	count int(20) not null
)charset utf8;