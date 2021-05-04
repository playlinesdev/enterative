ALTER TABLE sale_order_line ADD COLUMN user_email_status INT(4) DEFAULT 0;
ALTER TABLE sale_order_line ADD COLUMN user_email VARCHAR(255);
ALTER TABLE shopping_cart_line ADD COLUMN user_email VARCHAR(255);