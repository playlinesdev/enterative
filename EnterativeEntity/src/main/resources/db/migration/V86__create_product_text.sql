CREATE TABLE product_text (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    description TEXT,
    type INT(4)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE product ADD COLUMN terms_and_conditions BIGINT(20);
ALTER TABLE product ADD COLUMN activation_instructions BIGINT(20);