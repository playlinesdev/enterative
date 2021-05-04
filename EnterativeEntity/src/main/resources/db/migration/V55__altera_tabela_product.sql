ALTER TABLE product ADD COLUMN status INT(4);
UPDATE product SET status = 0;