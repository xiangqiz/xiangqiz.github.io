package com.xiangqi.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.xiangqi.entity.Student;

/**
 * 利用jdk中自带的转换类实现
 * 
 * @XmlRootElement(name = "ROOT")
 * @XmlElement(name = "KEY")
 * 
 * @author Xiangqi
 */
public class JAXBText {

	public static void main(String[] args) throws JAXBException {
		Student student = new Student("name", 28);

		// 利用jdk中自带的转换类实现
		JAXBContext context = JAXBContext.newInstance(student.getClass());

		// JavaBean -> String
		Marshaller marshaller = context.createMarshaller();

		// 格式化xml输出的格式
		// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		// 是否省略xml头信息（<?xml version="1.0" encoding="gb2312" standalone="yes"?>）
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		StringWriter writer = new StringWriter();
		// 将对象转换成输出流形式的xml，需要在对象上添加 @XmlRootElement注解
		marshaller.marshal(student, writer);
		String xmlString = writer.toString();
		System.out.println(xmlString);

		// String -> JavaBean

		// 进行将Xml转成对象的核心接口
		Unmarshaller unmarshaller = context.createUnmarshaller();
		StringReader reader = new StringReader(xmlString);
		Student student2 = (Student) unmarshaller.unmarshal(reader);
		System.out.println(student2);
	}
}