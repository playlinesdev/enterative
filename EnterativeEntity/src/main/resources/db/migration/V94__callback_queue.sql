DROP TABLE callback_queue;
CREATE TABLE callback_queue (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    bhn_activation_id BIGINT(20),
    epay_activation_id BIGINT(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE epay_activation ADD COLUMN transaction_id VARCHAR(36);