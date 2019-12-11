package com.xiangqi;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
         * 1. 字符串 <-> JSONObject
         */
        // 字符串->JSONObject（反序列化）
        JSONObject jsonObject11 = JSON.parseObject(JSON_OBJ_STR);
        JSONObject parse2 = (JSONObject) JSON.parse(JSON_OBJ_STR);

        // JSONObject->字符串（序列化）
        String jsonStr11 = JSON.toJSONString(jsonObject11);
        // 第二种方法
        String jsonStr12 = jsonObject11.toJSONString();
        String jsonStr13 = jsonObject11.toString();

        /**
         * 2. 字符串 <-> JavaBean
         */
        // 字符串->JavaBean（使用Gson的思想）
        Student student21 = JSON.parseObject(JSON_OBJ_STR, Student.class);
        // 第二种方法, 使用TypeReference<T>类,由于其构造方法使用protected进行修饰,故创建其子类
        Student student22 = JSON.parseObject(JSON_OBJ_STR, new TypeReference<Student>() {
        });

        // JavaBean->字符串
        String jsonString21 = JSON.toJSONString(student22);

        /**
         * 3. JavaBean <-> JSONObject
         */
        Student student3 = new Student("name", 28);

        // Bean->JSONObject
        String jsonString31 = JSON.toJSONString(student3);
        JSONObject jsonObject31 = JSON.parseObject(jsonString31);
        // 第二种方法
        JSONObject jsonObject32 = (JSONObject) JSON.toJSON(student3);

        JSONObject jsonObject3 = JSON.parseObject(JSON_OBJ_STR);
        // JSONObject->Bean
        Student student31 = JSON.parseObject(jsonObject3.toJSONString(), Student.class);
        // 第二种方法
        Student student32 = JSON.parseObject(jsonObject3.toJSONString(), new TypeReference<Student>() {
        });
        // 第三种方法
        Student student33 = JSON.toJavaObject(jsonObject3, Student.class);
        Student student34 = jsonObject3.toJavaObject(Student.class);

        /**
         * 4. jsonStr<->List
         */

        Student student41 = new Student("name41", 28);
        Student student42 = new Student("name42", 30);
        List<Student> studentList = Arrays.asList(student41, student42);

        // List->字符串
        String jsonArrayStr = JSON.toJSONString(studentList);
        // 字符串->JSONArray
        JSONArray jsonArray = JSON.parseArray(jsonArrayStr);
        JSONArray parse = (JSONArray) JSON.parse(jsonArrayStr);
        // jsonArray->List
        List<Student> list1 = jsonArray.toJavaList(Student.class);
        List<?> javaObject = jsonArray.toJavaObject(List.class);
        List<Student> list2 = JSON.parseArray(jsonArrayStr, Student.class);
        List<Map<String, Object>> list3 = JSON.parseObject(jsonArrayStr, new TypeReference<List<Map<String, Object>>>() {
                });

    }
}