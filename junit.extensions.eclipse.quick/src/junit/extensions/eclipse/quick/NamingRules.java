package junit.extensions.eclipse.quick;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.IPreferenceStore;


public class NamingRules {
    private IPreferenceStore store;
    private final String STORE_ID = "NamingRules";

    public NamingRules(IPreferenceStore store) {
        this.store = store;
    }

    public List get() {
        String value = store.getString(STORE_ID);
        if (value == null || value.length() == 0)
            return getDefault();
        return stringToList(value);
    }

    public String[] getEnableValues() {
        List namingRules = get();
        List result = new ArrayList();
        for (int i = 0; i < namingRules.size(); ++i) {
            NamingRule rule = (NamingRule) namingRules.get(i);
            if (rule.isEnabled()) {
                result.add(rule.getValue());
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }


    public void set(List namingRules) {
        store.setValue(STORE_ID, listToString(namingRules));
    }

    private String listToString(List namingRules) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < namingRules.size(); ++i) {
            if (i != 0)
                buf.append(',');
            NamingRule rule = (NamingRule) namingRules.get(i);
            buf.append(rule.getValue() + ":" + rule.isEnabled());
        }
        return buf.toString();
    }

    private List stringToList(String string) {
        List result = new ArrayList();
        StringTokenizer st = new StringTokenizer(string, ",");
        while(st.hasMoreTokens()) {
            String column = st.nextToken();
            int index = column.indexOf(':');
            if (index != -1) {
                String value = column.substring(0, index);
                Boolean enabled = Boolean.valueOf(column.substring(index + 1));
                result.add(new NamingRule(value, enabled.booleanValue()));      
            }
        }
        return result;
    }

    public List getDefault() {
        List result = new ArrayList();
        result.add(new NamingRule("${package}.${type}Test", true));
        result.add(new NamingRule("${package}.${type}PDETest", false));
        return result;
    }
}
