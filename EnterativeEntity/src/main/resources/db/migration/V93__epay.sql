CREATE TABLE epay_activation (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    status INT(4),
    queue_status INT(4),
    callback_status INT(4),
    response_code VARCHAR(10),
    ttl_activation INT(4),
    merchant_id BIGINT(20),
    voucher_id BIGINT(20),
    shop_code VARCHAR(5),
    terminal VARCHAR(3),
    external_code VARCHAR(20),
    callbackurl VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE epay_activation ADD UNIQUE (external_code);

CREATE TABLE epay_transaction (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    queue_insert_date TIMESTAMP NULL DEFAULT NULL,
    transaction_return_date TIMESTAMP NULL DEFAULT NULL,
    direction INT(4),
    resource_id BIGINT(20),
    epay_activation_id BIGINT(20),
    amount VARCHAR(255),
    local_date_time VARCHAR(255),
    message_number VARCHAR(255),
    phone VARCHAR(255),
    product_id VARCHAR(255),
    request_type VARCHAR(255),
    item_sequence VARCHAR(255),
    service_fee VARCHAR(255),
    terminal_id VARCHAR(255),
    transaction_id VARCHAR(255),
    result_code VARCHAR(255),
    result_text VARCHAR(255),
    hosttxid VARCHAR(255),
    receipt_customer TEXT,
    receipt_merchant TEXT,
    server_date_time VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE epay_voucher (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    ean VARCHAR(13),
    product_id BIGINT(20),
    operator VARCHAR(255),
    area_code VARCHAR(255),
    phone VARCHAR(255),
    epay_product_id VARCHAR(255),
    amount DECIMAL(20,6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;