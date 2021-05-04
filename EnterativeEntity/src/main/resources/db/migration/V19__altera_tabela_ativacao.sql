ALTER TABLE ativacao DROP INDEX uk_codexterno;
ALTER TABLE ativacao ADD CONSTRAINT ativacao.uk_distr_pdv_terminal_codexterno UNIQUE KEY (distribuidor_iddistribuidor,pdv,terminal,codexterno);