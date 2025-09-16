# 抽象类
BaseFileReader.java
抽象类中定义抽象方法readFile() 或 通用方法readFile()

实现类LowercaseFileReader.java，UppercaseFileReader.java
实现类实现具体的抽象方法。


使用场景
   当多个类有共同的属性和方法时
   需要强制子类实现某些方法时
   设计框架或模板模式时
与接口的区别
   抽象类可以有构造方法，接口不能
   抽象类可以有成员变量，接口只能有常量
   类只能单继承抽象类，但可以实现多个接口

4. 推荐做法
   如果只需要使用公共接口，保持使用抽象类引用
   如果确实需要子类特有功能，考虑是否应该将方法提升到抽象类中
   必要时使用类型转换，但要注意类型安全检查
   这样既保持了代码的灵活性，又能访问所需的特定功能。

7. 设计理念
   抽象类：
   表示"is-a"关系（是什么）
   用于代码复用和统一子类行为
   适合有共同属性和方法的类
   接口：
   表示"can-do"关系（能做什么）
   定义契约和能力
   适合定义服务和能力规范

9. 选择原则
   使用抽象类当你：
   需要共享代码和状态
   有共同的属性和方法实现
   需要定义构造方法
   使用接口当你：
   需要多重继承
   定义服务契约
   实现特定能力（如 Comparable、Runnable）
   需要解耦设计
   总的来说，抽象类更适合用于代码复用和建立类层次结构，而接口更适合定义规范和实现多重继承。

03、两者差别
1）语法层面上
抽象类可以提供成员方法的实现细节，而接口中只能存在 public abstract 方法；
抽象类中的成员变量可以是各种类型的，而接口中的成员变量只能是 public static final 类型的；
接口中不能含有静态代码块，而抽象类可以有静态代码块；
一个类只能继承一个抽象类，而一个类却可以实现多个接口。
2）设计层面上
抽象类是对一种事物的抽象，即对类抽象，继承抽象类的子类和抽象类本身是一种 is-a 的关系。而接口是对行为的抽象。抽象类是对整个类整体进行抽象，包括属性、行为，但是接口却是对类局部（行为）进行抽象。

举个简单的例子，飞机和鸟是不同类的事物，但是它们都有一个共性，就是都会飞。那么在设计的时候，可以将飞机设计为一个类 Airplane，将鸟设计为一个类 Bird，但是不能将 飞行 这个特性也设计为类，因此它只是一个行为特性，并不是对一类事物的抽象描述。

此时可以将 飞行 设计为一个接口 Fly，包含方法 fly()，然后 Airplane 和 Bird 分别根据自己的需要实现 Fly 这个接口。然后至于有不同种类的飞机，比如战斗机、民用飞机等直接继承 Airplane 即可，对于鸟也是类似的，不同种类的鸟直接继承 Bird 类即可。从这里可以看出，继承是一个 "是不是"的关系，而 接口 实现则是 "有没有"的关系。如果一个类继承了某个抽象类，则子类必定是抽象类的种类，而接口实现则是有没有、具备不具备的关系，比如鸟是否能飞（或者是否具备飞行这个特点），能飞行则可以实现这个接口，不能飞行就不实现这个接口。

接口是对类的某种行为的一种抽象，接口和类之间并没有很强的关联关系，举个例子来说，所有的类都可以实现 Serializable 接口，从而具有序列化的功能，但不能说所有的类和 Serializable 之间是 is-a 的关系。

抽象类作为很多子类的父类，它是一种模板式设计。而接口是一种行为规范，它是一种辐射式设计。什么是模板式设计？最简单例子，大家都用过 ppt 里面的模板，如果用模板 A 设计了 ppt B 和 ppt C，ppt B 和 ppt C 公共的部分就是模板 A 了，如果它们的公共部分需要改动，则只需要改动模板 A 就可以了，不需要重新对 ppt B 和 ppt C 进行改动。而辐射式设计，比如某个电梯都装了某种报警器，一旦要更新报警器，就必须全部更新。也就是说对于抽象类，如果需要添加新的方法，可以直接在抽象类中添加具体的实现，子类可以不进行变更；而对于接口则不行，如果接口进行了变更，则所有实现这个接口的类都必须进行相应的改动。





# 字符串不可变性在这段代码中的体现
1. 字符串不可变性的基本概念
   在Java中，String对象一旦创建就不能被修改。任何对String的操作都会创建新的String对象，而不是修改原有对象。
2. 代码中的具体体现
   // 1. 创建User对象
   String username = "沉默王二";
   String password = "123456";
   User user = new User(username, password);

// 2. 获取凭据数组
String[] credentials = getUserCredentials(user);

// 3. 尝试修改数组中的字符串
credentials[0] = "陈清扬";  // 这里只是改变了数组元素的引用
credentials[1] = "612311";  // 并没有修改原始的String对象

3. 内存层面的解释
```java
   // getUserCredentials方法内部
   public static String[] getUserCredentials(User user) {
   String[] credentials = new String[2];
   credentials[0] = user.getUsername(); // credentials[0]指向"沉默王二"对象
   credentials[1] = user.getPassword(); // credentials[1]指向"123456"对象
   return credentials;
   }
```

内存结构示意：
```java
User对象:
username引用 -> "沉默王二" String对象
password引用 -> "123456" String对象

credentials数组:
credentials[0]引用 -> "沉默王二" String对象 (与User.username指向同一对象)
credentials[1]引用 -> "123456" String对象 (与User.password指向同一对象)

执行 credentials[0] = "陈清扬" 后:
credentials[0]引用 -> "陈清扬" String对象 (新的String对象)
User.username引用 -> "沉默王二" String对象 (原对象未改变)
```

4. 不可变性的关键体现
   原始String对象未被修改：尽管我们试图通过数组修改凭据，但原始 User 对象中的username和password保持不变
   引用的重新指向：credentials[0] = "陈清扬" 只是让数组元素指向新的String对象，而不是修改原有String对象
   数据安全性：由于String的不可变性，敏感信息如密码不会被意外修改
5. 验证不可变性的代码
   如果String是可变的，我们可能会期望以下行为：

```java
// 如果String可变，这可能会影响User对象（但实际上不会）
String temp = user.getUsername();
temp = "新用户名"; // 这不会影响user.getUsername()的值
System.out.println(user.getUsername()); // 仍然是"沉默王二"
```