package com.xiangqi.xml;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.xiangqi.entity.Student;

public class FasterXml {

	private static ObjectMapper mapper = new XmlMapper();

	public static void main(String[] args) throws IOException {
		Student student = new Student("name", 28);

		// JavaBean -> String
		String xmlString = mapper.writeValueAsString(student);
		System.out.println(xmlString);

		// String -> JavaBean
		Student student2 = mapper.readValue(xmlString, Student.class);
		System.out.println(student2);
	}
}