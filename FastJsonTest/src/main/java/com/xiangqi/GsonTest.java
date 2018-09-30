package com.xiangqi;

import com.google.gson.Gson;
import com.xiangqi.entity.Student;

public class GsonTest {
	private static final String JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";
	private static final Gson gson = new Gson();

	public static void main(String[] args) {
		Student student = gson.fromJson(JSON_OBJ_STR, Student.class);
		System.out.println(student);

		String jsonStr = gson.toJson(student);
		System.out.println(jsonStr);
	}
}
