package org.sundart.dbjframework.Model.Creator;

import org.sundart.dbjframework.Model.ModelTable;
import org.sundart.dbjframework.Util.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TestCreator {
    public static void createTest(File dir, ModelTable table, String testPackage, String repoPackage) throws IOException {
        FileWriter aWriter = new FileWriter(dir.getAbsolutePath()+ "/" + StringUtils.FirstLetterUppercase(table.getName()) + "Test.java", false);
        ClassGenerator.Writer(aWriter)
                .PackageName(testPackage)
                .Imports(new String[]{repoPackage + "." + StringUtils.FirstLetterUppercase(table.getName()) + "Repository"})
                .ClassInformation(
                        new ClassInformation(StringUtils.FirstLetterUppercase(table.getName()) + "Test")
                        .ClassConstructor(
                                new ClassMethod(StringUtils.FirstLetterUppercase(table.getName()) + "Test")
                                        .AccessType(AccessType.Public)
                                        .Parameter(new ClassParameter("repo", StringUtils.FirstLetterUppercase(table.getName()) + "Repository"))
                                        .Body("\t\tthis.repo = repo;")
                                ))
                .Parameters(List.of(new ClassParameter("repo", StringUtils.FirstLetterUppercase(table.getName())+ "Repository").Final()))
                .Methods(List.of(new ClassMethod("testId")
                        .AccessType(AccessType.Public)
                        .Type("void")
                        .Body("\t\trepo.findById(0).get().getId();")))
                .End();
    }
}
