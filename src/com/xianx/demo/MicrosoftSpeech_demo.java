package com.xianx.demo;

import java.io.File;
import java.io.StringReader;
import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.JSONFactory;
import com.oracle.javafx.jmx.json.JSONReader;
import com.xianx.demo.HttpConnector;

/**
 * @author xx
 * @date 2017年12月18日
 * 
 */
public class MicrosoftSpeech_demo {
	
	public static void main(String[] args) {
		MicrosoftSpeech_demo demo = new MicrosoftSpeech_demo();
		System.out.println("文本为："+demo.recognize().getText());
	}
	public SpeechRecognition recognize(){
		//定义文件
		File audioFile = new File("C:\\Users\\dqq\\Desktop\\public\\8k.wav");
		//定义请求路径
		String BASE_URL = "https://speech.platform.bing.com/speech/recognition/%s/cognitiveservices/v1?language=%s&format=%s";
		//
		String RecognitionMode = "INTERACTIVE";
		//
		String language = "zh-CN";
		//
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
	    
	    String status = jsonDocument.getString(Parameter.RECOGNITION_STATUS);
	    Number offset = jsonDocument.getNumber(Parameter.OFFSET);
	    Number duration = jsonDocument.getNumber(Parameter.DURATION);
	    String text = jsonDocument.getString(Parameter.DISPLAY_TEXT);
	    return new SpeechRecognition(offset.longValue(), duration.longValue(), text);
		
	}
	
	
	static final class Header {
		static final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
		static final String CONTENT_TYPE = "Content-Type";//传递类型
		static final String TRANSFER_ENCODING = "Transfer-Encoding";
		static final String ACCEPT = "Accept";
	}
	static final class Parameter {
        static final String RECOGNITION_STATUS = "RecognitionStatus";//识别状态
        static final String OFFSET = "Offset";//
        static final String DURATION = "Duration";//持续时间
        static final String DISPLAY_TEXT = "DisplayText";//显示文本
    }
}
