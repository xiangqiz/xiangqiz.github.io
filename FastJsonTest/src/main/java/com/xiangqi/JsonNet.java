package com.xiangqi;

import com.xiangqi.entity.Student;

import net.sf.json.JSONObject;

public class JsonNet {
	private static final String JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";

	public static void main(String[] args) {
		// JSON json = JSONSerializer.toJSON(JSON_OBJ_STR);
		// Object javaObject = JSONSerializer.toJava(json);
		JSONObject jsonObject = JSONObject.fromObject(JSON_OBJ_STR);
		Student student = (Student) JSONObject.toBean(jsonObject, Student.class);
		System.out.println(student);

		JSONObject jsonObject2 = JSONObject.fromObject(student);
		String jsonStr = jsonObject2.toString();
		System.out.println(jsonStr);
	}
}
