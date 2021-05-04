DROP TABLE IF EXISTS purchase_order_line;
DROP TABLE IF EXISTS purchase_order;

CREATE TABLE purchase_order (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    data_incl TIMESTAMP,
    web_user_id BIGINT(20),
    shop_id BIGINT(20),
    total_amount DECIMAL(20,6),
    status INT(4),
    activated_web_user_id BIGINT(20),
    activated_date TIMESTAMP NULL,

    CONSTRAINT fk_purchase_order_web_user_id FOREIGN KEY (web_user_id) REFERENCES web_user(id),
    CONSTRAINT fk_purchase_order_shop_id FOREIGN KEY (shop_id) REFERENCES shop(id),
    CONSTRAINT fk_purchase_order_activated_web_user_id FOREIGN KEY (activated_web_user_id) REFERENCES web_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE purchase_order_line (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    purchase_order_id BIGINT(20),
    status INT(4),
    amount DECIMAL(20,6),

    CONSTRAINT fk_purchase_order_line_purchase_order_id FOREIGN KEY (purchase_order_id) REFERENCES purchase_order(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE shop_transaction ADD COLUMN purchase_order_line_id BIGINT(20);
ALTER TABLE shop_transaction ADD CONSTRAINT fk_shop_transaction_purchase_order_line_id FOREIGN KEY (purchase_order_line_id) REFERENCES purchase_order_line(id);