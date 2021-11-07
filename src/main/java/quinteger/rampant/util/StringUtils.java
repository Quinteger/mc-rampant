package quinteger.rampant.util;

public class StringUtils {

    private StringUtils() {}

    /**
     * Try to parse a given string as a two-element array of strings similar to a
     * {@link net.minecraft.util.ResourceLocation}.
     *
     * @param input the string to parse
     * @return a two-sized string array with parsed namespace and path
     */
    public static String[] decomposeResourceLocationString(String input) {
        if (input == null) return new String[]{"minecraft", ""};
        String[] result = {"minecraft", input};
        int i = input.indexOf(':');
        if (i >= 0) {
            result[1] = input.substring(i + 1);
            if (i >= 1) {
                result[0] = input.substring(0, i);
            }
        }
        return result;
    }

    public static boolean stringCheck(Object object) {
        return object instanceof String;
    }
}
