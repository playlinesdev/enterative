package br.com.chart.enterative.service;

import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.vo.ServiceResponse;

import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Objects;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Encapsula os metodos da classe Fluent
 *
 * @author Cristhiano Roberto
 */
@Service
public class FluentService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EnvParameterDAO parameterDAO;

    public String sendEpay(String endpoint, String envelope, String soapAction) throws IOException, CRUDServiceException {
        String response = null;
        File epay_jks = null;
        String epay_jks_password = null;

        try {
            epay_jks = new File((String) this.parameterDAO.get(ENVIRONMENT_PARAMETER.EPAY_JKS));
            if (!epay_jks.exists()) {
                epay_jks = null;
                log.error("E-Pay jks não existe!");
            }

            epay_jks_password = this.parameterDAO.get(ENVIRONMENT_PARAMETER.EPAY_JKS_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Objects.isNull(epay_jks) || Objects.isNull(epay_jks_password)) {
            log.error("E-Pay jks não foi configurado corretamente!");
            return response;
        }

        try {
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(epay_jks,
                    epay_jks_password.toCharArray(), new TrustSelfSignedStrategy()).build();

            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"},
                    null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            // Cria conexão http informando que deverá criar o socket
            // permitindo o certificado da blackhalk para conexões https
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

            response = Executor.newInstance(httpclient)
                    .execute(Request.Post(endpoint)
                            .addHeader("Accept", "text/xml")
                            .addHeader("Content-Type", "text/xml; charset=utf-8")
                            .addHeader("Connection", "Keep-Alive")
                            .addHeader("SoapAction", soapAction)
                            .bodyString(envelope, ContentType.TEXT_XML)
                            .connectTimeout(23 * 1000)
                            .socketTimeout(23 * 1000))
                    .returnContent().asString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Response sendSimpleJSON(String endpoint, String conteudo) throws IOException, CRUDServiceException {
        try {
            CloseableHttpClient httpclient;

            if (StringUtils.containsIgnoreCase(endpoint, "//localhost")) {
                httpclient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.createDefault(), new String[]{"TLSv1.2"},
                        null, NoopHostnameVerifier.INSTANCE)).build();
            } else {
                httpclient = HttpClients.custom().build();
            }

            return Executor.newInstance(httpclient)
                    .execute(Request.Post(endpoint)
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Connection", "Keep-Alive")
                            .bodyString(conteudo, ContentType.APPLICATION_JSON)
                            .connectTimeout(23 * 1000)
                            .socketTimeout(23 * 1000));
        } catch (HttpHostConnectException e) {
            throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E26), false);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Envia o conteudo para o endpoint validando o SSL com o certificado selecionado {@link}
     * https://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientCustomSSL.java
     *
     * @param endpoint
     * @param conteudo
     * @return
     */
    public String sendBHN(String endpoint, String conteudo) {
        String retorno = null;
        File bhn_jks = null;
        String bhn_jks_password = null;

        try {
            bhn_jks = new File((String) this.parameterDAO.get(ENVIRONMENT_PARAMETER.BLACKHAWK_JKS));
            if (!bhn_jks.exists()) {
                bhn_jks = null;
                log.error("BHN jks não existe!");
            }

            bhn_jks_password = this.parameterDAO.get(ENVIRONMENT_PARAMETER.BLACKHAWK_JKS_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Objects.isNull(bhn_jks) || Objects.isNull(bhn_jks_password)) {
            log.error("BHN jks não foi configurado corretamente!");
            return retorno;
        }

        try {

            // Trust own CA and all self-signed certs
            // src/main/resources/bhn.jks
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(bhn_jks,
                    bhn_jks_password.toCharArray(), new TrustSelfSignedStrategy()).build();

            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1.2"},
                    null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            // Cria conexão http informando que deverá criar o socket
            // permitindo o certificado da blackhalk para conexões https
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

            retorno = Executor.newInstance(httpclient)
                    .execute(Request.Post(endpoint)
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Connection", "Keep-Alive")
                            .bodyString(conteudo, ContentType.APPLICATION_JSON).connectTimeout(23 * 1000)
                            .socketTimeout(23 * 1000))
                    .returnContent().asString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retorno;
    }

    /**
     * Envia o conteudo para o endpoint validando o SSL com o certificado selecionado {@link}
     * https://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientCustomSSL.java
     *
     * @param endpoint
     * @param conteudo
     * @param token
     * @param login
     * @return
     */
    public String sendWSEnterative(String endpoint, String conteudo, String token, String login) {
        String retorno = null;
        File wsenterative_jks = null;
        String wsenterative_jks_password = "";

        try {
            wsenterative_jks = new File((String) this.parameterDAO.get(ENVIRONMENT_PARAMETER.ENTERATIVE_JKS));
            if (!wsenterative_jks.exists()) {
                wsenterative_jks = null;
                log.error("WSEnterative jks não existe!");
            }
            wsenterative_jks_password = this.parameterDAO.get(ENVIRONMENT_PARAMETER.ENTERATIVE_JKS_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Objects.isNull(wsenterative_jks) || Objects.isNull(wsenterative_jks_password)) {
            log.error("WSEnterative jks não foi configurado corretamente!");
            return null;
        }

        try {
            // Build SSL Context
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(wsenterative_jks, wsenterative_jks_password.toCharArray(), (TrustStrategy) (X509Certificate[] arg0, String arg1) -> true).build();

            // SSL Socket Factory
//            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1.2"}, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            // Doesn't check Hostname with Certificate
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);

            String webservice_domain = this.parameterDAO.get(ENVIRONMENT_PARAMETER.WEB_WSENTERATIVE_DOMAIN);
            Integer webservice_port = this.parameterDAO.get(ENVIRONMENT_PARAMETER.WEB_WSENTERATIVE_PORT);

            // Basic HTTP Authentication
            Credentials cred = new UsernamePasswordCredentials(login, token);
            AuthScope scope = new AuthScope(webservice_domain, webservice_port);

            CredentialsProvider credentialProvider = new BasicCredentialsProvider();
            credentialProvider.setCredentials(scope, cred);

            // Cria conexão http informando que deverá criar o socket
            // permitindo o certificado da blackhalk para conexões https
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCredentialsProvider(credentialProvider).build();

            retorno = Executor.newInstance(httpclient)
                    .use(credentialProvider)
                    .execute(Request.Post(endpoint)
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Connection", "Keep-Alive")
                            .bodyString(conteudo, ContentType.APPLICATION_JSON).connectTimeout(23 * 1000)
                            .socketTimeout(23 * 1000))
                    .returnContent().asString();
        } catch (HttpResponseException e) {
            log.info("HTTP Error: " + e.getStatusCode());
            if (e.getStatusCode() == 401) {
                log.info("Acesso não autorizado!");
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retorno;
    }

}
