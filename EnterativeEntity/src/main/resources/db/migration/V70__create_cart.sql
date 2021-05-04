CREATE TABLE shopping_cart (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    amount DECIMAL(20,6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE shopping_cart_line (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    shopping_cart_id BIGINT(20),
    product_id BIGINT(20),
    quantity DECIMAL(20,6),
    amount DECIMAL(20, 6),
    total_amount DECIMAL(20, 6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;