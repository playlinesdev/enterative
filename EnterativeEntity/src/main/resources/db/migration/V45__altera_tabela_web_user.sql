ALTER TABLE web_user DROP FOREIGN KEY fk_web_user_merchant;
ALTER TABLE web_user DROP COLUMN merchant_id;

ALTER TABLE web_user ADD COLUMN terminal VARCHAR(3);