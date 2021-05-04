INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164401908' , 'GCMV IMVU 20BRL', 20.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164401916' , 'GCMV HELP PROVAS 19 90BRL', 19.90);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164401188' , 'GCMV FACEBOOK 25BRL', 25.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164401189' , 'GCMV FACEBOOK 45BRL', 45.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164403309' , 'GCMV GOOGLE PLAY BRASIL 15BRL', 15.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164403310' , 'GCMV GOOGLE PLAY BRASIL 30BRL', 30.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164403311' , 'GCMV GOOGLE PLAY BRASIL 50BRL', 50.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164403312' , 'GCMV GOOGLE PLAY BRASIL 100BRL', 100.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164404845' , 'GCMV HABBO ASSISTED 20BRL', 20.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164405063' , 'GCMV RIOT BRASIL ASSISTED 20BRL', 20.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164405061' , 'GCMV RIOT BRASIL ASSISTED 50BRL', 50.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164405062' , 'GCMV RIOT BRASIL ASSISTED 100BRL', 100.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164405062' , 'GCMV RIOT BRASIL ASSISTED 100BRL', 100.00);	
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164400391' , 'ROTEIRO HOMOLOGACAO 7', 20.00);

INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164402264' , 'Cartão Google Play R$100', 100.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164404984' , 'Cartão Habbo R$45', 45.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164400869' , 'Cartão Level Up R$ 20', 20.00);
INSERT INTO produto (ean, descricao, valor_de_face) VALUES ('505164400870' , 'Cartão Level Up R$ 40', 40.00);

INSERT INTO distribuidor (iddistribuidor, nome, cnpj, cpf, endereco, bairro, cidade, estado, cep, telefone, contato, email, situacao, data_incl, data_alte, observacao, acquiring_institution_identifier, merchant_identifier, merchant_location, merchant_category_code, point_of_service_entry_mode) VALUES
(142749, 'Chart', '42767525000105', '', 'Rua dos Guajajaras,977', 'Lourdes', 'Belo Hol', 11, '31360060', '', '', 'andremelo@chart.com.br', 0, '2017-02-02 17:31:40', '2017-02-02 17:31:40', '', '60300004684', '60300004684    ', '                      Campo Grande MS BR', '5399', '041');

INSERT INTO usuarioweb (idusuarioweb, login, password, nome, situacao, distribuidor_iddistribuidor) VALUES
(3, 'chart', '$2a$10$xkdQa3ulII0gM22Mz4mXOOqdyInLRajplnugCN5jmcdNsEjF4PARK', 'chart', 0, 142749); --inovation

INSERT INTO usuarioweb_roles (usuario_web_idusuarioweb, roles_nome) VALUES
(3, 'ROLE_USUARIO');

