package Tools;

public final class DEBUG {

    public static final String NONE = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";

    public static void printlnRed(String text) {
        System.out.println(RED + text + NONE);
    }

    public static void printlnGreen(String text) {
        System.out.println(GREEN + text + NONE);
    }

    public static void printlnBlue(String text) {
        System.out.println(BLUE + text + NONE);
    }

    public static void printlnYellow(String text) {
        System.out.println(YELLOW + text + NONE);
    }

    public static void printlnPurple(String text) {
        System.out.println(PURPLE + text + NONE);
    }

}
