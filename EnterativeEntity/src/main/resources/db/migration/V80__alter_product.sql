ALTER TABLE product ADD COLUMN type INT(4);
UPDATE product SET type = 0;