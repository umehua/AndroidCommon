import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtils {

    private final static String TAG = HttpUtils.class.getSimpleName();

    private static final HostnameVerifier DEFAULT_HV = HttpsURLConnection.getDefaultHostnameVerifier();
    private static final SSLSocketFactory DEFAULT_SF = HttpsURLConnection.getDefaultSSLSocketFactory();

    public static String download(String surl) {

        StringBuilder html = new StringBuilder();

        if (surl.toLowerCase().startsWith("https://")) {
            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    //do nothing，接受任意客户端证书
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    //do nothing，接受任意服务端证书
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    // we trust all hostname
                    return true;
                }
            };

            try {
                URL myURL = new URL(surl);
                HttpsURLConnection conn = (HttpsURLConnection) myURL.openConnection();
                addRequestHeader(conn);

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{tm}, new SecureRandom());
                SSLSocketFactory ssf = sslContext.getSocketFactory();

                conn.setSSLSocketFactory(ssf);
                conn.setHostnameVerifier(hostnameVerifier);

//                gbk?utf8?
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    html.append(line);
                }

                reader.close();

//<**>                 LogUtil.d(TAG, "download() https = "+ html.toString());

            } catch (Exception e) {
//<**>                 LogUtil.w(TAG, "download failed !", e);
            }

        } else if (surl.toLowerCase().startsWith("http://")){
            try {
                URL url = new URL(surl);
                URLConnection conn = url.openConnection();
                addRequestHeader(conn);

//                gbk?utf8?
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    html.append(line);
                }

                reader.close();

//<**>                 LogUtil.d(TAG, "download() http = "+ html.toString());

            } catch (Exception e) {
//<**>                 LogUtil.w(TAG, "download failed !", e);
            }
        }

        return html.toString();
    }

    private static void addRequestHeader(URLConnection conn) {
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Pragma", "no-cache");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:59.0) Gecko/20100101 Firefox/59.0");
    }

    public static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    public static void disableSSLSocket() {
        HttpsURLConnection.setDefaultHostnameVerifier(DEFAULT_HV);
        HttpsURLConnection.setDefaultSSLSocketFactory(DEFAULT_SF);
    }


    public static String getLocalWifiIp(Context context){
        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        String ip = null;
        if(!wm.isWifiEnabled()) {
            return ip;
        }

        try {
            WifiInfo wi = wm.getConnectionInfo();
            int ipAdd = wi.getIpAddress(); // 获取32位整型IP地址
            ip = intToIp(ipAdd); // 把整型地址转换成“*.*.*.*”地址
        } catch (Exception e) {
//            LogUtil.w(TAG, "getLocalWifiIp() failed !", e);
        }

        return ip;
    }

    private static String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

    public static String getLocalIP() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
//<**>             LogUtil.d(TAG, "getLocalIP() failed !", ex);
        }
        return null;
    }
}
