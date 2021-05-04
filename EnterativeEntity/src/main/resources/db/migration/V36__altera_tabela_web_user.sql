ALTER TABLE web_user ADD COLUMN shop_id BIGINT(20);
ALTER TABLE web_user ADD CONSTRAINT fk_web_user_shop_id FOREIGN KEY (shop_id) REFERENCES shop(id);