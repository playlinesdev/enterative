CREATE TABLE shop_transaction (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	amount DECIMAL(20,6),
	type INT(4),
    status INT(4),
    transaction_date TIMESTAMP,
    web_user_id BIGINT(20),
    sale_order_line_id BIGINT(20),
    shop_id BIGINT(20),

    CONSTRAINT fk_shop_transaction_web_user_id FOREIGN KEY (web_user_id) REFERENCES web_user(id),
    CONSTRAINT fk_shop_transaction_sale_order_line_id FOREIGN KEY (sale_order_line_id) REFERENCES sale_order_line(id),
    CONSTRAINT fk_shop_transaction_shop_id FOREIGN KEY (shop_id) REFERENCES shop(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;