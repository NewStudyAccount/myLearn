package com.example;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class TestService {




    ExecutorService threadPool = Executors.newFixedThreadPool(100);


    public List<User> generateUsers(int j){

        List<User> userList = new ArrayList<>();

        for (int i = 0; i < j; i++) {
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
        System.out.println("UserService: 正在生成验证码 -> " + userId);
        return "CODE"+userId;
    }

    public void handleData(){
        List<User> userList = generateUsers(100);
        System.out.println("开始处理数据");
        long start = System.currentTimeMillis();
//        handleWithThread(userList);

        //9091ms 1037ms
//        CopyOnWriteArrayList<String> strings = handleWithAsyncProcessing(userList);
        //1138ms   1028ms
        CopyOnWriteArrayList<String> strings = handleWithVirtualThread(userList);


        long end = System.currentTimeMillis();
        System.out.println("处理完成，耗时：" + (end - start) + "ms");
        System.out.println("处理完成，错误列表：" + strings.size());
        System.out.println("处理完成，错误列表：" + strings);

    }



    public CopyOnWriteArrayList<String> handleWithAsyncProcessing(List<User> userList) {
        CopyOnWriteArrayList<String> errorList = new CopyOnWriteArrayList<>();


        List<CompletableFuture<Void>> futures = userList.stream()
                .map(user -> CompletableFuture.runAsync(() -> {
                    int id = user.getId();
                    String errorMessage = "";

                    if (id % 10 == 0) {
                        errorMessage = "处理用户" + id + "时发生错误";
                        errorList.add(errorMessage);
                    } else {
                        try {
                            String code = generateValidCode(id);
                            user.setValidCode(code);
                        } catch (Exception e) {
                            errorMessage = "处理用户" + id + "时发生错误";
                            errorList.add(errorMessage);
                        }
                    }
                }, threadPool))
                .collect(Collectors.toList());

        // 等待所有任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 过滤掉有问题的用户
        userList.removeIf(user -> errorList.contains("处理用户" + user.getId() + "时发生错误"));

        return errorList;
        //模拟插入数据库耗时
//        for (User user : userList) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }

    }



    public CopyOnWriteArrayList<String> handleWithVirtualThread(List<User> userList) {
        CopyOnWriteArrayList<String> errorList = new CopyOnWriteArrayList<>();
        CountDownLatch latch = new CountDownLatch(userList.size()); // 初始化计数器

        // 使用虚拟线程
        userList.stream().forEach(user -> Thread.startVirtualThread(() -> {
            int id = user.getId();
            String errorMessage = "";

            try {
                if (id % 10 == 0) {
                    errorMessage = "处理用户" + id + "时发生错误";
                    errorList.add(errorMessage);
                } else {
                    try {
                        String code = generateValidCode(id);
                        user.setValidCode(code);
                    } catch (Exception e) {
                        errorMessage = "处理用户" + id + "时发生错误";
                        errorList.add(errorMessage);
                    }
                }
            } finally {
                latch.countDown(); // 完成时递减计数器
            }
        }));

        try {
            latch.await(); // 等待所有虚拟线程完成
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        userList.removeIf(user -> errorList.contains("处理用户" + user.getId() + "时发生错误"));

        return errorList;
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
