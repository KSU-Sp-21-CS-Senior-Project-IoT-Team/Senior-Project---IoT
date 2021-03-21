public class LoadingClassTest {
    public static void main(String[] args) {
        Class a = LoadingClass.class;
        System.out.println(".class called");
        LoadingClass.touch();
    }
}
