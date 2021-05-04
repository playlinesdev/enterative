create table pedido (
	idpedido BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	data_incl TIMESTAMP,
	usuario_web_idusuarioweb BIGINT(20),
	valtotal DECIMAL(20,6),
	status INT(4),
	tipo INT(4),
	
	CONSTRAINT fk_usuario_web FOREIGN KEY (usuario_web_idusuarioweb) REFERENCES usuarioweb(idusuarioweb)
	
	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;