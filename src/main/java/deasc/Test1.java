package deasc;

//          /usr/lib/jvm/jdk1.7.0_71/bin/javap -c Test1 > file.txt

public class Test1 {

    public void my() {
        int i = 0;
        i++;
    }

}
/*


Compiled from "Test1.java"
public class deasc.Test1 {
public deasc.Test1();
        Code:
        0: aload_0
        1: invokespecial #1                  // Method java/lang/Object."<init>":()V
        4: return

public void my();
        Code:
        0: iconst_0
        1: istore_1
        2: iinc          1, 1
        5: return
}

*/

