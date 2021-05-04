CREATE TABLE shop_product_commission (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	shop_id BIGINT(20),
    product_id BIGINT(20),
    amount DECIMAL(20,6),
    percentage DECIMAL(20,6),

    CONSTRAINT fk_shop_product_commission_shop_id FOREIGN KEY (shop_id) REFERENCES shop(id),
    CONSTRAINT fk_shop_product_commission_product_id FOREIGN KEY (product_id) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;