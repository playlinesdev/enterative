package br.com.enterative.demo.service;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "enterative")
public class Configuration {
	private String jkspath;
	private String jkspassword;
	private String webserviceDomain;
	private String webservicePort;
	private String urlActivation;
	private String urlStatus;

	public String getJkspath() {
		return jkspath;
	}

	public void setJkspath(String jkspath) {
		this.jkspath = jkspath;
	}

	public String getJkspassword() {
		return jkspassword;
	}

	public void setJkspassword(String jkspassword) {
		this.jkspassword = jkspassword;
	}

	public String getWebserviceDomain() {
		return webserviceDomain;
	}

	public void setWebserviceDomain(String webserviceDomain) {
		this.webserviceDomain = webserviceDomain;
	}

	public String getWebservicePort() {
		return webservicePort;
	}

	public void setWebservicePort(String webservicePort) {
		this.webservicePort = webservicePort;
	}

	public String getUrlActivation() {
		return urlActivation;
	}

	public void setUrlActivation(String urlActivation) {
		this.urlActivation = urlActivation;
	}

	public String getUrlStatus() {
		return urlStatus;
	}

	public void setUrlStatus(String urlStatus) {
		this.urlStatus = urlStatus;
	}
}
