create table usuarioweb (
	idusuarioweb BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	login varchar(10),
	password varchar(80),
	nome varchar(80),
	situacao INT(4)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;