package junit.extensions.eclipse.quick;


public class NamingRule {
    private String value;
    private boolean enabled;
    public NamingRule(String value, boolean enabled) {
        this.value = value;
        this.enabled = enabled;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public String getValue() {
        return value;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public void setValue(String value) {
        this.value = value;
    }
}

