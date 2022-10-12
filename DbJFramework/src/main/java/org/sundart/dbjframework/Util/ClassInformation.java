package org.sundart.dbjframework.Util;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassInformation {
    private String className;
    private String classComments;

    private final List<String> classAnnotations = new ArrayList<>();

    private String extention = "";
    private ClassMethod constructor;

    public ClassInformation ClassConstructor(ClassMethod constructor){
        this.constructor = constructor;
        return this;
    }

    public enum ClassTypeEnum{
        Class, Interface
    }

    private ClassTypeEnum classType;

    public ClassInformation(String className) {
        this.className = className;
        this.classType = ClassTypeEnum.Class;
        this.classComments = """
                /**
                 * Generated with DbJFramework ${generatedDate}
                 */
                """;
    }

    public ClassInformation(String className, String classComments, String[] classAnnotations) {
        this.className = className;
        this.classComments = classComments;
        this.classAnnotations.addAll(List.of(classAnnotations));
        this.classType = ClassTypeEnum.Class;
    }

    public ClassInformation ClassType(ClassTypeEnum classType) {
        this.classType = classType;
        return this;
    }

    public ClassInformation ClassExtends(String extention) {
        this.extention = extention;
        return this;
    }

    public ClassInformation ClassComments(String classComments) {
        this.classComments = classComments;
        return this;
    }

    public ClassInformation ClassAnnotations(String[] annotations) {
        this.classAnnotations.addAll(List.of(annotations));
        return this;
    }

    public ClassInformation ClassName(String className) {
        this.className = className;
        return this;
    }

    public void write(Writer aWriter) throws IOException {
        aWriter.write(classComments.replace("${generatedDate}", new SimpleDateFormat("yyyy.MM.dd'T'HH:mm.ss").format(new Date())) + System.lineSeparator());
        for (String annotation : classAnnotations) {
            aWriter.write(annotation + System.lineSeparator());
        }
        aWriter.write("public " + (classType == ClassTypeEnum.Class ? "class" : "interface") + " " + className);
        if(extention != null && !extention.isEmpty()) {
            aWriter.write(" extends " + extention);
        }
        aWriter.write(" {" + System.lineSeparator());
        if(classType == ClassTypeEnum.Class) {
            if(constructor != null) {
                constructor.write(aWriter);
                aWriter.write(System.lineSeparator());
            }
        }
    }
}
