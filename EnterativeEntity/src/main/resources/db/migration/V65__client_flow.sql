UPDATE web_user_role SET name = 'ROLE_CUSTOMER' WHERE id = 2;
UPDATE web_user SET token = null;

ALTER TABLE web_user ADD COLUMN email VARCHAR(255);
ALTER TABLE web_user ADD UNIQUE (login);
ALTER TABLE web_user ADD UNIQUE (token);
ALTER TABLE web_user ADD UNIQUE (email);
ALTER TABLE bhn_activation ADD UNIQUE (external_code);
ALTER TABLE product ADD UNIQUE (ean);
ALTER TABLE product_category ADD UNIQUE (code);
ALTER TABLE sale_order ADD COLUMN payment_gateway_token VARCHAR(36);
ALTER TABLE sale_order ADD COLUMN payment_transaction_id VARCHAR(36);