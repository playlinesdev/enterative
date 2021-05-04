INSERT INTO roleweb(nome) VALUES('ROLE_ADMIN');
INSERT INTO roleweb(nome) VALUES('ROLE_USUARIO');
INSERT INTO usuarioweb(idusuarioweb, login, password, nome, situacao) VALUES(null, 'admin', '$2a$10$k4.qsBsoljHU7VjZZWz8/ubSkdyFkGYObgjtDxU3mUCATqbwvxcKq', 'Administrador', 0);
INSERT INTO usuarioweb(idusuarioweb, login, password, nome, situacao) VALUES(null, 'user', '$2a$10$k4.qsBsoljHU7VjZZWz8/ubSkdyFkGYObgjtDxU3mUCATqbwvxcKq', 'Usuario', 0);
INSERT INTO usuarioweb_roles(usuario_web_idusuarioweb, roles_nome) VALUES(1, 'ROLE_ADMIN');
INSERT INTO usuarioweb_roles(usuario_web_idusuarioweb, roles_nome) VALUES(2, 'ROLE_USUARIO');
