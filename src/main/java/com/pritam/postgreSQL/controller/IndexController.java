package com.pritam.postgreSQL.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

// https://stackoverflow.com/questions/29965764/how-to-parse-json-file-with-gson

@RestController
public class IndexController {
	
	@GetMapping("/")
    public LinkedHashMap<String, Object> getIndex() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
	    map.put("ServerRuning", true);
	    map.put("ServerDate", (new Date()).toLocaleString());
	    return map;
    }
	
	@GetMapping("/postman")
	public Object sendPostman() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("./postgresql.postman_collection.json"));
			Gson gson = new Gson();
			Object json = gson.fromJson(bufferedReader, HashMap.class);
		    return json;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		    map.put("Exception", true);
		    map.put("Path", System.getProperty("user.dir"));
		    map.put("StackTrace", errors.toString());
		    return map;
		}
		
	}

}