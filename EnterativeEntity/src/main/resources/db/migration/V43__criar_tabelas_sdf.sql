CREATE TABLE sdf_file (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	data_incl TIMESTAMP,
	name VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sdf_header (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    sdf_file_id BIGINT(20),
    file_transmission_date DATE,
    file_reporting_date DATE,
    partner_id VARCHAR(10),
    partner_name VARCHAR(25),
    filler VARCHAR(372),

    CONSTRAINT fk_sdf_header_sdf_file_id FOREIGN KEY (sdf_file_id) REFERENCES sdf_file(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sdf_detail (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    sdf_file_id BIGINT(20),
    merchant_id VARCHAR(25),
    merchant_name VARCHAR(50),
    store_id VARCHAR(15),
    terminal_id VARCHAR(16),
    clerk_id VARCHAR(20),
    card_issuer_id VARCHAR(25),
    card_issuer_processor_id VARCHAR(25),
    acquirer_id VARCHAR(25),
    acquired_transaction_date DATE,
    acquired_transaction_time TIME,
    gift_card_number VARCHAR(32),
    product_id VARCHAR(30),
    pos_transaction_date DATE,
    pos_transaction_time TIME,
    transaction_type VARCHAR(8),
    system_trace_audit_number VARCHAR(16),
    product_item_price DECIMAL(20,6),
    currency_code VARCHAR(3),
    merchant_transaction_id VARCHAR(16),
    bhn_transaction_id VARCHAR(30),
    auth_response_code VARCHAR(3),
    approval_code VARCHAR(6),
    reversal_type_code VARCHAR(1),
    transaction_amount DECIMAL(20, 6),
    consumer_fee_amount DECIMAL(20, 6),
    commission_amount DECIMAL(20, 6),
    total_tax_transaction_amount DECIMAL(20, 6),
    total_tax_commission_amount DECIMAL(20, 6),
    net_amount DECIMAL(20, 6),

    CONSTRAINT fk_sdf_detail_sdf_file_id FOREIGN KEY (sdf_file_id) REFERENCES sdf_file(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sdf_trailer (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    sdf_file_id BIGINT(20),
    file_transmission_date DATE,
    total_activation_count BIGINT(20),
    total_redemption_count BIGINT(20),
    total_reload_count BIGINT(20),
    total_refund_activation_count BIGINT(20),
    total_return_count BIGINT(20),
    total_reversal_count BIGINT(20),
    total_transaction_count BIGINT(20),
    total_activation_amount DECIMAL(20, 6),
    total_redemption_amount DECIMAL(20, 6),
    total_reload_amount DECIMAL(20, 6),
    total_refund_activation_amount DECIMAL(20, 6),
    total_return_amount DECIMAL(20, 6),
    total_reversal_amount DECIMAL(20, 6),
    total_consumer_fee_amount DECIMAL(20, 6),
    total_commission_amount DECIMAL(20, 6),
    total_tax_transaction_amount DECIMAL(20, 6),
    total_tax_commission_amount DECIMAL(20, 6),
    net_transaction_amount DECIMAL(20, 6),
    filler VARCHAR(130),

    CONSTRAINT fk_sdf_trailer_sdf_file_id FOREIGN KEY (sdf_file_id) REFERENCES sdf_file(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;