package logger;

public class Logger {
    public static boolean shouldLog;
    public boolean shouldLogStack;

    public static void log(String msg){
        if(shouldLog)
            System.out.println(msg);
    }
    public void logStack(String msg){
        if(shouldLogStack)
            System.out.println(msg);
    }
}
