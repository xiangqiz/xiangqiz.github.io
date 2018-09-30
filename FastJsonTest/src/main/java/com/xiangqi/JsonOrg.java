package com.xiangqi;

import org.json.JSONObject;

import com.xiangqi.entity.Student;

public class JsonOrg {
	private static final String JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";

	public static void main(String[] args) {
		JSONObject jsonObject = new JSONObject(JSON_OBJ_STR);
		Student student = new Student();
		student.setStudentName(jsonObject.getString("studentName"));
		student.setStudentAge(jsonObject.getInt("studentAge"));
		System.out.println(student);

		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("studentName", student.getStudentName());
		jsonObject2.put("studentAge", student.getStudentAge());
		String jsonStr = jsonObject2.toString();
		System.out.println(jsonStr);
	}
}
