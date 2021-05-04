ALTER TABLE voucher ADD COLUMN product_id BIGINT(20);
ALTER TABLE voucher ADD CONSTRAINT fk_voucher_product_id FOREIGN KEY (product_id) REFERENCES product(id);