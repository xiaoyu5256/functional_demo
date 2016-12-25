package cn.com.chinaratings.functional.demo;

import java.util.Optional;

/**
 * Created by xiaoyu on 2016/12/24.
 */
public class OptionalDemo {
    public static void main(String[] args) {
        Optional<String> optional =  Optional.of("Hello Optional");
        System.out.println(optional.get());
        Optional empty = Optional.empty();
        System.out.println(empty.orElse("没有值就是我"));
        Optional alsoEmpty = Optional.ofNullable(null);
        System.out.println(alsoEmpty.orElse("没有值就是我"));
        Optional normal = Optional.ofNullable("我有值");
        System.out.println(normal.orElse("没有值就是我"));
        Optional<String> exception =  Optional.of(null); //抛异常
    }
}
