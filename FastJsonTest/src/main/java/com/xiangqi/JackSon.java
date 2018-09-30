package com.xiangqi;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiangqi.entity.Student;

public class JackSon {
	private static final String JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";
	private static final ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		Student student = mapper.readValue(JSON_OBJ_STR, Student.class);
		System.out.println(student);

		String jsonStr = mapper.writeValueAsString(student);
		System.out.println(jsonStr);
	}
}
