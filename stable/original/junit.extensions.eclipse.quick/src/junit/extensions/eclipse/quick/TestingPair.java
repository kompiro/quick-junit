package junit.extensions.eclipse.quick;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestingPair {
    private List namingRules = new ArrayList();


    public void addNamingRule(String rule) {
        namingRules.add(rule);
    }

    public void clearNamingRules() {
        namingRules.clear();        
    }

    public String[] getPairClassNames(String className) {
        Set result = new LinkedHashSet();
        className = chopInnerClassName(className);
        addTestedClassNames(className, result);
        if (result.isEmpty()) {
            addTestClassNames(className, result);
        }
        return (String[]) result.toArray(new String[result.size()]);
    }

    private void addTestedClassNames(String className, Set result) {
        for (int i = 0; i < namingRules.size(); ++i) {
            String testedClassName = getTestedClassName(className, (String) namingRules.get(i));
            if (testedClassName != null)
                result.add(testedClassName);
        }
    }

    private String escapeAllChars(String str) {
        // 正規表現の特殊文字
        final String escapeChars = "\\${}?+.[]-()><!|^:=*&,";
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if (escapeChars.indexOf(c) != -1)
                buf.append('\\');
            buf.append(c);
        }
        return buf.toString();
    }

    private String getTestedClassName(String className, String namingRule) {
        int index = className.lastIndexOf('.');
        // 正規表現で問題のある文字をすべてエスケープしておく
        namingRule = escapeAllChars(namingRule);
        if (index == -1) {
            // デフォルトパッケージの場合．
            // $, {, }の文字はエスケープされているため，\$, \{, \} と変換されている．
            // したがって，文字列中に埋め込む場合は， \$ => \\\\\\$, \{ => \\\\\\{
            // などとしなければならない．
            namingRule = namingRule.replaceAll("\\\\\\$\\\\\\{package\\\\\\}\\\\\\.", "");
            namingRule = namingRule.replaceAll("\\\\\\$\\\\\\{type\\\\\\}", "(\\\\w+)");
            Pattern p = Pattern.compile(namingRule);
            Matcher m = p.matcher(className);
            if (m.matches()) {
                return m.group(1);
            }
        } else {
            namingRule = namingRule.replaceAll("\\\\\\$\\\\\\{package\\\\\\}\\\\\\.", "(?:([\\\\w\\\\.]+)\\\\.)?");
            Pattern p = Pattern.compile(namingRule.replaceAll("\\\\\\$\\\\\\{type\\\\\\}", "(\\\\w+)"));
            Matcher m = p.matcher(className);
            if (m.matches()) {
                if (m.group(1) == null)
                    return m.group(2);
                else
                    return m.group(1) + "." + m.group(2);
            }
        }
        return null;
    }

    private void addTestClassNames(String className, Set result) {
        String packageName, typeName;
        int index = className.lastIndexOf('.');
        if (index != -1) {
            typeName = className.substring(index + 1);
            packageName = className.substring(0, index); 
        } else {
            typeName = className;
            packageName = null;
        }
        for (int i = 0; i < namingRules.size(); ++i) {
            result.add(getTestClassName(packageName, typeName, (String) namingRules.get(i)));
        }
    }

    private String getTestClassName(String packageName, String typeName, String namingRule) {
        String result = new String(namingRule);
        if (packageName != null) {
            result = result.replaceAll("\\$\\{package\\}", packageName);
        } else {
            result = result.replaceAll("\\$\\{package\\}\\.", "");
        }
        return result.replaceAll("\\$\\{type\\}", typeName);
    }

    private String chopInnerClassName(String className) {
        int index = className.indexOf('$');
        if (index == -1)
            return className;
        return className.substring(0, index);
    }
}
