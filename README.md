# Java函数式编程
---
# Lambda
---
# Lambda
## 类型推断

    !java
    String[] strArr = {"Hello","World"};
    HashMap<String,Integer> map = new HashMap<>();
---
# Lambda
## java8 函数接口

- Predicate<T>: 判断接口，参数T，返回boolean
- BinaryOperator<T>: 二元操作符， 传入的两个参数的类型和返回类型相同， 继承BiFunction
- UnaryOperator<T>: 一元操作符， 继承Function,传入参数的类型和返回类型相同
- Consumer<T>: 传入一个参数，无返回值，纯消费
- Function<T>: 传入一个参数，返回一个结果
- Supplier<T>: 无参数传入，返回一个结果
---
# Lambda
## java8 之前函数接口
- java.lang.Runnable
- java.util.concurrent.Callable
- java.util.Comparator
- java.io.FileFilter
- java.security.PrivilegedAction
- java.nio.file.PathMatcher
- java.lang.reflect.InvocationHandler
- java.beans.PropertyChangeListener
- java.awt.event.ActionListener
- javax.swing.event.ChangeListener


---
# Predicate
代码实现：

    !java
    @FunctionalInterface
    public interface Predicate<T> {
        boolean test(T t);
    }

使用：

    !java
    Predicate<Integer> lessThanZero = x->x<0;
    System.out.printf("5 is Less Than Zero?" + lessThanZero.test(5));

---
# BinaryOperator
二元操作符， 传入的两个参数的类型和返回类型相同
代码实现：

    !java
    @FunctionalInterface
    public interface BinaryOperator<T> extends BiFunction<T,T,T> {
        public static <T> BinaryOperator<T> minBy(Comparator<? super T> comparator) {
            Objects.requireNonNull(comparator);
            return (a, b) -> comparator.compare(a, b) <= 0 ? a : b;
        }
        public static <T> BinaryOperator<T> maxBy(Comparator<? super T> comparator) {
            Objects.requireNonNull(comparator);
            return (a, b) -> comparator.compare(a, b) >= 0 ? a : b;
        }
    }
    @FunctionalInterface
    public interface BiFunction<T, U, R> {
        R apply(T t, U u);
        default <V> BiFunction<T, U, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (T t, U u) -> after.apply(apply(t, u));
        }
    }

---
# BinaryOperator
代码示例

    !java
    BinaryOperator<Integer> area = (x, y)-> x*y;
    System.out.println("width:"+5+",height:"+10+",area:"+area.apply(5,10));
---
# Consumer
传入一个参数，无返回值，纯消费
代码实现：

    !java
    @FunctionalInterface
    public interface Consumer<T> {
        void accept(T t);
        default Consumer<T> andThen(Consumer<? super T> after) {
            Objects.requireNonNull(after);
            return (T t) -> { accept(t); after.accept(t); };
        }
    }
代码示例：

    !java
    Consumer<String> sayHi = x-> System.out.println("Hi,"+x);
    sayHi.accept("祝大师");
---
# Function
传入一个参数，返回一个结果
代码实现：

    !java
    @FunctionalInterface
    public interface Function<T, R> {
        R apply(T t);
        default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
            Objects.requireNonNull(before);
            return (V v) -> apply(before.apply(v));
        }
        default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (T t) -> after.apply(apply(t));
        }
        static <T> Function<T, T> identity() {
            return t -> t;
        }
    }
代码示例：

    !java
    Function<String,Integer> parseInt = x -> Integer.parseInt(x);
    System.out.println(parseInt.apply("12"));

---
# Supplier
无参数传入，返回一个结果

代码实现：
    !java
    @FunctionalInterface
    public interface Supplier<T> {
        T get();
    }

代码示例:
    !java
    Supplier<Double> getPi = ()->Math.PI;
    System.out.println(getPi.get());


---
# Stream
---
# Stream 惰性求值

Stream创建的时候不会计算，只有产生值的时候才会计算，判断是否求值：看返回结果，如果返回的是stream则没有求值，返回其他的值才会计算

代码示例：

    !java
    Stream newUsersStream = users.stream()
            .filter(user -> user.getId()>95)
            .map(user -> {
                System.out.println("new user id:"+user.getId());
                return user;
            });
    System.out.println("Not begin calc");
    Long count = newUsersStream.count();
    System.out.println("calc end "+count);

---
# Stream 常用方法
- of(...) 已参数创建一个stream
- map(Function) 转换成另外一个值，得到新的集合
- filter(Predicate) 过滤掉集合中的元素，得到新的集合
- collect(toList()) ：将stream转换成一个list
- flatMap(Function): 将多个stream合并成一个stream返回
- min(Comparator): 获取最小值
- max(Comparator): 获取最大值

---
# Stream 代码示例：

    !java
    User user1 = new User(1);
    User user2 = new User(2);
    User user3 = new User(3);
    List<User> users = Stream
        .of(user1,user2,user3)  //创建Stream
        .map(user->{
            user.setUsername(user.username.toUpperCase());
            return user;
            })  //将User集合映射为用户名的集合
        .filter(user->user.getId()>1)
        .collect(Collectors.toList());
    users.stream().forEach(user-> System.out.println(user.getUsername()));
    Stream<User> userStream = Stream.of(asList(user1),asList(user2,user3))
        .flatMap(userlist->userlist.stream());
    Integer maxId = userStream.max(
            Comparator.comparing(user -> user.getId())).get().getId();
    Integer minId = userStream.min(
            Comparator.comparing(user -> user.getId())).get().getId();
    System.out.println("maxId:"+maxId+",minId:"+minId);
---
