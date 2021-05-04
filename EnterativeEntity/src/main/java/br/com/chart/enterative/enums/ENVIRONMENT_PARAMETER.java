package br.com.chart.enterative.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 *
 * @author William Leite
 */
public enum ENVIRONMENT_PARAMETER {
    TTL_ECHO("TTL_ECHO", "Tempo de Vida em Segundos de uma Requisição ECHO", Integer.class, 26),
    SCHEDULE_ECHO("SCHEDULE_ECHO", "Tempo de Schedule em Segundos para o envio de uma Requisição ECHO", Integer.class, 300 * 1000l),
    ECHO_MAX_RETRIES("ECHO_MAX_RETRIES", "Número máximo de tentativas consecutivas de ECHO", Integer.class, 10),
    THREADS_CALLBACK("THREADS_CALLBACK", "Número de Threads para o serviço de Callback", Integer.class, 3),
    THREADS_ACTIVATION("THREADS_ACTIVATION", "Número de Threads para o serviço de Ativação", Integer.class, 3),
    THREADS_REVERSAL("THREADS_REVERSAL", "Número de Threads para o serviço de Desfazimento", Integer.class, 3),
    SEQUENCIALTRANSACAO("SEQUENCIALTRANSACAO", "SEQUENCIAL DA TRANSACAO PARA ENVIAR PARA BLACKHAWK", Integer.class, 90373),
    TTL_ATIVACAO("TTL_ATIVACAO", "Quantidade de vezes que um voucher tentará ser ativado", Integer.class, 0),
    TTL_DESFAZIMENTO("TTL_DESFAZIMENTO", "Quantidade de vezes que um voucher tentará ser desfeito (15 em 15 minutos por 24 hrs)", Integer.class, 1),
    TEMPO_ENVIO_CANCELAMENTO("TEMPO_ENVIO_CANCELAMENTO", "Tempo para aguardar para o reenvio de cancelamento Padrão é 15 minutos Paramentro em milisegundos", Long.class, 15000L),
    BLACKHAWK_JKS("BLACKHAWK_JKS", "Caminho para o key store da BlackHawk", String.class, "/var/wsenterative/bhn.jks"),
    BLACKHAWK_JKS_PASSWORD("BLACKHAWK_JKS_PASSWORD", "Senha para o key store da BlackHawk", String.class, "masterkey"),
    ENTERATIVE_JKS("ENTERATIVE_JKS", "Caminho para o key store da Enterative", String.class, "/var/wsenterative/enterative.p12"),
    ENTERATIVE_JKS_PASSWORD("ENTERATIVE_JKS_PASSWORD", "Senha para o key store da Enterative", String.class, "chart@123"),
    WEB_WSENTERATIVE_PATH("WEB_WSENTERATIVE_PATH", "Endereço do WSEnterative (WebEnterative)", String.class, "https://enterativeapk.tk:8123/ws/ativacao"),
    WEB_WSENTERATIVE_STATUS_PATH("WEB_WSENTERATIVE_STATUS_PATH", "Endereço do serviço de Status do WSEnterative (WebEnterative)", String.class, "https://enterativeapk.tk:8123/ws/status"),
    WEB_WSENTERATIVE_DOMAIN("WEB_WSENTERATIVE_DOMAIN", "Domínio do WSEnterative (WebEnterative)", String.class, "enterativeapk.tk"),
    WEB_WSENTERATIVE_PORT("WEB_WSENTERATIVE_PORT", "Porta do WSEnterative (WebEnterative)", Integer.class, 8123),
    WEB_WSENTERATIVE_CALLBACKURL("WEB_WSENTERATIVE_CALLBACKURL", "URL para callback da ativação", String.class, "https://enterativeapk.tk/enterative/callback"),
    FILE_SDF_PATH_RECEIVED("FILE_SDF_PATH_RECEIVED", "Caminho para a pasta de arquivos de retorno recebidos da BHN", String.class, "/var/fileenterative/received"),
    FILE_SDF_PATH_PROCESSED("FILE_SDF_PATH_PROCESSED", "Caminho para a pasta de arquivos de retorno processados da BHN", String.class, "/var/fileenterative/processed"),
    FILE_SDF_PATH_ERROR("FILE_SDF_PATH_ERROR", "Caminho para a pasta de arquivos de retorno com erro da BHN", String.class, "/var/fileenterative/error"),
    FILE_SDF_NAME_PATTERN("FILE_SDF_NAME_PATTERN", "Padrão de nome para o arquivo de retorno da BHN (sem máscara de data)", String.class, "BHN_TXN_CHARTBRASIL_"),
    FILE_SDF_EXTENSION("FILE_SDF_EXTENSION", "Extensão do arquivo de retorno da BHN", String.class, "csv"),
    REPORT_OUTPUT_PATH("REPORT_OUTPUT_PATH", "Caminho para a pasta destino dos relatórios PDF", String.class, "/var/enterative/pdf"),
    MAINTENANCE("MAINTENANCE", "Hora da próxima manutenção (ddMMyyy HHmmss)", String.class, "01011970 000000"),
    VERBOSE("VERBOSE", "Indica se a aplicação deve operar em modo VERBOSE", String.class, "S"),
    SHOP_TRANSACTION_CATEGORY_PURCHASE("SHOP_TRANSACTION_CATEGORY_PURCHASE", "ID da categoria de transações de loja [Compra]", Long.class, 1L),
    SHOP_TRANSACTION_CATEGORY_SALE("SHOP_TRANSACTION_CATEGORY_SALE", "ID da categoria de transações de loja [Venda]", Long.class, 2L),
    SHOP_TRANSACTION_CATEGORY_COMMISSION("SHOP_TRANSACTION_CATEGORY_COMMISSION", "ID da categoria de transações de loja [Comissão]", Long.class, 3L),
    SYSTEM_USER("SYSTEM_USER", "ID do usuário a ser utilizado pelo sistema", Long.class, 0L),
    DEFAULT_CUSTOMER_SHOP("DEFAULT_CUSTOMER_SHOP", "Loja padrão para usuários consumidores", Long.class, 12L),
    DEFAULT_CUSTOMER_TERMINAL("DEFAULT_CUSTOMER_TERMINAL", "Terminal padrão para usuários consumidores", String.class, "001"),
    CUSTOMER_EMAILCONFIRMATION_LINK("CUSTOMER_EMAILCONFIRMATION_LINK", "Link para confirmação de email", String.class, "https://enterativeapk.tk/enterative/user/email"),
    DEFAULT_FROM_ADDRESS("DEFAULT_FROM_ADDRESS", "Endereço DE padrão ", String.class, "no-reply@enterative.com.br"),
    PAYMENT_MANAGER_ORDER_URL("PAYMENT_MANAGER_ORDER_URL", "Endereço do serviço de pedidos do gerenciador de pagamentos", String.class, "https://enterativeapk.tk:8125/order"),
    PAYMENT_MANAGER_METHOD_URL("PAYMENT_MANAGER_METHOD_URL", "Endereço do serviço que retorna os métodos de pagamento", String.class, "https://enterativeapk.tk:8125/paymentmethod/list/"),
    PAYMENT_MANAGER_INIT_PAYMENT_URL("PAYMENT_MANAGER_INIT_PAYMENT_URL", "Endereço do serviço de início de pagamento", String.class, "https://enterativeapk.tk:8125/checkout/init/"),
    PAYMENT_MANAGER_PAGSEGURO_CHECKOUT("PAYMENT_MANAGER_PAGSEGURO_CHECKOUT", "Endereço de checkout do PagSeguro", String.class, "https://sandbox.pagseguro.uol.com.br/v2/checkout/payment.html?code="),
    PAYMENT_MANAGER_PAGSEGURO_REDIRECT_URL("PAYMENT_MANAGER_PAGSEGURO_REDIRECT_URL", "Endereço de redirecionamento do PagSeguro (Ativação Fisico)", String.class, "https://enterativeapk.tk/enterative/ativacao/fisico/customer/payment/done/pagseguro"),
    PAYMENT_MANAGER_CIELO_REDIRECT_URL("PAYMENT_MANAGER_CIELO_REDIRECT_URL", "Endereço de redirecionamento da Cielo (Ativação Fisico)", String.class, "https://enterativeapk.tk/enterative/ativacao/fisico/customer/payment/done/cielo"),
    PAYMENT_MANAGER_CHECK_PAYMENT_PAGSEGURO_URL("PAYMENT_MANAGER_CHECK_PAYMENT_PAGSEGURO_URL", "Endereço para verificação de finalização do Pagseguro", String.class, "https://enterativeapk.tk:8125/checkout/checkpay/pagseguro/"),
    PAYMENT_MANAGER_CHECK_PAYMENT_CIELO_URL("PAYMENT_MANAGER_CHECK_PAYMENT_CIELO_URL", "Endereço para verificação de finalização da Cielo", String.class, "https://enterativeapk.tk:8125/checkout/checkpay/cielo/"),
    FILE_SDF_REMOTE_EXTENSION("FILE_SDF_REMOTE_EXTENSION", "Extensão do arquivo remoto", String.class, "txt"),
    FILE_SDF_SFTP_USERNAME("FILE_SDF_SFTP_USERNAME", "Nome do usuário do SFTP dos arquivos SDF", String.class, "ChartBrasil"),
    FILE_SDF_SFTP_PASSWORD("FILE_SDF_SFTP_PASSWORD", "Senha do usuário do SDFP dos arquivos SDF", String.class, "HMyvdKP_xF#m=8{>(Z%)"),
    FILE_SDF_SFTP_URL("FILE_SDF_SFTP_URL", "URL do SFTP dos arquivos SDF", String.class, "bhndysh-ftps.blackhawk-net.com"),
    FILE_SDF_SFTP_PORT("FILE_SDF_SFTP_PORT", "Porta do SFTP dos arquivos SDFP", Integer.class, 21),
    CRON_FILE("CRON_FILE", "Define se o serviço CRON de arquivos deve rodar", String.class, "S"),
    PAYMENT_MANAGER_PAGSEGURO_CART_REDIRECT_URL("PAYMENT_MANAGER_PAGSEGURO_CART_REDIRECT_URL", "Endereço de redirecionamento do PagSeguro (Carrinho de Compras)", String.class, "https://enterativeapk.tk/enterative/cart/pay/done/pagseguro"),
    PAYMENT_MANAGER_CIELO_CART_REDIRECT_URL("PAYMENT_MANAGER_CIELO_CART_REDIRECT_URL", "Endereço de redirecionamento da Cielo (Carrinho de Compras)", String.class, "https://enterativeapk.tk/enterative/cart/pay/done/cielo"),
    PAYMENT_MANAGER_ENTERATIVE_CART_REDIRECT_URL("PAYMENT_MANAGER_ENTERATIVE_CART_REDIRECT_URL", "Endereço de redirecionamento da Enterative (Carrinho de Compras)", String.class, "https://enterativeapk.tk/enterative/cart/pay/done/enterative"),
    PAYMENT_MANAGER_ENTERATIVE_CARD_REDIRECT_URL("PAYMENT_MANAGER_ENTERATIVE_CARD_REDIRECT_URL", "Endereço de redirecionamento da Enterative (Ativação Fisico)", String.class, "https://enterativeapk.tk/enterative/ativacao/fisico/customer/payment/done/enterative"),
    DEFAULT_ACCOUNT_TYPE_USER("DEFAULT_ACCOUNT_TYPE_USER", "Tipo de conta para usuários", Long.class, 2L),
    VIRTUAL_PRIMARY_ACCOUNT_NUMBER("VIRTUAL_PRIMARY_ACCOUNT_NUMBER", "Número fixo a ser enviado com os produtos virtuais", String.class, "6039534201000000024"),
    EPAY_USERNAME("EPAY_USERNAME", "Nome de usuário do E-Pay", String.class, "User_Epay"),
    EPAY_PASSWORD("EPAY_PASSWORD", "Senha do E-Pay", String.class, "123Mudar!"),
    EPAY_APPNAME("EPAY_APPNAME", "Nome do aplicativo do E-Pay", String.class, "TMS"),
    EPAY_JKS("EPAY_JKS", "Caminho para o key store da E-pay", String.class, "/var/wsenterative/epay.jks"),
    EPAY_JKS_PASSWORD("EPAY_JKS_PASSWORD", "Senha para o key store da E-pay", String.class, "masterkey"),
    SMS_APIKEY("SMS_APIKEY", "Nexmo API Key", String.class, "a1e17a4d"),
    SMS_APISECRET("SMS_APISECRET", "Nexmo API Secret", String.class, "vMAl7vv4tFssgHBH"),
    CUSTOMER_FORGOTPASSWORD_LINK("CUSTOMER_FORGOTPASSWORD_LINK", "Link para redefinição de senha", String.class, "https://enterativeapk.tk/enterative/user/forgotpassword"),
    SHOP_TRANSACTION_CATEGORY_REFUND("SHOP_TRANSACTION_CATEGORY_REFUND", "ID da categoria para transações de conta [Refund]", Long.class, 4L),
    MAX_ITEM_PURCHASE_UNITS("MAX_ITEM_PURCHASE_UNITS", "Quantidade máxima de itens", Integer.class, 50);
    
    @Getter private final String name;
    @Getter private final String description;
    @Getter private final Object defaultValue;
    @Getter private final Class<?> clazz;

    ENVIRONMENT_PARAMETER(String name, String description, Class<?> clazz, Object defaultValue) {
        this.name = name;
        this.description = description;
        this.clazz = clazz;
        this.defaultValue = defaultValue;
    }

    public List<ENVIRONMENT_PARAMETER> ordered() {
        return Arrays.stream(ENVIRONMENT_PARAMETER.values()).sorted((e1, e2) -> e1.getName().compareTo(e2.getName())).collect(Collectors.toList());
    }
}
