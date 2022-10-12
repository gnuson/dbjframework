package org.sundart.dbjframework.Util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ClassGenerator {
    public static ClassGeneratorWithWriter Writer(FileWriter aWriter) {
        return new ClassGeneratorWithWriter(aWriter);
    }

    public static class ClassGeneratorWithWriter {
        protected static Writer aWriter;

        public ClassGeneratorWithWriter(FileWriter aWriter) {
            ClassGeneratorWithWriter.aWriter = aWriter;
        }

        public ClassGeneratorWithPackage PackageName(String packageName) throws IOException {
            return new ClassGeneratorWithPackage(packageName);
        }

        public static class ClassGeneratorWithPackage {

            public ClassGeneratorWithPackage(String packageName) throws IOException {
                aWriter.write("package " + packageName + ";" + System.lineSeparator());
            }


            public ClassGeneratorWithImports Imports(String[] imports) throws IOException {
                return new ClassGeneratorWithImports(imports);
            }

            public static class ClassGeneratorWithImports {

                public ClassGeneratorWithImports(String[] imports) throws IOException {
                    for (String importt : imports) {
                        aWriter.write("import " + importt + ";" + System.lineSeparator());
                    }
                }

                public ClassGeneratorWithClassInformation ClassInformation(ClassInformation classInformations) throws IOException {
                    return new ClassGeneratorWithClassInformation(classInformations);
                }


                public static class ClassGeneratorWithClassInformation {
                    public ClassGeneratorWithClassInformation(ClassInformation classInformations) throws IOException {
                        classInformations.write(aWriter);
                    }

                    public ClassGeneratorWithClassInformation Parameters(List<ClassParameter> classParameters) throws IOException {
                        for (ClassParameter classParameter : classParameters) {
                            classParameter.write(aWriter);
                        }
                        return this;
                    }

                    public void End() throws IOException {
                        aWriter.write(System.lineSeparator() + "}" + System.lineSeparator());
                        aWriter.flush();
                        aWriter.close();
                    }

                    public ClassGeneratorWithClassInformation Methods(List<ClassMethod> methods) throws IOException {
                        for (ClassMethod method : methods) {
                            method.write(aWriter);
                        }
                        return this;
                    }
                    public ClassGeneratorWithClassInformation Method(ClassMethod method) throws IOException {
                        method.write(aWriter);
                        return this;
                    }
                }
            }
        }
    }
}