ALTER TABLE sale_order_line ADD COLUMN sms_status INT(4) DEFAULT 0;
ALTER TABLE sale_order_line ADD COLUMN user_cellphone VARCHAR(255);
ALTER TABLE shopping_cart_line ADD COLUMN user_cellphone VARCHAR(255);
ALTER TABLE product ADD COLUMN sends_sms BIT(1);