create table usuarioweb_roles (
	usuario_web_idusuarioweb BIGINT(20),
	roles_nome varchar(30),
	foreign key (usuario_web_idusuarioweb) references usuarioweb(idusuarioweb),
	foreign key (roles_nome) references roleweb(nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;