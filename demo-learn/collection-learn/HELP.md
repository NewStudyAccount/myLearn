集合
List
向 ArrayList 中添加元素
堆栈过程图示：
add(element)
└── if (size == elementData.length) // 判断是否需要扩容
    ├── grow(minCapacity) // 扩容
    │   └── newCapacity = oldCapacity + (oldCapacity >> 1) // 计算新的数组容量
    │   └── Arrays.copyOf(elementData, newCapacity) // 创建新的数组
    ├── elementData[size++] = element; // 添加新元素
    └── return true; // 添加成功

```java
/**
 * 将指定元素添加到 ArrayList 的末尾
 * @param e 要添加的元素
 * @return 添加成功返回 true
 */
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // 确保 ArrayList 能够容纳新的元素
    elementData[size++] = e; // 在 ArrayList 的末尾添加指定元素
    return true;
}
```
继续跟下去，来看看 ensureCapacityInternal()方法：
```java
/**
 * 确保 ArrayList 能够容纳指定容量的元素
 * @param minCapacity 指定容量的最小值
 */
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) { // 如果 elementData 还是默认的空数组
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity); // 使用 DEFAULT_CAPACITY 和指定容量的最小值中的较大值
    }

    ensureExplicitCapacity(minCapacity); // 确保容量能够容纳指定容量的元素
}
```
此时：

* 参数 minCapacity 为 1（size+1 传过来的）
* elementData 为存放 ArrayList 元素的底层数组，前面声明 ArrayList 的时候讲过了，此时为空 {}
* DEFAULTCAPACITY_EMPTY_ELEMENTDATA 前面也讲过了，为 {}
* 所以，if 条件此时为 true，if 语句minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity)要执行。

DEFAULT_CAPACITY 为 10（见下面的代码），所以执行完这行代码后，minCapacity 为 10，Math.max() 方法的作用是取两个当中最大的那个。
```java
private static final int DEFAULT_CAPACITY = 10;
```

接下来执行 ensureExplicitCapacity() 方法，来看一下源码：
```java
/**
 * 检查并确保集合容量足够，如果需要则增加集合容量。
 *
 * @param minCapacity 所需最小容量
 */
private void ensureExplicitCapacity(int minCapacity) {
    // 检查是否超出了数组范围，确保不会溢出
    if (minCapacity - elementData.length > 0)
        // 如果需要增加容量，则调用 grow 方法
        grow(minCapacity);
}
```
此时：

* 参数 minCapacity 为 10
* elementData.length 为 0（数组为空）
所以 10-0>0，if 条件为 true，进入 if 语句执行 grow() 方法，来看源码：

```java
/**
 * 扩容 ArrayList 的方法，确保能够容纳指定容量的元素
 * @param minCapacity 指定容量的最小值
 */
private void grow(int minCapacity) {
    // 检查是否会导致溢出，oldCapacity 为当前数组长度
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1); // 扩容至原来的1.5倍
    if (newCapacity - minCapacity < 0) // 如果还是小于指定容量的最小值
        newCapacity = minCapacity; // 直接扩容至指定容量的最小值
    if (newCapacity - MAX_ARRAY_SIZE > 0) // 如果超出了数组的最大长度
        newCapacity = hugeCapacity(minCapacity); // 扩容至数组的最大长度
    // 将当前数组复制到一个新数组中，长度为 newCapacity
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```
此时：

* 参数 minCapacity 为 10
* 变量 oldCapacity 为 0
所以 newCapacity 也为 0，于是 newCapacity - minCapacity 等于 -10 小于 0，于是第一个 if 条件为 true，执行第一个 if 语句 newCapacity = minCapacity，然后 newCapacity 为 10。

紧接着执行 elementData = Arrays.copyOf(elementData, newCapacity);，也就是进行数组的第一次扩容，长度为 10。

回到 add() 方法：
```java
public boolean add(E e) {
    ensureCapacityInternal(size + 1);
    elementData[size++] = e;
    return true;
}
```
执行 elementData[size++] = e。

此时：
* size 为 0
* e 为 “沉默王二”
所以数组的第一个元素（下标为 0） 被赋值为“沉默王二”，接着返回 true，第一次 add 方法执行完毕。
PS：add 过程中会遇到一个令新手感到困惑的右移操作符 >>，借这个机会来解释一下。
ArrayList 在第一次执行 add 后会扩容为 10，那 ArrayList 第二次扩容发生在什么时候呢？
答案是添加第 11 个元素时，大家可以尝试分析一下这个过程。





