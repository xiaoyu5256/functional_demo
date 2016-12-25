package cn.showmecode.functional.demo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.*;

/**
 * Created by chengguoliang on 2016/12/23.
 */
public class LambdaDemo {
    //定义一个函数式接口
    @FunctionalInterface
    public interface Worker {
        public void doWork();
    }

    public static class Executor{
        private void execute(Worker worker){
            worker.doWork();
        }
    }




    public static void main(String[] args) {
        //类型推断
        String[] strArr = {"Hello","World"};
        HashMap<String,Integer> map = new HashMap<>();

        //函数接口
        Predicate<Integer> lessThanZero = x->x<0;
        System.out.println("5 is Less Than Zero?" + lessThanZero.test(5));

        BinaryOperator<Integer> area = (x, y)-> x*y;
        System.out.println("width:"+5+",height:"+10+",area:"+area.apply(5,10));

        UnaryOperator<Integer> addOne = x ->  x+1;
        System.out.println("4 + 1 = "+ addOne.apply(4));

        Consumer<String> sayHi = x-> System.out.println("Hi,"+x);
        sayHi.accept("祝大师");

        Function<String,Integer> parseInt = x -> Integer.parseInt(x);
        System.out.println(parseInt.apply("12"));

        Supplier<Double> getPi = ()->Math.PI;
        System.out.println(getPi.get());

        Runnable helloWorld = () -> System.out.println("hello world");
        new Thread(helloWorld).start();

        Integer[] scores = {90,76,23,45,41};
        Arrays.sort(scores, (x, y)-> x-y);
        Arrays.stream(scores).forEach((x)-> System.out.println(x));


        //自定义函数接口
        Executor executor = new Executor();
        executor.execute(()-> System.out.println("export data"));

    }


}
