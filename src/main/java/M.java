/**
 * Created by zinchenko on 25.06.14.
 */
public class M {

    public static void main(String[] args) {
        try{
            f();
        } catch (Exception e) {
            throw new RuntimeException("em", e);
        }
        finally {
            try {
                e();
            } catch (Exception e) {
//                e.printStackTrace();
                throw new RuntimeException("finally fail", e);
            }
        }
    }

    public static void f(){
        throw new RuntimeException("email or plan error");
    }

    public static void e(){
        throw new NullPointerException();
    }


}
