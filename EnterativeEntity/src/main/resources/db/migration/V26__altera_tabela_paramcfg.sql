ALTER TABLE paramcfg DROP PRIMARY KEY;

ALTER TABLE paramcfg ADD COLUMN id INT NOT NULL PRIMARY KEY AUTO_INCREMENT;