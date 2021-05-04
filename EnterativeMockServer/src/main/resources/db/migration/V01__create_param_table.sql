CREATE TABLE server_param (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
        param_name INT(4),
        param_value VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO server_param (param_name, param_value) VALUES (0, '1');