create table produto (
	idproduto BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
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
	
	CONSTRAINT fk_fornecedor FOREIGN KEY (fornecedor_idfornecedor) REFERENCES fornecedor(idfornecedor),
	CONSTRAINT fk_categoria FOREIGN KEY (categoria_idcategoria) REFERENCES categoria(idcategoria),
	CONSTRAINT fk_agrupamento FOREIGN KEY (agrupamento_idagrupamento) REFERENCES agrupamento(idagrupamento)
	
	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;