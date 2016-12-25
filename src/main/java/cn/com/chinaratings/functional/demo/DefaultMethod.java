package cn.com.chinaratings.functional.demo;

/**
 * Created by xiaoyu on 2016/12/24.
 */
public class DefaultMethod {
    public interface Person{
        void sayHi();
        default void sayBye(){
            System.out.println("GodBye");
        }
//        static void eat(){
//            System.out.println("吃货在吃");
//        }
    }

    public static class Student implements Person{
        @Override
        public void sayHi() {
            System.out.println("Student Say Hi");
        }

        @Override
        public void sayBye() {
            System.out.println("Student Say Bye");
        }
    }

    public static  class Teacher implements Person{
        @Override
        public void sayHi() {
            System.out.println("Teacher say hi");
        }
    }


    public static void main(String[] args) {
        Student student = new Student();
        student.sayBye();
        Teacher teacher = new Teacher();
        teacher.sayBye();

//        Person.eat();
    }


}
