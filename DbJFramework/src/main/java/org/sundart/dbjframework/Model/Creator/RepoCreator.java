package org.sundart.dbjframework.Model.Creator;

import org.sundart.dbjframework.Model.ModelTable;
import org.sundart.dbjframework.Util.ClassGenerator;
import org.sundart.dbjframework.Util.ClassInformation;
import org.sundart.dbjframework.Util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RepoCreator {
    public static void createRepository(File dir, ModelTable table, String packageName, String modelPackage) throws IOException {
        FileWriter aWriter = new FileWriter(dir.getAbsolutePath() + "/" + StringUtils.FirstLetterUppercase(table.getName()) + "Repository.java", false);
        ClassGenerator.Writer(aWriter).PackageName(packageName).Imports(new String[]{
            modelPackage + "." + StringUtils.FirstLetterUppercase(table.getName()), "org.springframework.data.jpa.repository.JpaRepository"
        }).ClassInformation(new ClassInformation(StringUtils.FirstLetterUppercase(table.getName()) + "Repository")
                .ClassExtends("JpaRepository<" + StringUtils.FirstLetterUppercase(table.getName()) + ", Integer>")
                .ClassType(ClassInformation.ClassTypeEnum.Interface)).End();
    }
}
