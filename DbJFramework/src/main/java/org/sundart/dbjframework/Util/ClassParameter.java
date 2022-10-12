package org.sundart.dbjframework.Util;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ClassParameter {
    private final List<String> parameterAnnotation = new ArrayList<>();
    private AccessType accessType;
    private final String parameterName;
    private final String parameterType;
    private boolean getter;
    private boolean setter;
    private boolean finalParameter;

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public ClassParameter Private() {
        this.accessType = AccessType.Private;
        return this;
    }

    public ClassParameter Getter() {
        return Getter(true);
    }
    public ClassParameter Getter(boolean get) {
        this.getter = get;
        return this;
    }

    public ClassParameter Setter() {
        return Setter(true);
    }

    public ClassParameter Setter(boolean set) {
        this.setter = set;
        return this;
    }

    public ClassParameter Annotation(String[] annotations) {
        if(annotations == null || annotations.length<=0) return this;
        this.parameterAnnotation.addAll(List.of(annotations));
        return this;
    }

    public ClassParameter GetterSetter() {
        this.getter = true;
        this.setter = true;
        return this;
    }

    public void write(Writer aWriter) throws IOException {
        for (String annotation: parameterAnnotation) {
            aWriter.write("\t" + annotation + System.lineSeparator());
        }
        if(accessType == AccessType.Private) aWriter.write("\tprivate ");
        else if(accessType == AccessType.Protected) aWriter.write("protected ");
        else if(accessType == AccessType.Public) aWriter.write("public ");
        if(finalParameter) aWriter.write("final ");
        aWriter.write(getParameterType() + " " + getParameterName() + ";" + System.lineSeparator());
        if(getter) aWriter.write(getterString() + System.lineSeparator());
        if(setter) aWriter.write(setterString() + System.lineSeparator());
    }

    private String getterString() {
        return "\tpublic " + parameterType + " get" + StringUtils.FirstLetterUppercase(parameterName) + "(){" + System.lineSeparator() + "\t\treturn this." + parameterName + ";\n\t}";
    }

    private String setterString(){
        return "\tpublic void set" + StringUtils.FirstLetterUppercase(parameterName) + "(" + parameterType + " " + parameterName + "){" + System.lineSeparator() + "\t\tthis." + parameterName + " = " + parameterName +";\n\t}";
    }

    public ClassParameter Annotation(String annotation) {
        if(annotation == null || annotation.isEmpty()) return this;
        this.parameterAnnotation.add(annotation);
        return this;
    }

    public ClassParameter Final() {
        this.finalParameter = true;
        return this;
    }

    public ClassParameter(String name, String type) {
        this.parameterName = name;
        this.parameterType = type;
        this.getter = false;
        this.setter = false;
        this.accessType = AccessType.Private;
    }
}
