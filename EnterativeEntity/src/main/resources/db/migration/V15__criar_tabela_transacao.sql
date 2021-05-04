CREATE TABLE transacao (
	idtransacao BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	
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
	    terms_and_conditions  VARCHAR(999),
	
	    	product_id VARCHAR(99),
    		balance_amount VARCHAR(13),
    		redemption_pin VARCHAR(16),
    		redemption_account_number VARCHAR(30),
    		activation_account_number VARCHAR(19),
    		transaction_unique_id VARCHAR(26),
    		correlated_transaction_unique_id VARCHAR(26),
    
    		line VARCHAR(680),
    		
    	data_incl TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    	data_incl_fila TIMESTAMP NULL DEFAULT NULL,
    	data_retorno_transacao TIMESTAMP NULL DEFAULT NULL,
    	ativacao_idativacao BIGINT(20),
    	status INT(4),
    	acao INT(4),
    	recurso_idrecurso bigint(20) NULL,
    	
    	CONSTRAINT fk_ativacao FOREIGN KEY (ativacao_idativacao) REFERENCES ativacao(idativacao),
    	CONSTRAINT fk_recurso FOREIGN KEY (recurso_idrecurso) REFERENCES recurso(idrecurso)
    	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;