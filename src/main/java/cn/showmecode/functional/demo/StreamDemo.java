package cn.showmecode.functional.demo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
    }

    public static List<User> initListData(){
        return IntStream.range(0,100)
                .mapToObj(i->new User(i))
                .collect(Collectors.toList());
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
        Integer minId = userStream.min(Comparator.comparing(user -> user.getId())).get().getId();
        System.out.println("maxId:"+maxId+",minId:"+minId);
    }

    public static void main(String[] args) {
        List<User> users = initListData();
        //惰性求值
        //lazyCalcStream(users);
        //常用方法
        streamFunction();

    }
}
