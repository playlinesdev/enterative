package br.com.enterative.demo.service;

import java.io.File;
import java.security.cert.X509Certificate;
import java.util.Objects;

import javax.net.ssl.SSLContext;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
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

@Service
public class FluentService {

	@Autowired
	private Configuration config;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public String send(String endpoint, String content, String token, String login) {
		String result = null;
		File jks = null;
		String jksPassword = null;

		try {
			jks = new File((String) config.getJkspath());
			if (!jks.exists()) {
				jks = null;
				log.error("Invalid jks !");
			}
			jksPassword = config.getJkspassword();
		} catch (Exception e) {
			this.log.error(e.getMessage()); 
		}

		if (Objects.isNull(jks) || Objects.isNull(jksPassword)) {
			log.error("Jks has not been configured correctly!");
			return result;
		}

		try {
			// Build SSL Context
			SSLContext sslcontext = SSLContexts.custom()
					.loadTrustMaterial(jks, jksPassword.toCharArray(),(TrustStrategy) (X509Certificate[] arg0, String arg1) -> true)
					.build();

			// SSL Socket Factory
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);

			// Basic HTTP Authentication
			Credentials cred = new UsernamePasswordCredentials(login, token);
			AuthScope scope = new AuthScope(config.getWebserviceDomain(), Integer.parseInt(config.getWebservicePort()));
			CredentialsProvider credentialProvider = new BasicCredentialsProvider();
			credentialProvider.setCredentials(scope, cred);

			//Build conection with SSL
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCredentialsProvider(credentialProvider).build();

			//Execute
			result = Executor.newInstance(httpclient).use(credentialProvider)
					.execute(Request.Post(endpoint)
							.addHeader("Accept", "application/json")
							.addHeader("Content-Type", "application/json")
							.addHeader("Connection", "Keep-Alive")
							.bodyString(content, ContentType.APPLICATION_JSON)
							.connectTimeout(23 * 1000)
							.socketTimeout(23 * 1000))
					.returnContent().asString();

		} catch (HttpResponseException e) {
			log.info("HTTP Error: " + e.getStatusCode());
			if (e.getStatusCode() == 401) {
				log.info("Access denied!");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return result;
	}

}
