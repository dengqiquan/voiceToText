package com.xianx.demo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

class HttpConnector {

    /**
     * API url.
     */
    private String mUrl;

    /**
     * Request headers.请求头
     */
    private Map<String, List<String>> mHeaders;

    private HttpConnector() {
        this.mHeaders = new HashMap<>();
    }

    /**
     * 使用这个类初始化连接器。
     */
    static HttpConnector initialize() {
        return new HttpConnector();
    }

    /**
     * 配置api url
     */
    HttpConnector url(String url) {
        this.mUrl = url;
        return this;
    }

    /**
     * 向map实体添加一个头。
     */
    HttpConnector addHeader(String key, String value) {
        addHeader(key, new String[] {value});
        return this;
    }

    /**
     * 将一个头部的倍数添加到映射实体
     */
    HttpConnector addHeader(String key, String... values) {
        this.mHeaders.put(key, Arrays.asList(values));
        return this;
    }

    /**
     * post请求
     */
    String post(File audioFile){
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(this.mUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");

            Set<String> keys = this.mHeaders.keySet();
            for (String key : keys) {
                List<String> values = this.mHeaders.get(key);

                StringBuilder builder = new StringBuilder();
                for (int i = 0;i < values.size();i++) {
                    builder.append(values.get(i));
                    if (i != values.size() - 1) {
                        // Header separator - e.g. "application/json; charset=utf-8"
                        builder.append("; ");
                    }
                }

                urlConnection.setRequestProperty(key, builder.toString());
            }
            urlConnection.connect();

            os = urlConnection.getOutputStream();
            FileInputStream fis = new FileInputStream(audioFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int bytesReaded;
            int bufferSize = 1024;
            byte[] fileBuffer = new byte[bufferSize];
            while ((bytesReaded = bis.read(fileBuffer)) > 0) {
                os.write(fileBuffer, 0, bytesReaded);
                os.flush();
            }
            os.close();

            is = urlConnection.getInputStream();

            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            try (Reader in = new InputStreamReader(is, "UTF-8")) {
                // TODO Check if `CHINESE` letters can be read as `UTF-8`.
                for (;;) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
            }
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null)
                    os.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (urlConnection != null)
                urlConnection.disconnect();
        }
		return mUrl;
    }

}
