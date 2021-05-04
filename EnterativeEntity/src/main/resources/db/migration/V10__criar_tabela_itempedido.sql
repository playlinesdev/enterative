create table itempedido (
	iditempedido BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	pedido_idpedido BIGINT(20) NOT NULL,
	status INT(4),
	situacao VARCHAR(20),
	produto_idproduto BIGINT(20),
	card_no VARCHAR(19),
	gc_cod_barra VARCHAR(31),
	valitem DECIMAL(20,6),
	ativacao BIGINT(20),
	resposta VARCHAR(80),
	data_retorno TIMESTAMP,
	pin VARCHAR(20),
	
	CONSTRAINT fk_pedido FOREIGN KEY (pedido_idpedido) REFERENCES pedido(idpedido),
	CONSTRAINT fk_produto FOREIGN KEY (produto_idproduto) REFERENCES produto(idproduto)
	
	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;