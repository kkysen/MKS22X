import java.lang.instrument.Instrumentation;

/**
 * 
 * 
 * @author Khyber Sen
 */
@Deprecated
public class Size {
    
    private static Instrumentation instrumentation;
    
    public static void premain(final String agentArgs, final Instrumentation instrumentation) {
        Size.instrumentation = instrumentation;
    }
    
    public static long of(final Object o) {
        return instrumentation.getObjectSize(o);
    }
    
}
