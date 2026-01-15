package com.example;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TestService {




    ExecutorService threadPool = Executors.newFixedThreadPool(100);


    public List<User> generateUsers(){

        List<User> userList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            User user = new User(i,"张三"+i, 18+i);

            userList.add( user);

        }

        return userList;
    }

    public String generateValidCode(Integer userId){
        try {
            //模拟调用耗时1S
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "CODE"+userId;
    }

    public void handleData(){
        List<User> userList = generateUsers();
        System.out.println("开始处理数据");
        long start = System.currentTimeMillis();
        handleWithThread(userList);


        long end = System.currentTimeMillis();
        System.out.println("处理完成，耗时：" + (end - start) + "ms");
    }

    public void handleWithThread(List<User> userList){


        CopyOnWriteArrayList<String> errorList = new CopyOnWriteArrayList();



        Iterator<User> iterator = userList.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            int id = user.getId();
            String errorMessage = "";
            if (id%10==0){
                errorMessage = "处理用户" + id + "时发生错误";
                errorList.add(errorMessage);
                iterator.remove();
            }else {
                String code = null;
                try {
                    code = generateValidCode(id);
                } catch (Exception e) {
                    errorMessage = "处理用户" + id + "时发生错误";
                    errorList.add(errorMessage);
                    iterator.remove();
                    continue;
                }
                user.setValidCode(code);
            }
        }


        //模拟插入数据库耗时
        for (User user : userList) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



    }

    public void handleWithVirtualThread(){

    }


    public class User {

        private int id;
        private String name;
        private int age;
        private String validCode;


        public User() {
        }

        public User(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getValidCode() {
            return validCode;
        }

        public void setValidCode(String validCode) {
            this.validCode = validCode;
        }
    }


}
