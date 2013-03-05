package org.jspringbot.keyword.http;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SchemeRegistryBean {

    private SchemeRegistry registry;

    public SchemeRegistryBean(SchemeRegistry registry) {
        this.registry = registry;
    }

    public void setAllowUntrusted(boolean allowUntrusted) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

        if(allowUntrusted) {
            SSLSocketFactory sf = new SSLSocketFactory(new TrustAllStrategy(), new AllowAllHostnameVerifier());
            registry.register(new Scheme("https", 443, sf));
        } else {
            registry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        }
    }

    public static class TrustAllStrategy implements TrustStrategy {
        @Override
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return true;
        }
    }
}
