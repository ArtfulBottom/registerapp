package edu.uark.uarkregisterapp.models.api.services;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public abstract class BaseRemoteService {
	protected JSONObject requestSingle(PathElementInterface[] pathElements) {
		return this.requestSingle(pathElements, StringUtils.EMPTY);
	}

	protected JSONObject requestSingle(PathElementInterface[] pathElements, UUID id) {
		return this.requestSingle(pathElements, id.toString());
	}

	protected JSONObject requestSingle(PathElementInterface[] pathElements, String value) {
		URL connectionUrl = this.buildPath(pathElements, value);
		String rawResponse = this.performGetRequest(connectionUrl);

		return this.rawResponseToJSONObject(rawResponse);
	}

	protected JSONObject putSingle(PathElementInterface[] pathElements, JSONObject jsonObject) {
		URL connectionUrl = this.buildPath(pathElements);
		String rawResponse = this.performPutRequest(connectionUrl, jsonObject);

		return this.rawResponseToJSONObject(rawResponse);
	}

	private String performGetRequest(URL connectionUrl) {
		if (connectionUrl == null) {
			return StringUtils.EMPTY;
		}

		HttpURLConnection httpURLConnection = null;
		StringBuilder rawResponse = new StringBuilder();

		try {
			httpURLConnection = (HttpURLConnection) connectionUrl.openConnection();
			httpURLConnection.setRequestMethod(GET_REQUEST_METHOD);
			httpURLConnection.addRequestProperty(ACCEPT_REQUEST_PROPERTY, JSON_PAYLOAD_TYPE);

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

			char[] buffer = new char[1024];
			int readCharacters = bufferedReader.read(buffer, 0, buffer.length);
			while (readCharacters > 0) {
				rawResponse.append(buffer, 0, readCharacters);
				readCharacters = bufferedReader.read(buffer, 0, buffer.length);
			}

			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}

		return rawResponse.toString();
	}

	private String performPutRequest(URL connectionUrl, JSONObject jsonObject) {
		if (connectionUrl == null) {
			return StringUtils.EMPTY;
		}

		HttpURLConnection httpURLConnection = null;
		StringBuilder rawResponse = new StringBuilder();

		try {
			httpURLConnection = (HttpURLConnection) connectionUrl.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setFixedLengthStreamingMode(jsonObject.toString().getBytes(UTF8_CHARACTER_ENCODING).length);
			httpURLConnection.setRequestMethod(PUT_REQUEST_METHOD);
			httpURLConnection.addRequestProperty(ACCEPT_REQUEST_PROPERTY, JSON_PAYLOAD_TYPE);
			httpURLConnection.addRequestProperty(CONTENT_TYPE_REQUEST_PROPERTY, JSON_PAYLOAD_TYPE);

			OutputStream outputStream = httpURLConnection.getOutputStream();
			outputStream.write(jsonObject.toString().getBytes(UTF8_CHARACTER_ENCODING));
			outputStream.flush();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

			char[] buffer = new char[1024];
			int readCharacters = bufferedReader.read(buffer, 0, buffer.length);
			while (readCharacters > 0) {
				rawResponse.append(buffer, 0, readCharacters);
				readCharacters = bufferedReader.read(buffer, 0, buffer.length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}

		return rawResponse.toString();
	}

	private JSONObject rawResponseToJSONObject(String rawResponse) {
		JSONObject jsonObject = null;

		if (!StringUtils.isBlank(rawResponse)) {
			try {
				jsonObject = new JSONObject(rawResponse);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return jsonObject;
	}

	private URL buildPath(PathElementInterface[] pathElements) {
		return this.buildPath(pathElements, StringUtils.EMPTY);
	}

	private URL buildPath(PathElementInterface[] pathElements, String specificationEntry) {
		StringBuilder completePath = new StringBuilder(BASE_URL);

		for (PathElementInterface pathElement : pathElements) {
			String pathEntry = pathElement.getPathValue();

			if (!StringUtils.isBlank(pathEntry)) {
				completePath.append(pathEntry).append(URL_JOIN);
			}
		}

		if (!StringUtils.isBlank(specificationEntry)) {
			completePath.append(specificationEntry);
		}

		URL connectionUrl;
		try {
			connectionUrl = new URL(completePath.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			connectionUrl = null;
		}

		return connectionUrl;
	}

	private static final String URL_JOIN = "/";
	private static final String GET_REQUEST_METHOD = "GET";
	private static final String PUT_REQUEST_METHOD = "PUT";
	private static final String UTF8_CHARACTER_ENCODING = "UTF8";
	private static final String ACCEPT_REQUEST_PROPERTY = "Accept";
	private static final String JSON_PAYLOAD_TYPE = "application/json";
	private static final String CONTENT_TYPE_REQUEST_PROPERTY = "Content-Type";
  private static final String BASE_URL = "http://aeregisterservice.herokuapp.com/";
}
