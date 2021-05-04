SET FOREIGN_KEY_CHECKS=0;
UPDATE distribuidor SET iddistribuidor = 1 WHERE iddistribuidor IS NOT NULL;
UPDATE usuarioweb SET distribuidor_iddistribuidor = 1 WHERE distribuidor_iddistribuidor IS NOT NULL;
SET FOREIGN_KEY_CHECKS=1;

INSERT INTO merchant (nome, cnpj, cpf, endereco, bairro, cidade, estado, cep, telefone, contato, email, situacao, data_incl, data_alte, observacao,
                      acquiring_institution_identifier, merchant_identifier, merchant_location, merchant_category_id) (SELECT nome, cnpj, cpf, endereco, bairro,
                      cidade, estado, cep, telefone, contato, email, situacao, data_incl, data_alte, observacao, acquiring_institution_identifier,
                      merchant_identifier, merchant_location, null FROM distribuidor);

INSERT INTO web_user (login, name, situacao, password, token, merchant_id) (SELECT login, nome, situacao, password, token, distribuidor_iddistribuidor FROM usuarioweb);

INSERT INTO web_user_role (id, name) (SELECT id, nome FROM roleweb);

INSERT INTO product (ean, descricao, data_incl, data_alte, fornecedor_idfornecedor, codigo_produto_fornecedor, categoria_idcategoria, agrupamento_idagrupamento,
                    valor_de_face, per_comissao, val_comissao, taxa_ativacao, imagem) (SELECT ean, descricao, data_incl, data_alte, fornecedor_idfornecedor, 
                    codigo_produto_fornecedor, categoria_idcategoria, agrupamento_idagrupamento, valor_de_face, per_comissao, val_comissao, taxa_ativacao, imagem FROM produto);

DROP TABLE usuarioweb_roles;
DROP TABLE roleweb;
DROP TABLE usuarioweb;
DROP TABLE distribuidor;
DROP TABLE produto;