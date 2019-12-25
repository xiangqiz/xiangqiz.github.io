package com.xiangqi.xml;

import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.xiangqi.entity.Student;

public class XStreamTest {

	public static void main(String[] args) {
		Student student = new Student("name", 28);

		XStream xstream = new XStream(new MyXppDriver(false));
		xstream.autodetectAnnotations(true);

		// JavaBean -> String

		// 支持注解
		// xstream.processAnnotations(Student.class);
		String xmlString = xstream.toXML(student);
		System.out.println(xmlString);

		// String -> JavaBean

		// xstream.processAnnotations(Student.class);
		Student student2 = (Student) xstream.fromXML(xmlString);
		System.out.println(student2);
	}

	static class MyXppDriver extends XppDriver {
		boolean useCDATA = false;

		public MyXppDriver(boolean useCDATA) {
			super(new XmlFriendlyNameCoder("__", "_"));
			this.useCDATA = useCDATA;
		}

		@Override
		public HierarchicalStreamWriter createWriter(Writer out) {
			if (!useCDATA) {
				return super.createWriter(out);
			}
			return new PrettyPrintWriter(out) {
				boolean cdata = true;

				@Override
				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write(cDATA(text));
					} else {
						writer.write(text);
					}
				}

				private String cDATA(String text) {
					return "<![CDATA[" + text + "]]>";
				}
			};
		}
	}
}