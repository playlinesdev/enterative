CREATE TABLE partner (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE partner_product(
    partner_id BIGINT(20),
    product_id BIGINT(20),
    KEY pk_partner_product_partner_id (partner_id),
    KEY pk_partner_product_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE web_user ADD COLUMN partner_id BIGINT(20);