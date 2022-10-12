package org.sundart.dbjframework.Util;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassMethod {
    private final String name;
    private final List<ClassParameter> parameters = new ArrayList<>();
    private AccessType accessType;
    private String type;
    private String body;
    private final List<String> annotations = new ArrayList<>();

    public ClassMethod(String name) {
        this.name = name;
        this.accessType = AccessType.Private;
        this.type = null;
        this.body = null;
    }

    public ClassMethod AccessType(AccessType accessType) {
        this.accessType = accessType;
        return this;
    }

    public ClassMethod Type(String type) {
        this.type = type;
        return this;
    }

    public ClassMethod Body(String body) {
        this.body = body;
        return this;
    }

    public ClassMethod Parameter(ClassParameter parameter){
        this.parameters.add(parameter);
        return this;
    }

    public void write(Writer aWriter) throws IOException {
        for (String annotation: annotations) {
            aWriter.write(System.lineSeparator() + "\t" + annotation);
        }
        aWriter.write(System.lineSeparator() + "\t" + StringUtils.FirstLetterLowercase(accessType.name()) + " ");
        if(type != null){
            aWriter.write(type + " ");
        }
        aWriter.write(name + "(");
        if(!parameters.isEmpty()){
            aWriter.write(parameters.stream().map(parameter -> parameter.getParameterType() + " " + parameter.getParameterName())
                    .collect(Collectors.joining(", ")));
        }
        aWriter.write(") {" + System.lineSeparator());
        aWriter.write(body + System.lineSeparator() + "\t}");
    }

    public ClassMethod Annotation(String annotation) {
        annotations.add(annotation);
        return this;
    }
}
