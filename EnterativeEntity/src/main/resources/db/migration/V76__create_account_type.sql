CREATE TABLE account_type (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),    
    initial_deposit DECIMAL(20,6),
    commissionable TINYINT(1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE account ADD COLUMN account_type_id BIGINT(20);

INSERT INTO account_type(name, initial_deposit, commissionable) VALUES ('base.shop', 500, 1);
INSERT INTO account_type(name, initial_deposit, commissionable) VALUES ('base.user', 0, 0);
UPDATE account SET account_type_id = 2;