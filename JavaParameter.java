import java.util.Map;
import java.util.Properties;

/**
 * Java 程序参数获取的三种方式
 * 
 * @author kdgc
 * @describe 设置环境变量 export a=a
 * @call java -Db=b JavaParameter a1 b2 b3
 */
public class JavaParameter {
	public static void main(String[] args) {
		// 获取程序参数 a1 b2 b3
		divide("main arguments");
		for (String a : args)
			System.out.println(a);

		// 获取环境变量 a=a
		divide("System.getenv()");
		// String a = System.getenv("a");
		Map<String, String> envs = System.getenv();
		for (Map.Entry<String, String> entry : envs.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}

		// 获取虚拟机参数 b=b
		divide("System.getProperties");
		// String b = System.getProperty("b");
		Properties properties = System.getProperties();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	private static void divide(String str) {
		System.out.println("\n\n======" + str + "======");
	}
}
