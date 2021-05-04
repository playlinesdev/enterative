CREATE TABLE shop_phone (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    shop_id BIGINT(20),
    phone VARCHAR(11),
    type INT(4),
    contact VARCHAR(255),
    CONSTRAINT fk_shop_phone_shop_id FOREIGN KEY (shop_id) REFERENCES shop(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;