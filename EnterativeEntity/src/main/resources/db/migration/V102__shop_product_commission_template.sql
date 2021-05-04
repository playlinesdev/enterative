CREATE TABLE shop_product_commission_template (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE shop_product_commission_template_line (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    template_id BIGINT(20),
    product_id BIGINT(20),
    amount DECIMAL(20,6),
    percentage DECIMAL(20,6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;