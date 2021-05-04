CREATE TABLE pagseguro_payment_method (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),    
    payment_method INT(4),
    status INT(4)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pagseguro_payment_method (created_at, created_by, altered_at, altered_by, name, payment_method, status) VALUES (CURRENT_TIMESTAMP(), 0, CURRENT_TIMESTAMP(), 0, 'base.eft', 0, 0);
INSERT INTO pagseguro_payment_method (created_at, created_by, altered_at, altered_by, name, payment_method, status) VALUES (CURRENT_TIMESTAMP(), 0, CURRENT_TIMESTAMP(), 0, 'base.boleto', 1, 0);
INSERT INTO pagseguro_payment_method (created_at, created_by, altered_at, altered_by, name, payment_method, status) VALUES (CURRENT_TIMESTAMP(), 0, CURRENT_TIMESTAMP(), 0, 'base.creditcard', 2, 2);
INSERT INTO pagseguro_payment_method (created_at, created_by, altered_at, altered_by, name, payment_method, status) VALUES (CURRENT_TIMESTAMP(), 0, CURRENT_TIMESTAMP(), 0, 'base.pagsegurobalance', 3, 0);
INSERT INTO pagseguro_payment_method (created_at, created_by, altered_at, altered_by, name, payment_method, status) VALUES (CURRENT_TIMESTAMP(), 0, CURRENT_TIMESTAMP(), 0, 'base.deposit', 4, 0);