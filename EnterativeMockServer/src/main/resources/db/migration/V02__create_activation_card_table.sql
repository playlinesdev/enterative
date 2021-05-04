CREATE TABLE activation_card (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
        ean VARCHAR(13),
        card_number VARCHAR(19),
        one_time VARCHAR(1),
        response_code VARCHAR(2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO activation_card (ean, card_number, one_time, response_code) VALUES ('5051644010690', '1234567890123456789', '0', '00');