CREATE TABLE account (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),    
    status INT(4)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE account_transaction(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    amount DECIMAL(20,6),
    type INT(4),
    status INT(4),
    transaction_date TIMESTAMP NULL DEFAULT NULL,
    sale_order_line_id BIGINT(20),
    purchase_order_line_id BIGINT(20),
    account_id BIGINT(20),
    account_transaction_category_id BIGINT(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE account_transaction_category(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE account_transaction_dead_file(
    id BIGINT(20) PRIMARY KEY,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    amount DECIMAL(20,6),
    type INT(4),
    status INT(4),
    transaction_date TIMESTAMP NULL DEFAULT NULL,
    sale_order_line_id BIGINT(20),
    purchase_order_line_id BIGINT(20),
    account_id BIGINT(20),
    account_transaction_category_id BIGINT(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE shop ADD COLUMN account_id BIGINT(20);
ALTER TABLE web_user ADD COLUMN account_id BIGINT(20);
ALTER TABLE purchase_order ADD COLUMN account_id BIGINT(20);
ALTER TABLE sale_order ADD COLUMN account_id BIGINT(20);

INSERT INTO account (name, status) (SELECT name, 0 FROM shop);

UPDATE shop s SET account_id = (SELECT a.id FROM account a WHERE a.name = s.name);

UPDATE web_user u SET account_id = (SELECT s.account_id FROM shop s WHERE s.id = u.shop_id);

UPDATE purchase_order p SET account_id = (SELECT s.account_id FROM shop s WHERE s.id = p.shop_id);

UPDATE sale_order o SET account_id = (SELECT s.account_id FROM shop s WHERE s.id = o.shop_id);

INSERT INTO account_transaction_category (id, created_at, altered_at, created_by, altered_by, name) (SELECT id, created_at, altered_at, created_by, altered_by,
name FROM shop_transaction_category);

INSERT INTO account_transaction(id, created_at, altered_at, created_by, altered_by, amount, type, status, transaction_date, sale_order_line_id,
purchase_order_line_id, account_id, account_transaction_category_id) (SELECT t.id, t.created_at, t.altered_at, t.created_by, t.altered_by, amount,
type, t.status, transaction_date, sale_order_line_id, purchase_order_line_id, s.account_id, shop_transaction_category_id FROM shop_transaction t LEFT JOIN shop s
 ON s.id = t.shop_id);

INSERT INTO account_transaction_dead_file(id, created_at, altered_at, created_by, altered_by, amount, type, status, transaction_date, sale_order_line_id,
purchase_order_line_id, account_id, account_transaction_category_id) (SELECT t.id, t.created_at, t.altered_at, t.created_by, t.altered_by, amount,
type, t.status, transaction_date, sale_order_line_id, purchase_order_line_id, s.account_id, shop_transaction_category_id FROM shop_transaction_dead_file t 
LEFT JOIN shop s ON s.id = t.shop_id);

DROP TABLE shop_transaction;
DROP TABLE shop_transaction_category;
DROP TABLE shop_transaction_dead_file;