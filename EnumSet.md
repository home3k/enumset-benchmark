### EnumSet

EnumSet是JDK 1.5推出的，针对Enum类型的特殊的Set数据接口。这种数据结构只能由同一种类型的Enum类型进行创建。

在数据结构内部，它使用了Bit Vectors来辅助操作，因此，操作性能极其高效。
同时，其Iterator是弱一致的（Weakly Consistent）因此针对写操作不会抛出ConcurrentModificationException
Null元素不允许插入，它不是线程安全的。

EnumSet设计为抽象类，因此只能通过静态工厂来进行创建，整个创建过程EnumSet进行了进一步的封装

```java
Enum<?>[] universe = getUniverse(elementType);
if (universe == null)
    throw new ClassCastException(elementType + " not an enum");

if (universe.length <= 64)
    return new RegularEnumSet<>(elementType, universe);
else
    return new JumboEnumSet<>(elementType, universe);
```

当Enum元素个数<=64时，返回RegularEnumSet，反之，返回JumboEnumSet

### RegularEnumSet

对于RegularEnumSet

使用elements作为位向量，通过每一位来表征Set元素的是否存在

```java
/**
 * Bit vector representation of this set.  The 2^k bit indicates the
 * presence of universe[k] in this set.
 */
private long elements = 0L;
```

看下其addAll函数

```java
void addAll() {
    if (universe.length != 0)
        elements = -1L >>> -universe.length;
}
```

RegularEnumSet使用 `-1L >>> -universe.length`来进行位向量设置。

这个操作是什么意思？

先从位操作开始，Java的位操作符有三个， << >> >>> 左移，有符号右移，无符号右移。对于无符号右移操作符

[Shift Operators](https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.19)
>>> If the promoted type of the left-hand operand is int, only the five lowest-order bits of the right-hand operand are used as the shift distance. It is as if the right-hand operand were subjected to a bitwise logical AND operator & with the mask value 0x1f (0b11111). The shift distance actually used is therefore always in the range 0 to 31, inclusive.
If the promoted type of the left-hand operand is long, then only the six lowest- order bits of the right-hand operand are used as the shift distance. It is as if the right-hand operand were subjected to a bitwise logical AND operator & with the mask value 0x3f (0b111111). The shift distance actually used is therefore always in the range 0 to 63, inclusive.

即：左操作符为int时，右操作符只有低5位有效，右操作数会与0x1f做and操作，如果左操作数是long，则右操作数只有低6位有效，会与0xbf做and操作。
但是，左右操作数都是负数又是什么意思？
再从计算机的原码，反码和补码说起，
一个数在计算机中的二进制表示， 叫做这个数的**机器数**。 机器数用最高位来存放符号，0表示正数，1表示负数。
比如 0000 0001 表示 1， 1000 0001 表示 -1， 00000001 10000001均为机器数。
因为符号位的存在，机器数并非真实数值，即**真值**，10000001的真值为-1而非129
**原码**：符号位与真值的绝对值， 0000 0001, 1000 0001
**反码**：正数的反码为其本身，负数的反码符号位不变，其他位取反，1000 0001 原 = 1111 1110 反
**补码**：正数的补码为祁本身，负数的补码符号位不变，其他位取反后+1 ，1000 0001 原 = 1111 1110 反 = 1111 1111 补
为什么要有这三种码？ 只有原码具有识别含义，符号位的引入会导致机器识别数字的复杂度，期望的结果是符号位参与运算。即补码方式。
回到前面， -1L的，机器数为， 0xffffffffffffffff （补码），>>> 为无符号右移，右移后左侧补零。假设-universe.length = -5，则其机器数为0xfffffffb，由于只有后6位生效，因此其机器数为0x3b = 59，也就是0xffffffffffffffff右移59位，高位补零。其结果为 0.... 01 1111，即低五位为1，即长度为5，5个元素都存在。
`-1L >>> -n [1,64]，是一个常用技巧，生成长度为n的bit vectors.`
```java
public boolean add(E e) {
    typeCheck(e);

    long oldElements = elements;
    elements |= (1L << ((Enum<?>)e).ordinal());
    return elements != oldElements;
}
```
```java
public boolean remove(Object e) {
    if (e == null)
        return false;
    Class<?> eClass = e.getClass();
    if (eClass != elementType && eClass.getSuperclass() != elementType)
        return false;

    long oldElements = elements;
    elements &= ~(1L << ((Enum<?>)e).ordinal());
    return elements != oldElements;
}
```
RegularEnumSet的add和remove操作实现就非常容易理解了，及将bit vector对于的bit位设置成0或者1。
### JumboEnumSet
对于超过64个元素的Enum，使用long elements作为bit vector显然无法描述，解决办法是，使用long输入，缺多少，补多少个long
```java
/**
 * Bit vector representation of this set.  The ith bit of the jth
 * element of this array represents the  presence of universe[64*j +i]
 * in this set.
 */
private long elements[];
```
数组长度如何确定？
```java
JumboEnumSet(Class<E>elementType, Enum<?>[] universe) {
    super(elementType, universe);
    elements = new long[(universe.length + 63) >>> 6];
}
```
`(universe.length+64)>>>6`  右移6位 = 除以2^6，即除以64，长度直接除64，显然不对，比如68/64=1，此时显然应该为2，因此，通过+63再进行该除法，从而确定了数组长度。
对于操作：
```java
void addAll() {
    for (int i = 0; i < elements.length; i++)
        elements[i] = -1;
    elements[elements.length - 1] >>>= -universe.length;
    size = universe.length;
}
```
对于addAll，显然，最后一个数组元素之前的所有long，全部置为-1，对于最后一个元素，进行 -1 >>> - universe.length即可（由于只取低6位，不用关注其数值到底多少）
```java
public boolean add(E e) {
    typeCheck(e);

    int eOrdinal = e.ordinal();
    int eWordNum = eOrdinal >>> 6;

    long oldElements = elements[eWordNum];
    elements[eWordNum] |= (1L << eOrdinal);
    boolean result = (elements[eWordNum] != oldElements);
    if (result)
        size++;
    return result;
}
```
```java
public boolean remove(Object e) {
    if (e == null)
        return false;
    Class<?> eClass = e.getClass();
    if (eClass != elementType && eClass.getSuperclass() != elementType)
        return false;
    int eOrdinal = ((Enum<?>)e).ordinal();
    int eWordNum = eOrdinal >>> 6;

    long oldElements = elements[eWordNum];
    elements[eWordNum] &= ~(1L << eOrdinal);
    boolean result = (elements[eWordNum] != oldElements);
    if (result)
        size--;
    return result;
}
```
对于add和remove操作，首先确定操作元素位于数组的哪个索引 `ordinal >>> 6` 当前位置除以64获得，然后对确定索引的elements的long进行对应的bit的设置。 对于`1L<<eOrdinal` （由于只取低6为，不用关注祁具体的数值）

