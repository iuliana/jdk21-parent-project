/*
public class MainOne {
    public static void main(String... args) {
        System.out.println("Child One Project.");
    }
}
*/

// JEP 5. Unnamed Classes and Instance Main Methods (Preview)
// run with:
// java --enable-preview --source 21 child-one-project/src/main/java/MainOne.java
// implied static
void main() {
    System.out.println("Child One Project.");
    if(staticVariable) {
        System.out.println("static variable");
    }

    staticMethod();
 }

static boolean staticVariable = true;

static void staticMethod(){
    System.out.println("static method");
}

// Purpose: students can write their first programs without needing to understand language features designed for large programs.
 // Rules:
// 1. package Statement is Not Allowed
// 2. No Constructor is Allowed
// 3. A main() Method must Exist
// 4. Unnamed class cannot 'extend' or 'implement'
// 5. can only access their own static members