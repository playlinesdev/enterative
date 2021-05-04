DROP TABLE IF EXISTS callback_queue;
DROP TABLE IF EXISTS transacao;
DROP TABLE IF EXISTS ativacao;
DROP TABLE IF EXISTS voucher;

CREATE TABLE voucher (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	upc VARCHAR(99),
	card_no VARCHAR(20),
	amount VARCHAR(12),
	pin VARCHAR(20)	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE merchant_category (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    category_code VARCHAR(4),
    description VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE merchant ( 
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome varchar(80),
    cnpj varchar(14),
    cpf varchar(11),
    endereco varchar(80),
    bairro varchar(80),
    cidade varchar(80),
    estado INT(4),
    cep varchar(8),
    telefone varchar(18),
    contato varchar(20),
    email varchar(255),
    situacao INT(4),
    data_incl TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	data_alte TIMESTAMP NULL DEFAULT NULL,
    observacao varchar(255),
    acquiring_institution_identifier varchar(11),
    merchant_identifier varchar(15),
	merchant_location varchar(40),
	merchant_category_id BIGINT(20),

    CONSTRAINT fk_merchant_merchant_category FOREIGN KEY (merchant_category_id) REFERENCES merchant_category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE web_user (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	login varchar(10) NOT NULL,
    name varchar(80) NOT NULL,
    situacao INT(4),
	password varchar(80),
    token VARCHAR(80),
    merchant_id BIGINT(20),

    CONSTRAINT fk_web_user_merchant FOREIGN KEY (merchant_id) REFERENCES merchant(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE web_user_role (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE web_user_web_user_role (
    web_user_id BIGINT(20),
    web_user_role_id BIGINT(20),
    KEY pk_web_user_web_user_role_web_user_id (web_user_id),
    KEY pk_web_user_web_user_role_web_user_role_id (web_user_role_id),
    CONSTRAINT fk_web_user_id FOREIGN KEY (web_user_id) REFERENCES web_user(id),
    CONSTRAINT fk_web_user_role_id FOREIGN KEY (web_user_role_id) REFERENCES web_user_role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE ativacao (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    status INT(4),
    tipo INT(4),
    callback_status INT(4),
    data_incl TIMESTAMP,
    prioridade INT(4),
    codexterno VARCHAR(20),
	response_code VARCHAR(3),
	situacao VARCHAR(20),
	ttl_ativacao INT(4),
	ttl_desfazimento INT(4),
    pdv VARCHAR(10),
    terminal VARCHAR(6),
    callbackurl VARCHAR(255),
    merchant_id BIGINT(20),
    web_user_id BIGINT(20),
    voucher_id BIGINT(20),
    data_envio_desfazimento TIMESTAMP NULL DEFAULT NULL,

    CONSTRAINT fk_ativacao_voucher_id FOREIGN KEY (voucher_id) REFERENCES voucher(id),
    CONSTRAINT fk_ativacao_merchant_id FOREIGN KEY (merchant_id) REFERENCES merchant(id),
    CONSTRAINT fk_ativacao_web_user_id FOREIGN KEY (web_user_id) REFERENCES web_user(id),
    CONSTRAINT uk_ativacao_codexterno UNIQUE KEY (codexterno)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE callback_queue (
    ativacao_id BIGINT(20) PRIMARY KEY,
    
    CONSTRAINT fk_callback_queue_ativacao FOREIGN KEY (ativacao_id) REFERENCES ativacao(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE product (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	ean VARCHAR(13),
	descricao VARCHAR(80),
	data_incl TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	data_alte TIMESTAMP NULL DEFAULT NULL,
	fornecedor_idfornecedor BIGINT(20),
	codigo_produto_fornecedor VARCHAR(20),
	categoria_idcategoria BIGINT(20),
	agrupamento_idagrupamento BIGINT(20),
	valor_de_face DECIMAL(20,6),
	per_comissao DECIMAL(20,6),
	val_comissao DECIMAL(20,6),
	taxa_ativacao DECIMAL(20,6),
	imagem VARCHAR(20),
	
	CONSTRAINT fk_product_fornecedor_id FOREIGN KEY (fornecedor_idfornecedor) REFERENCES fornecedor(idfornecedor),
	CONSTRAINT fk_product_categoria_id FOREIGN KEY (categoria_idcategoria) REFERENCES categoria(idcategoria),
	CONSTRAINT fk_product_agrupamento_id FOREIGN KEY (agrupamento_idagrupamento) REFERENCES agrupamento(idagrupamento)
	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE shop (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(40) NOT NULL,
    code VARCHAR(5),
    merchant_id BIGINT(20),

    CONSTRAINT fk_shop_merchant_id FOREIGN KEY (merchant_id) REFERENCES merchant(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE itempedido;
DROP TABLE pedido;

CREATE TABLE sale_order (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	data_incl TIMESTAMP,
	web_user_id BIGINT(20),
	valtotal DECIMAL(20,6),
	status INT(4),
	tipo INT(4),
	
	CONSTRAINT fk_sale_order_web_user_id FOREIGN KEY (web_user_id) REFERENCES web_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE sale_order_line (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	sale_order_id BIGINT(20) NOT NULL,
    product_id BIGINT(20) NOT NULL,
    data_retorno TIMESTAMP,
    status INT(4),
	codexterno VARCHAR(32) NOT NULL,
    resposta INT(4),
    resposta_aux INT(4),
    barcode VARCHAR(32),
	valitem DECIMAL(20,6),
    callback_status INT(4),
    ativacao_status INT(4),
	
	CONSTRAINT fk_sale_order_line_sale_order_id FOREIGN KEY (sale_order_id) REFERENCES sale_order(id),
	CONSTRAINT fk_sale_order_line_product_id FOREIGN KEY (product_id) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `transacao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `signature` varchar(6) DEFAULT NULL,
  `product_category_code` varchar(2) DEFAULT NULL,
  `spec_version` varchar(2) DEFAULT NULL,
  `status_code` varchar(2) DEFAULT NULL,
  `acquiring_institution_identifier` varchar(11) DEFAULT NULL,
  `local_transaction_date` varchar(6) DEFAULT NULL,
  `local_transaction_time` varchar(6) DEFAULT NULL,
  `merchant_category_code` varchar(4) DEFAULT NULL,
  `merchant_identifier` varchar(15) DEFAULT NULL,
  `merchant_location` varchar(40) DEFAULT NULL,
  `merchant_terminal_id` varchar(16) DEFAULT NULL,
  `point_of_service_entry_mode` varchar(3) DEFAULT NULL,
  `primary_account_number` varchar(19) DEFAULT NULL,
  `processing_code` varchar(6) DEFAULT NULL,
  `retrieval_reference_number` varchar(12) DEFAULT NULL,
  `system_trace_audit_number` varchar(6) DEFAULT NULL,
  `transaction_amount` varchar(12) DEFAULT NULL,
  `transaction_currency_code` varchar(3) DEFAULT NULL,
  `transmission_date_time` varchar(12) DEFAULT NULL,
  `auth_identification_response` varchar(6) DEFAULT NULL,
  `response_code` varchar(2) DEFAULT NULL,
  `network_management_code` varchar(3) DEFAULT NULL,
  `terms_and_conditions` varchar(999) DEFAULT NULL,
  `product_id` varchar(99) DEFAULT NULL,
  `balance_amount` varchar(13) DEFAULT NULL,
  `redemption_pin` varchar(16) DEFAULT NULL,
  `redemption_account_number` varchar(30) DEFAULT NULL,
  `activation_account_number` varchar(19) DEFAULT NULL,
  `transaction_unique_id` varchar(26) DEFAULT NULL,
  `correlated_transaction_unique_id` varchar(26) DEFAULT NULL,
  `line` varchar(680) DEFAULT NULL,
  `data_incl` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_incl_fila` timestamp NULL DEFAULT NULL,
  `data_retorno_transacao` timestamp NULL DEFAULT NULL,
  `ativacao_id` bigint(20) DEFAULT NULL,
  `status` int(4) DEFAULT NULL,
  `acao` int(4) DEFAULT NULL,
  `recurso_id` bigint(20) DEFAULT NULL,
  `tipo_ativacao` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_transacao_ativacao_id` (`ativacao_id`),
  KEY `fk_transacao_recurso_id` (`recurso_id`),
  CONSTRAINT `fk_transacao_ativacao_id` FOREIGN KEY (`ativacao_id`) REFERENCES `ativacao` (`id`),
  CONSTRAINT `fk_transacao_recurso_id` FOREIGN KEY (`recurso_id`) REFERENCES `recurso` (`idrecurso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
