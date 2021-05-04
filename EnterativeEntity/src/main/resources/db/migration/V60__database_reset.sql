CREATE TABLE bhn_activation (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    status INT(4),
    activation_type INT(4),
    queue_status INT(4),
    callback_status INT(4),
    reversal_sent_date TIMESTAMP NULL DEFAULT NULL,
    response_code VARCHAR(10),
    ttl_activation INT(4),
    ttl_reversal INT(4),
    priority INT(4),
    external_code VARCHAR(20),
    shop_code VARCHAR(5),
    terminal VARCHAR(3),
    callbackurl VARCHAR(255),
    merchant_id BIGINT(20),
    bhn_voucher_id BIGINT(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE bhn_transaction (
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
    bhn_activation_id BIGINT(20),
    signature VARCHAR(6),
    product_category_code VARCHAR(2),
    spec_version VARCHAR(2),
    status_code VARCHAR(2),
    acquiring_institution_identifier VARCHAR(11),
    local_transaction_date VARCHAR(6),
    local_transaction_time VARCHAR(6),
    merchant_category_code VARCHAR(4),
    merchant_identifier VARCHAR(15),
    merchant_location VARCHAR(40),
    merchant_terminal_id VARCHAR(16),
    point_of_service_entry_mode VARCHAR(3),
    primary_account_number VARCHAR(19),
    processing_code VARCHAR(6),
    retrieval_reference_number VARCHAR(12),
    system_trace_audit_number VARCHAR(6),
    transaction_amount VARCHAR(12),
    transaction_currency_code VARCHAR(3),
    transmission_date_time VARCHAR(12),
    auth_identification_response VARCHAR(6),
    response_code VARCHAR(2),
    network_management_code VARCHAR(3),
    terms_and_conditions VARCHAR(999),
    product_id VARCHAR(99),
    balance_amount VARCHAR(13),
    redemption_pin VARCHAR(16),
    redemption_account_number VARCHAR(30),
    activation_account_number VARCHAR(19),
    transaction_unique_id VARCHAR(26),
    correlated_transaction_unique_id VARCHAR(26),
    line VARCHAR(680)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE bhn_voucher(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    ean VARCHAR(13),
    card_number VARCHAR(19),
    amount VARCHAR(12),
    pin VARCHAR(20),
    product_id BIGINT(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE callback_queue_(
    bhn_activation_id BIGINT(20) PRIMARY KEY
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE env_parameter(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    param INT(4),
    param_value VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE merchant_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(100),
    cnpj VARCHAR(14),
    cpf VARCHAR(11),
    street VARCHAR(255),
    street_number VARCHAR(50),
    district VARCHAR(255),
    city VARCHAR(255),
    country_state VARCHAR(2),
    cep VARCHAR(8),
    phone VARCHAR(11),
    contact VARCHAR(255),
    email VARCHAR(255),
    status INT(4),
    observation VARCHAR(255),
    acquiring_institution_identifier VARCHAR(11),
    merchant_identifier VARCHAR(15),
    merchant_location VARCHAR(40),
    merchant_category_id BIGINT(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE merchant_category_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    code VARCHAR(4),
    name VARCHAR(255)    
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE product_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    ean VARCHAR(13),
    name VARCHAR(255),
    supplier_code VARCHAR(255),
    image_path VARCHAR(255),
    supplier_ref_id VARCHAR(255),
    supplier_ref_category VARCHAR(255),
    supplier_ref_type VARCHAR(255),
    amount DECIMAL(20,6),
    commission_percentage DECIMAL(20,6),
    commission_amount DECIMAL(20,6),
    activation_fee DECIMAL(20,6),
    supplier_id BIGINT(20),
    product_category_id BIGINT(20),
    status INT(4)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE product_category(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    code VARCHAR(100),
    name VARCHAR(255),
    product_category_parent BIGINT(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE purchase_order_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    shop_id BIGINT(20),
    total_amount DECIMAL(20,6),
    status INT(4),
    activated_web_user_id BIGINT(20),
    activated_date TIMESTAMP NULL DEFAULT NULL    
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE purchase_order_line_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    purchase_order_id BIGINT(20),
    status INT(4),
    amount DECIMAL(20,6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE resource_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    server_id BIGINT(20),
    type INT(4),
    url VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sdf_detail_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
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
    transaction_id BIGINT(20),
    status INT(4)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sdf_file_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    status INT(4)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sdf_header_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    sdf_file_id BIGINT(20),
    file_transmission_date TIMESTAMP NULL DEFAULT NULL,
    file_reporting_date TIMESTAMP NULL DEFAULT NULL,
    partner_id VARCHAR(10),
    partner_name VARCHAR(25),
    filler VARCHAR(372)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sdf_trailer_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    sdf_file_id BIGINT(20),
    file_transmission_date TIMESTAMP NULL DEFAULT NULL,
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
    filler VARCHAR(130)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sale_order_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    shop_id BIGINT(20),
    amount DECIMAL(20,6),
    status INT(4),
    type INT(4),
    customer_mobile VARCHAR(11),
    payment_manager_token VARCHAR(36)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sale_order_line_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    sale_order_id BIGINT(20),
    product_id BIGINT(20),
    return_date TIMESTAMP NULL DEFAULT NULL,
    status INT(4),
    external_code VARCHAR(20),
    response INT(4),
    response_aux INT(4),
    activation_status INT(4),
    callback_status INT(4),
    barcode VARCHAR(32),
    amount DECIMAL(20,6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE server_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    status INT(4),
    echo_product_id BIGINT(20),
    server_sequence INT(4)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE shop_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(100),
    code VARCHAR(5),
    merchant_id BIGINT(20),
    status INT(4),
    razao_social VARCHAR(255),
    fantasia VARCHAR(255),
    cnpj VARCHAR(14),
    street VARCHAR(255),
    street_number VARCHAR(50),
    contact VARCHAR(255),
    district VARCHAR(255),
    city VARCHAR(255),
    country_state VARCHAR(2),
    cep VARCHAR(8),
    payment_token VARCHAR(36)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE shop_phone_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    shop_id BIGINT(20),
    phone VARCHAR(11),
    type INT(4),
    contact VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE shop_product_commission_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    shop_id BIGINT(20),
    product_id BIGINT(20),
    amount DECIMAL(20,6),
    percentage DECIMAL(20,6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE shop_transaction_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    amount DECIMAL(20,6),
    type INT(4),
    status INT(4),
    transaction_date TIMESTAMP NULL DEFAULT NULL,
    sale_order_line_id BIGINT(20),
    purchase_order_line_id BIGINT(20),
    shop_id BIGINT(20),
    shop_transaction_category_id BIGINT(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE shop_transaction_category_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE shop_transaction_dead_file_(
    id BIGINT(20) PRIMARY KEY,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    amount DECIMAL(20,6),
    type INT(4),
    status INT(4),
    transaction_date TIMESTAMP NULL DEFAULT NULL,
    sale_order_line_id BIGINT(20),
    purchase_order_line_id BIGINT(20),
    shop_id BIGINT(20),
    shop_transaction_category_id BIGINT(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE supplier_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255),
    spec_version VARCHAR(50),
    signature VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE web_user_(
    id BIGINT(20) PRIMARY KEY,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    login VARCHAR(255),
    name VARCHAR(255),
    terminal VARCHAR(3),
    password VARCHAR(255),
    token VARCHAR(255),
    status INT(4),
    shop_id BIGINT(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE web_user_role_(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP NULL DEFAULT NULL,
    altered_at TIMESTAMP NULL DEFAULT NULL,
    created_by BIGINT(20),
    altered_by BIGINT(20),
    name VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE web_user_web_user_role_(
    web_user_id BIGINT(20),
    web_user_role_id BIGINT(20),
    KEY pk_web_user_web_user_role_web_user_id (web_user_id),
    KEY pk_web_user_web_user_role_web_user_role_id (web_user_role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;