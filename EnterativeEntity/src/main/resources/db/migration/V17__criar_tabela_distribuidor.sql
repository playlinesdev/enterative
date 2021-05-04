CREATE TABLE distribuidor ( 
	iddistribuidor BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
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
	merchant_category_code varchar(4),
	point_of_service_entry_mode varchar(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE distribuidor AUTO_INCREMENT=142749;
ALTER TABLE usuarioweb ADD distribuidor_iddistribuidor BIGINT(20);
ALTER TABLE usuarioweb ADD CONSTRAINT usuarioweb.fk_distribuidor FOREIGN KEY (distribuidor_iddistribuidor) REFERENCES distribuidor(iddistribuidor);