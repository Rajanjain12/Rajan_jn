package com.kyobee.util.pusherLatest;

import ibt.ortc.api.Ortc;
import ibt.ortc.extensibility.OrtcClient;
import ibt.ortc.extensibility.OrtcFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import com.kyobee.util.AppInitializer;
import com.kyobee.util.common.Constants;

@Component
public class Realtimefamework implements IPusher{
	private static final String defaultServerUrl = "http://ortc-developers.realtime.co/server/2.1";
	private static final boolean defaultIsBalancer = true;
	private static final String defaultApplicationKey = "j9MLMa";
	private static final String defaultPrivateKey = "XQJYeUzTZCCP";
	private static final String defaultAuthenticationToken = "j9MLMaXQJYeUzTZCCP";

	public static String serverUrl = defaultServerUrl;
	private static boolean isBalancer = defaultIsBalancer;
	public static String applicationKey = defaultApplicationKey;
	public static String authenticationToken = defaultAuthenticationToken;
	public static final String channel =AppInitializer.dashBoardChannel;

	public  static OrtcClient client = null;
	public Realtimefamework()
	{
		Ortc api = new Ortc();
		OrtcFactory factory = null;
		try {
			factory = api.loadOrtcFactory("IbtRealtimeSJ");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		client = factory.createClient();
		client.setClusterUrl(serverUrl);
		client.connect(applicationKey,authenticationToken);

	}

	public  void sendViaRealtimeRestApi(Map<String, Object> rootMap, String channel)
	{
		String urlString = getBestServerURL(defaultApplicationKey);
		try {
			if( Constants.REALTIME_API_POST_SUCCESS_CODE == authenticateWebServiceCall(urlString)) {

				HttpPost post = new HttpPost(urlString+"/send");
				List<NameValuePair>  nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("AT", defaultAuthenticationToken));
				nvps.add(new BasicNameValuePair("AK", defaultApplicationKey));
				nvps.add(new BasicNameValuePair("PK", defaultPrivateKey));
				nvps.add(new BasicNameValuePair("C", channel));
				nvps.add(new BasicNameValuePair("M", JSONObject.fromObject(rootMap).toString()));

				HttpClient httpClient = null;
				post.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
				httpClient = HttpClientBuilder.create().build();
				HttpResponse response = httpClient.execute(post);
				System.out.println(response.getStatusLine());

			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private  int authenticateWebServiceCall(String urlString) throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(urlString+"/authenticate");
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("AT", defaultAuthenticationToken));
		nvps.add(new BasicNameValuePair("AK", defaultApplicationKey));
		nvps.add(new BasicNameValuePair("PK", defaultPrivateKey));
		nvps.add(new BasicNameValuePair("C", "RSNT_GUEST_QA"));
		nvps.add(new BasicNameValuePair("PVT", "1"));
		nvps.add(new BasicNameValuePair("TTL", "1800"));
		nvps.add(new BasicNameValuePair("TP", "2"));

		post.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8.toString()));

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse response = httpClient.execute(post);
		return response.getStatusLine().getStatusCode();
	}

	private  String getBestServerURL(String defaultapplicationkey2) {
		HttpURLConnection conn = null;
		String output = null;
		try {
			URL url = new URL("https://ortc-developers.realtime.co/server/2.1?appkey="+defaultApplicationKey);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/x-www-form-urlencoded");
			if (conn.getResponseCode() != Constants.REALTIME_API_GET_SUCCESS_CODE) {
				throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			output = br.readLine();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			conn.disconnect();
		}
		return output.substring(output.indexOf("\"")+1, output.lastIndexOf("\""));
	}

	@Override
	public void sendPusher(Map<String, Object> rootMap, String channel) {
		String urlString = getBestServerURL(defaultApplicationKey);
		try {
			if( Constants.REALTIME_API_POST_SUCCESS_CODE == authenticateWebServiceCall(urlString)) {

				HttpPost post = new HttpPost(urlString+"/send");
				List<NameValuePair>  nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("AT", defaultAuthenticationToken));
				nvps.add(new BasicNameValuePair("AK", defaultApplicationKey));
				nvps.add(new BasicNameValuePair("PK", defaultPrivateKey));
				nvps.add(new BasicNameValuePair("C", channel));
				nvps.add(new BasicNameValuePair("M", JSONObject.fromObject(rootMap).toString()));

				HttpClient httpClient = null;
				post.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
				httpClient = HttpClientBuilder.create().build();
				HttpResponse response = httpClient.execute(post);
				System.out.println(response.getStatusLine());

			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void sucbscribe() {
		// TODO Auto-generated method stub
		
	}

}
