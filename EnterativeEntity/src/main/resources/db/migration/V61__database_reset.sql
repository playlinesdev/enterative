INSERT INTO bhn_activation (id, created_at, altered_at, created_by, altered_by, status, activation_type, queue_status, callback_status, reversal_sent_date,
response_code, ttl_activation, ttl_reversal, priority, external_code, shop_code, terminal, callbackurl, merchant_id, bhn_voucher_id)
(SELECT id, data_incl, data_incl, 0, 0, status, tipo, 2, callback_status, data_envio_desfazimento, response_code, ttl_ativacao,
ttl_desfazimento, prioridade, codexterno, pdv, terminal, callbackurl, merchant_id, voucher_id FROM ativacao);

INSERT INTO bhn_transaction (id, created_at, altered_at, created_by, altered_by, queue_insert_date, transaction_return_date, direction, resource_id,
bhn_activation_id, signature, product_category_code, spec_version, status_code, acquiring_institution_identifier, local_transaction_date,
local_transaction_time, merchant_category_code, merchant_identifier, merchant_location, merchant_terminal_id, point_of_service_entry_mode,
primary_account_number, processing_code, retrieval_reference_number, system_trace_audit_number, transaction_amount, transaction_currency_code,
transmission_date_time, auth_identification_response, response_code, network_management_code, terms_and_conditions, product_id, balance_amount,
redemption_pin, redemption_account_number, activation_account_number, transaction_unique_id, correlated_transaction_unique_id, line) (SELECT
id, data_incl, data_retorno_transacao, 0, 0, data_incl_fila, data_retorno_transacao, acao, recurso_id, ativacao_id, signature, product_category_code,
spec_version, status_code, acquiring_institution_identifier, local_transaction_date, local_transaction_time, merchant_category_code, merchant_identifier,
merchant_location, merchant_terminal_id, point_of_service_entry_mode, primary_account_number, processing_code, retrieval_reference_number,
system_trace_audit_number, transaction_amount, transaction_currency_code, transmission_date_time, auth_identification_response, response_code,
network_management_code, terms_and_conditions, product_id, balance_amount, redemption_pin, redemption_account_number, activation_account_number,
transaction_unique_id, correlated_transaction_unique_id, line FROM transacao);

INSERT INTO bhn_voucher (id, created_at, altered_at, created_by, altered_by, ean, card_number, amount, pin, product_id) 
(SELECT id, null, null, 0, 0, upc, card_no, amount, pin, product_id FROM voucher);

INSERT INTO callback_queue_(bhn_activation_id) (SELECT ativacao_id FROM callback_queue);

INSERT INTO merchant_(id, created_at, altered_at, created_by, altered_by, name, cnpj, cpf, street, street_number, district, city, country_state,
cep, phone, contact, email, status, observation, acquiring_institution_identifier, merchant_identifier, merchant_location, merchant_category_id)
(SELECT id, data_incl, data_alte, 0, 0, nome, cnpj, cpf, endereco, null, bairro, cidade, null, cep, telefone, contato, email, situacao, observacao,
acquiring_institution_identifier, merchant_identifier, merchant_location, merchant_category_id FROM merchant);

INSERT INTO merchant_category_(id, created_at, altered_at, created_by, altered_by, code, name)
(SELECT id, current_timestamp(), current_timestamp(), 0, 0, category_code, description FROM merchant_category);

INSERT INTO product_(id, created_at, altered_at, created_by, altered_by, ean, name, supplier_code, image_path, supplier_ref_id,
supplier_ref_category, supplier_ref_type, amount, commission_percentage, commission_amount, activation_fee, supplier_id,
product_category_id, status) (SELECT id, data_incl, data_alte, 0, 0, ean, descricao, codigo_produto_fornecedor, imagem,
supplier_ref_id, supplier_ref_category, supplier_ref_type, valor_de_face, per_comissao, val_comissao, taxa_ativacao,
supplier_id, null, status FROM product);

INSERT INTO purchase_order_(id, created_at, altered_at, created_by, altered_by, shop_id, total_amount, status, activated_web_user_id, activated_date)
(SELECT id, data_incl, activated_date, web_user_id, activated_web_user_id, shop_id, total_amount, status, activated_web_user_id, activated_date FROM purchase_order);

INSERT INTO purchase_order_line_(id, created_at, altered_at, created_by, altered_by, name, purchase_order_id, status, amount) (SELECT
id, null, null, 0, 0, name, purchase_order_id, status, amount FROM purchase_order_line);

INSERT INTO resource_(id, created_at, altered_at, created_by, altered_by, server_id, type, url) (SELECT id, current_timestamp(),
current_timestamp(), 0, 0, server_id, tipo, endereco FROM resource);

INSERT INTO sale_order_(id, created_at, altered_at, created_by, altered_by, shop_id, amount, status, type, customer_mobile) (SELECT
id, data_incl, data_incl, web_user_id, web_user_id, null, valtotal, status, tipo, customer_mobile FROM sale_order);

INSERT INTO sale_order_line_(id, created_at, altered_at, created_by, altered_by, sale_order_id, product_id, return_date, status, external_code, response,
response_aux, activation_status, callback_status, barcode, amount) (SELECT id, data_retorno, data_retorno, 0, 0, sale_order_id, product_id, data_retorno,
status, codexterno, resposta, resposta_aux, ativacao_status, callback_status, barcode, valitem FROM sale_order_line);

INSERT INTO sdf_detail_(id, created_at, altered_at, created_by, altered_by, sdf_file_id, merchant_id, merchant_name, store_id, terminal_id, clerk_id,
card_issuer_id, card_issuer_processor_id, acquirer_id, acquired_transaction_date, acquired_transaction_time, gift_card_number, product_id, pos_transaction_date,
pos_transaction_time, transaction_type, system_trace_audit_number, product_item_price, currency_code, merchant_transaction_id, bhn_transaction_id,
auth_response_code, approval_code, reversal_type_code, transaction_amount, consumer_fee_amount, commission_amount, total_tax_transaction_amount,
total_tax_commission_amount, net_amount, transaction_id, status) (SELECT id, current_timestamp(), current_timestamp(), 0, 0, sdf_file_id, merchant_id,
merchant_name, store_id, terminal_id, clerk_id, card_issuer_id, card_issuer_processor_id, acquirer_id, acquired_transaction_date, acquired_transaction_time,
gift_card_number, product_id, pos_transaction_date, pos_transaction_time, transaction_type, system_trace_audit_number, product_item_price, currency_code,
merchant_transaction_id, bhn_transaction_id, auth_response_code, approval_code, reversal_type_code, transaction_amount, consumer_fee_amount, commission_amount,
total_tax_transaction_amount, total_tax_commission_amount, net_amount, bhn_transaction_id, status FROM sdf_detail);

INSERT INTO sdf_file_(id, created_at, altered_at, created_by, altered_by, name, status) (SELECT
id, data_incl, data_incl, 0, 0, name, status FROM sdf_file);

INSERT INTO sdf_trailer_(id, created_at, altered_at, created_by, altered_by, sdf_file_id, file_transmission_date, total_activation_count, total_redemption_count,
total_reload_count, total_refund_activation_count, total_return_count, total_reversal_count, total_transaction_count, total_activation_amount,
total_redemption_amount, total_reload_amount, total_refund_activation_amount, total_return_amount, total_reversal_amount, total_consumer_fee_amount,
total_commission_amount, total_tax_transaction_amount, total_tax_commission_amount, net_transaction_amount, filler) (SELECT id,
current_timestamp(), current_timestamp(), 0, 0, sdf_file_id, file_transmission_date, total_activation_count, total_redemption_count, total_reload_count,
total_refund_activation_count, total_return_count, total_reversal_count, total_transaction_count, total_activation_amount, total_redemption_amount,
total_reload_amount, total_refund_activation_amount, total_return_amount, total_reversal_amount, total_consumer_fee_amount, total_commission_amount,
total_tax_transaction_amount, total_tax_commission_amount, net_transaction_amount, filler FROM sdf_trailer);

INSERT INTO server_(id, created_at, altered_at, created_by, altered_by, name, status, echo_product_id, server_sequence) (SELECT
id, current_timestamp(), current_timestamp(), 0, 0, description, status, echo_product_id, server_sequence FROM server);

INSERT INTO shop_(id, created_at, altered_at, created_by, altered_by, name, code, merchant_id, status, razao_social, fantasia, cnpj, street, street_number,
contact, district, city, country_state, cep) (SELECT id, current_timestamp(), current_timestamp(), 0, 0, name, code, merchant_id, status, razao_social,
fantasia, cnpj, logradouro, numero, contato, bairro, cidade, uf, cep FROM shop);

INSERT INTO shop_phone_(id, created_at, altered_at, created_by, altered_by, shop_id, phone, type, contact) (SELECT
id, current_timestamp(), current_timestamp(), 0, 0, shop_id, phone, type, contact FROM shop_phone);

INSERT INTO shop_product_commission_(id, created_at, altered_at, created_by, altered_by, shop_id, product_id, amount, percentage) (SELECT
id, current_timestamp(), current_timestamp(), 0, 0, shop_id, product_id, amount, percentage FROM shop_product_commission);

INSERT INTO shop_transaction_(id, created_at, altered_at, created_by, altered_by, amount, type, status, transaction_date, sale_order_line_id,
purchase_order_line_id, shop_id, shop_transaction_category_id) (SELECT id, transaction_date, transaction_date, web_user_id, web_user_id, amount,
type, status, transaction_date, sale_order_line_id, purchase_order_line_id, shop_id, shop_transaction_category_id FROM shop_transaction);

INSERT INTO shop_transaction_category_(id, created_at, altered_at, created_by, altered_by, name) (SELECT id, current_timestamp(), current_timestamp(),
0, 0, name FROM shop_transaction_category);

INSERT INTO shop_transaction_dead_file_(id, created_at, altered_at, created_by, altered_by, amount, type, status, transaction_date, sale_order_line_id,
purchase_order_line_id, shop_id, shop_transaction_category_id) (SELECT id, transaction_date, transaction_date, web_user_id, web_user_id, amount, type,
status, transaction_date, sale_order_line_id, purchase_order_line_id, shop_id, shop_transaction_category_id FROM shop_transaction_dead_file);

INSERT INTO supplier_(id, created_at, altered_at, created_by, altered_by, name, spec_version, signature) (SELECT 
id, current_timestamp(), current_timestamp(), 0, 0, name, spec_version, signature FROM supplier);

INSERT INTO web_user_(id, created_at, altered_at, created_by, altered_by, login, name, terminal, password, token, status, shop_id) (SELECT
id, current_timestamp(), current_timestamp(), null, null, login, name, terminal, password, token, situacao, shop_id FROM web_user);

ALTER TABLE web_user_ MODIFY id BIGINT(20) AUTO_INCREMENT, auto_increment=40;
UPDATE web_user_ SET id = 0 WHERE id = 40;

INSERT INTO web_user_role_(id, created_at, altered_at, name) (SELECT id, current_timestamp(), current_timestamp(), name FROM web_user_role);

INSERT INTO web_user_web_user_role_(web_user_id, web_user_role_id) (SELECT web_user_id, web_user_role_id FROM web_user_web_user_role);