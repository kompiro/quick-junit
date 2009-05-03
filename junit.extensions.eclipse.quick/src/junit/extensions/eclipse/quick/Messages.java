package junit.extensions.eclipse.quick;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

    private static final String BUNDLE_NAME = "junit.extensions.eclipse.quick.messages"; //$NON-NLS-1$
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
    public static String getString(String key, Object[] args) {
        return MessageFormat.format(RESOURCE_BUNDLE.getString(key), args);
    }
    public static String getString(String key, Object arg0) {
        return MessageFormat.format(RESOURCE_BUNDLE.getString(key), new Object[] {arg0});
    }
}
