CREATE TABLE shop_transaction_category (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE shop_transaction ADD COLUMN shop_transaction_category_id BIGINT(20);
ALTER TABLE shop_transaction ADD CONSTRAINT fk_shop_transaction_shop_transaction_category_id FOREIGN KEY (shop_transaction_category_id) REFERENCES shop_transaction_category(id);

ALTER TABLE shop_transaction_dead_file ADD COLUMN shop_transaction_category_id BIGINT(20);
ALTER TABLE shop_transaction_dead_file ADD CONSTRAINT fk_shop_transaction_dead_file_shop_transaction_category_id FOREIGN KEY (shop_transaction_category_id) REFERENCES shop_transaction_category(id);

INSERT INTO shop_transaction_category (name) VALUES ('Compra');
INSERT INTO shop_transaction_category (name) VALUES ('Venda');
INSERT INTO shop_transaction_category (name) VALUES ('Comissão');

UPDATE shop_transaction SET shop_transaction_category_id = 1 WHERE type = 0;
UPDATE shop_transaction SET shop_transaction_category_id = 2 WHERE type = 1;

INSERT INTO web_user (login, name, situacao) VALUES ('system', 'Sistema', 0);
UPDATE web_user SET id = 0 WHERE login = 'system';