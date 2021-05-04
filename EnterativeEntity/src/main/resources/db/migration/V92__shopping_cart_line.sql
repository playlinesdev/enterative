ALTER TABLE shopping_cart_line ADD COLUMN storestep TEXT;

ALTER TABLE sale_order_line ADD COLUMN operator VARCHAR(255);
ALTER TABLE sale_order_line ADD COLUMN area_code VARCHAR(2);
ALTER TABLE sale_order_line ADD COLUMN catalog_id VARCHAR(255);
ALTER TABLE sale_order_line ADD COLUMN phone VARCHAR(11);