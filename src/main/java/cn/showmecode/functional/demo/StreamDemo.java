package cn.showmecode.functional.demo;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.*;

import static java.util.Arrays.asList;

/**
 * Created by chengguoliang on 2016/12/23.
 */
public class StreamDemo {
    public static class User{
        private Integer id;
        private String username;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }

        public Integer getId() {
            return id;
        }
        public User(Integer id){
            this.id = id;
            this.username = "xiaoyu"+id;
        }
        public User(Integer id,String username){
            this.id = id;
            this.username = username;
        }

    }

    public static List<User> initListData(){
        return IntStream.range(0,100)
                .mapToObj(i->new User(i))
                .collect(Collectors.toList());
    }



    /**
     * stream常用方法演示
     */
    public static void streamFunction(){
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
        Integer maxId = userStream.max(Comparator.comparing(user -> user.getId())).get().getId();
        System.out.println("maxId:"+maxId);

        System.out.println("[1,2,3] sum:"+Stream.of(1,2,3).reduce((x,y)->x+y).get());
    }

    /**
     * 数字操作演示
     */
    public static void numberStream(){
        IntSummaryStatistics stat = IntStream.range(1,100).summaryStatistics();
        System.out.println("[1,..,100] average:"+stat.getAverage());
        System.out.println("[1,..,100] max:"+stat.getMax());
        System.out.println("[1,..,100] min:"+stat.getMin());
        System.out.println("[1,..,100] count:"+stat.getCount());
        System.out.println("[1,..,100] sum:"+stat.getSum());
    }
    /**
     * 收集器演示
     */
    public static  void collectors(List<User> users){
        List intList = Stream.of(2,1,2,3,4,9,5).collect(Collectors.toList());
        System.out.println(intList.stream().map(String::valueOf).collect(Collectors.joining(",","[","]")));
        Set intSet =Stream.of(2,1,2,3,4,9,5).collect(Collectors.toSet());
        System.out.println(intSet.stream().map(String::valueOf).collect(Collectors.joining(",","[","]")));
        Set intTreeSet =Stream.of(2,1,2,3,4,9,5).collect(Collectors.toCollection(TreeSet::new));
        System.out.println(intTreeSet.stream().map(String::valueOf).collect(Collectors.joining(",","[","]")));
        Optional<User> maxIdUser = users.stream().collect(Collectors.maxBy(Comparator.comparing(User::getId)));
        System.out.println("maxIdUser:"+maxIdUser.get().getId());
        Map<Boolean,List<Integer>> numbers =Stream.of(2,1,2,3,4,9,5).collect(Collectors.partitioningBy(x->x%2==0));
        System.out.println(numbers.get(Boolean.TRUE).stream().map(String::valueOf).collect(Collectors.joining(",","Even number:[","]")));
        System.out.println(numbers.get(Boolean.FALSE).stream().map(String::valueOf).collect(Collectors.joining(",","Odd number:[","]")));
        Map<Boolean,Long> numbersAvgMap =Stream.of(2,1,2,3,4,9,5).collect(Collectors.partitioningBy(x->x%2==0,Collectors.counting()));
        System.out.println("Even count:"+numbersAvgMap.get(Boolean.TRUE));
        System.out.println("Odd count:"+numbersAvgMap.get(Boolean.FALSE));
    }

    public static void customCollector(){

         class JoiningColletor implements Collector<String,StringJoiner,String>{
            @Override
            public Supplier<StringJoiner> supplier() {
                return ()-> new StringJoiner(",","[","]");
            }
            @Override
            public BiConsumer<StringJoiner,String> accumulator() {
                return (joiner,str)->joiner.add(str);
            }
            @Override
            public BinaryOperator<StringJoiner> combiner() {
                return (joiner1,joiner2)->joiner1.merge(joiner2);
            }
            @Override
            public Function<StringJoiner,String> finisher() {
                return joiner->joiner.toString();
            }
            @Override
            public Set<Characteristics> characteristics() {
                return new HashSet<>();
            }
        }

        String result = Stream.of("Hello","World").collect(new JoiningColletor());
        System.out.println(result);
    }
    /**
     * 惰性求值演示
     * @param users
     */
    public static void lazyCalcStream(List<User> users){
        Stream newUsersStream = users.stream()
                .filter(user -> user.getId()>95)
                .map(user -> {
                    System.out.println("new user id:"+user.getId());
                    return user;
                });
        System.out.println("Not begin calc");
        Long count = newUsersStream.count();
        System.out.println("calc end "+count);
    }

    /**
     * 方法引用演示
     */
    public static  void methodReference(){
        //静态方法引用
        Function<String,Optional> nullable = Optional::ofNullable;
        Optional<String> nullableOptional = nullable.apply("static method reference");
        System.out.println(nullableOptional.get());
        nullable = x->Optional.ofNullable(x);
        nullableOptional = nullable.apply("static method reference");
        System.out.println(nullableOptional.get());

        //实例方法引用
        User user = new User(1);
        Function<User,String> getUsername =  User::getUsername;
        System.out.println(getUsername.apply(user));
        getUsername =  u -> u.getUsername();
        System.out.println(getUsername.apply(user));

        //构造方法引用
        Function<Integer,User> newUser = User::new;
        User user2 = newUser.apply(2);
        System.out.println(user2.getUsername());
        newUser = x -> new User(x);
        user2 = newUser.apply(2);
        System.out.println(user2.getUsername());


    }



    public static void main(String[] args) {
        List<User> users = initListData();

        //常用方法
//         streamFunction();

//         numberStream();

        //收集器
//        collectors(users);
        //自定义收集器
//        customCollector();


        //惰性求值
//        lazyCalcStream(users);

        //方法引用
//        methodReference();




    }
}
