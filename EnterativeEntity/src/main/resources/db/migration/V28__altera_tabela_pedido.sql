DROP TABLE IF EXISTS itempedido;
DROP TABLE IF EXISTS pedido;

CREATE TABLE pedido (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	data_incl TIMESTAMP,
	usuario_id BIGINT(20),
	valtotal DECIMAL(20,6),
	status INT(4),
	tipo INT(4),
	
	CONSTRAINT fk_pedido_usuario_id FOREIGN KEY (usuario_id) REFERENCES usuarioweb(idusuarioweb)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE itempedido (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	pedido_id BIGINT(20) NOT NULL,
    produto_id BIGINT(20) NOT NULL,
    data_retorno TIMESTAMP,
    status INT(4),
	codexterno VARCHAR(32) NOT NULL,
    resposta INT(4),
    resposta_aux INT(4),
    barcode VARCHAR(32),
	valitem DECIMAL(20,6),
	
	CONSTRAINT fk_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id),
	CONSTRAINT fk_produto FOREIGN KEY (produto_id) REFERENCES produto(idproduto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;