package com.xianx.demo;

import java.io.File;
import java.io.StringReader;

import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.JSONFactory;
import com.oracle.javafx.jmx.json.JSONReader;

/**
 * @author xx
 * @date 2017年12月18日
 * 
 */
public class demo {
	public static void main(String[] args) {
		//定义文件
		File audioFile = new File("C:\\Users\\dqq\\Desktop\\public\\8k.wav");
		//定义请求路径
		String BASE_URL = "https://speech.platform.bing.com/speech/recognition/%s/cognitiveservices/v1?language=%s&format=%s";
		/**
		 * 定义识别模式  
		 * 1.INTERACTIVE:互动模式 
		 * 2.CONVERSATION:会话模式 
		 * 3.DICTATION:听写模式
		 */
		String RecognitionMode = "INTERACTIVE";
		//定义转译语言
		String language = "zh-CN";
		//1.SIMPLE:简单 2.DETAILED:详细
		String OutputFormat = "SIMPLE";
		//密匙
		String SubscriptionKey = "3b0bf4abb86c4621a54c79ae82d5df5e";
		
		String url = String.format(BASE_URL, RecognitionMode, language, OutputFormat);
		String result = HttpConnector.initialize()
				.url(url)
	            .addHeader(Header.OCP_APIM_SUBSCRIPTION_KEY, SubscriptionKey)
	            .addHeader(Header.ACCEPT, "application/json")
	            .addHeader(Header.TRANSFER_ENCODING, "chunked")
	            .addHeader(Header.CONTENT_TYPE, "audio/wav", "codec=audio/pcm", "samplerate=16000")
	            .post(audioFile);
		
		JSONFactory instance = JSONFactory.instance();
	    JSONReader jsonReader = instance.makeReader(new StringReader(result));
	    JSONDocument jsonDocument = jsonReader.build();
	    System.out.println("文本："+jsonDocument.getString("DisplayText"));
	}
	static final class Header {
		static final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";//key
		static final String CONTENT_TYPE = "Content-Type";//传递类型
		static final String TRANSFER_ENCODING = "Transfer-Encoding";//传输编码
		static final String ACCEPT = "Accept";//接收数据类型
	}

}
