CREATE TABLE ativacao (
	idativacao BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	codexterno VARCHAR(20),
	status INT(4),
	prioridade INT(4),
	data_incl TIMESTAMP,
	response_code VARCHAR(3),
	situacao VARCHAR(20),
	voucher_idvoucher BIGINT(20),
	ttl_ativacao INT(4),
	ttl_desfazimento INT(4),

    CONSTRAINT fk_voucher FOREIGN KEY (voucher_idvoucher) REFERENCES voucher(idvoucher),
    CONSTRAINT uk_codexterno UNIQUE KEY (codexterno)
    

) ENGINE=InnoDB DEFAULT CHARSET=utf8;