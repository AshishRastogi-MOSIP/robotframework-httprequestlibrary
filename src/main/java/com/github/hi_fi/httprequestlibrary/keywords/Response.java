package com.github.hi_fi.httprequestlibrary.keywords;

import java.util.List;

import java.util.Map;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.hi_fi.httprequestlibrary.domain.Session;
import com.github.hi_fi.httprequestlibrary.utils.RestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@RobotKeywords
public class Response {

	@RobotKeyword("Gets latest response in JSON format from given alias.")
	@ArgumentNames({"alias"})
	public Object getJsonResponse(String alias) {
		RestClient rc = new RestClient();
		Session session = rc.getSession(alias);
		return this.toJson(session.getResponseBody());
	}

	@RobotKeyword("Convert a string to a JSON object.")
	@ArgumentNames({"content"})
	public Object toJson(String data) {
	    if (data.trim().startsWith("{")) {
		    return new Gson().fromJson(data.replace("u'", "'"), Map.class);
	    } else if (data.trim().startsWith("[")) {
	        return new Gson().fromJson(data.replace("u'", "'"), List.class);
        }
        return new Object();
	}
	
	@RobotKeyword("Prints out given string as pretty printed JSON.")
	@ArgumentNames({ "content"})
	public String prettyPrintJson(String content) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
		  return gson.toJson(new JsonParser().parse(content));
		} catch (JsonSyntaxException e) {
		  throw new RuntimeException(String.format("Parsing error when trying to parse %s. \nError message was %s.",
					content, e.getMessage()));
		}
	}

	@RobotKeyword("Returns HTTP status code of latest response for given alias.")
	@ArgumentNames({"alias"})
	public Integer getResponseStatusCode(String alias) {
		return new RestClient().getSession(alias).getResponse().getStatusLine().getStatusCode();
	}

}
