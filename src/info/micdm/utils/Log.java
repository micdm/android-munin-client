package info.micdm.utils;

/**
 * Класс для более лучшего контроля за логированием.
 * @author Mic, 2011
 *
 */
public class Log {

    /**
     * Тег для всех сообщений.
     */
    protected static String _tag;
    
    /**
     * Включено ли логирование.
     */
    public static boolean isEnabled;
    
    /**
     * Инициализирует логирование.
     */
    public static void init(String tag, boolean enable) {
        _tag = tag;
        isEnabled = enable;
    }
    
    /**
     * Выводит отладочное сообщение.
     */
    public static synchronized void debug(String msg) {
        android.util.Log.d(_tag, msg);
    }
    
    /**
     * Выводит информационное сообщение.
     */
    public static synchronized void info(String msg) {
        android.util.Log.i(_tag, msg);
    }
    
    /**
     * Выводит предупреждение.
     */
    public static synchronized void warning(String msg) {
        android.util.Log.w(_tag, msg);
    }
    
    /**
     * Выводит сообщение об ошибке.
     */
    public static synchronized void error(String msg) {
        android.util.Log.e(_tag, msg);
    }
    
    /**
     * Выводит сообщение об ошибке + распечатывает исключение.
     */
    public static synchronized void error(String msg, Throwable e) {
        android.util.Log.e(_tag, msg, e);
    }
}

