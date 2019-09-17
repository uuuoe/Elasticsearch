package com.uisftech.elasticsearch.query.pojo.match;

public enum MatchType {
    MATCH,BOOST,RANGE,TERMS,WILDCARD
}
/**
 * 这个声明定义的是一个类，有四个实例，在此尽量不要构建新对象
 * 因此，在比较两个枚举类型的值时，永远不需要调用equals方法，而直接使用"=="就可以了。
 * (equals()方法也是直接使用==,  两者是一样的效果)
 * Java Enum类型的语法结构尽管和java类的语法不一样，应该说差别比较大。
 * 但是经过编译器编译之后产生的是一个class文件。
 * 该class文件经过反编译可以看到实际上是生成了一个类，该类继承了java.lang.Enum<E>。
 *
 * 用法一：常量
 * JDK1.5之前定义常亮都是public static final...
 * 有了枚举可以把相关常量分组到一个枚举类型里。
 * public enum Color{
 *     RED,GREEN,BLANK,YELLOW
 * }
 * 用法二：switch
 * JDK1.6之前的swith语句只支持int,char,enum类型。使用枚举可读性更强。
 * public void change(){
 *     switch(color){
 *         case RED:
 *         ...;
 *         break;
 *         case YELLOW:
 *         ...
 *         break;
 *     }
 * }
 * 用法三：向枚举中添加新方法
 * 如果打算自定义自己的方法，必须在enum实例序列的最后添加一个分号。
 * 并且java要求必须定义enum实例。
 *
 * 用法四：覆盖枚举的方法
 *
 * 用法五：实现接口
 * 所有的枚举都继承自java.lang.Enum类。
 * 由于Java 不支持多继承，所以枚举对象不能再继承其他类
 *
 * 用法六：
 * 使用接口组织枚举。
 * public interface Food{
 *     enum Coffee implements Food{
 *         BLACK_COFFEE,DECAF_COFFEE,LATTE
 *     }
 *     enum Dessert implements Food{
 *         FRUIT,CAKE,GELATO
 *     }
 * }
 * 用法七：
 * 关于枚举集合的使用
 * java.util.EnumSet和java.util.EnumMap是两个枚举集合。
 * EnumSet保证集合中的元素不重复;EnumMap中的 key是enum类型，而value则可以是任意类型。
 *
 */
