CREATE TABLE product_highlight (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    highlight_sequence INT(4),
    product_id BIGINT(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;