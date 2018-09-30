package com.xiangqi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xiangqi.entity.Student;

/**
 * Hello world!
 */
public class FastJson {
    private static final String JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";

    @SuppressWarnings("unused")
	public static void main(String[] args) {
        /**
         * 1. 字符串 <-> jsonObject
         */
        // 字符串->json对象（反序列化）
        JSONObject jsonObject11 = JSON.parseObject(JSON_OBJ_STR);
        System.out.println(jsonObject11);

        // json对象->字符串（序列化）
        String jsonStr11 = JSON.toJSONString(jsonObject11);
        // 第二种方法
        String jsonStr12 = jsonObject11.toJSONString();
        System.out.println(jsonStr11);


        /**
         * 2. 字符串 <-> javaBean
         */
        // 字符串->javaBean（使用Gson的思想）
        Student student21 = JSON.parseObject(JSON_OBJ_STR, Student.class);
        System.out.println(student21);
        // 第二种方法, 使用TypeReference<T>类,由于其构造方法使用protected进行修饰,故创建其子类
        Student student22 = JSON.parseObject(JSON_OBJ_STR, new TypeReference<Student>() {
        });

        // javaBean->字符串
        String jsonString21 = JSON.toJSONString(student22);
        System.out.println(jsonString21);


        /**
         * 3. javaBean <-> jsonObject
         */
        Student student3 = new Student("name", 28);
        // Bean->json对象
        String jsonString31 = JSON.toJSONString(student3);
        JSONObject jsonObject31 = JSON.parseObject(jsonString31);
        System.out.println(jsonObject31);
        // 第二种方法
        JSONObject jsonObject32 = (JSONObject) JSON.toJSON(student3);

        JSONObject jsonObject3 = JSON.parseObject(JSON_OBJ_STR);
        // json对象->Bean
        Student student31 = JSON.parseObject(jsonObject3.toJSONString(), Student.class);
        System.out.println(student31);
        // 第二种方法
        Student student32 = JSON.parseObject(jsonObject3.toJSONString(), new TypeReference<Student>() {
        });
        // 第三种方法
        Student student33 = JSON.toJavaObject(jsonObject3,Student.class);

    }
}
