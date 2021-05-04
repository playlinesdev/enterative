CREATE TABLE web_user_product (
    web_user_id BIGINT(20),
    product_id BIGINT(20),
    KEY pk_web_user_product_web_user_id (web_user_id),
    KEY pk_web_user_product_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;