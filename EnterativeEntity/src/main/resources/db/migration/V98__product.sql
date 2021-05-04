ALTER TABLE product ADD COLUMN primary_account_number VARCHAR(20);
UPDATE product SET primary_account_number = '6039534201000000024' WHERE type = 1;