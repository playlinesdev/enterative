create table agrupamento (
	idagrupamento BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	codigo VARCHAR(20),
	descricao VARCHAR(80),
	agrupamento_pai_idagrupamento BIGINT(20),
	
	CONSTRAINT fk_agrupamento_pai FOREIGN KEY (agrupamento_pai_idagrupamento) REFERENCES agrupamento(idagrupamento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
