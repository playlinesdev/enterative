CREATE TABLE supplier (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(80),
    spec_version VARCHAR(20),
    signature VARCHAR(20)	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO supplier (name) (SELECT nome FROM fornecedor);

ALTER TABLE product ADD COLUMN supplier_id BIGINT(20);

UPDATE product SET fornecedor_idfornecedor = supplier_id;

ALTER TABLE product DROP FOREIGN KEY fk_product_fornecedor_id;

ALTER TABLE product DROP COLUMN fornecedor_idfornecedor;

ALTER TABLE product ADD CONSTRAINT fk_product_supplier_id FOREIGN KEY (supplier_id) REFERENCES supplier(id);

DROP TABLE IF EXISTS fornecedor;