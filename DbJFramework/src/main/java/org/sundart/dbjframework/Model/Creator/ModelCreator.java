package org.sundart.dbjframework.Model.Creator;

import org.sundart.dbjframework.Model.ModelColumn;
import org.sundart.dbjframework.Model.ModelSchema;
import org.sundart.dbjframework.Model.ModelTable;
import org.sundart.dbjframework.Database.Utils;
import org.sundart.dbjframework.Util.ClassGenerator;
import org.sundart.dbjframework.Util.ClassInformation;
import org.sundart.dbjframework.Util.ClassParameter;
import org.sundart.dbjframework.Model.ModelForeignKey;
import org.sundart.dbjframework.Util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ModelCreator {
    public static void createModelTable(String packageName, File dir, ModelSchema schema, ModelTable table) throws IOException {
        FileWriter aWriter = new FileWriter(dir.getAbsolutePath() + "/" + StringUtils.FirstLetterUppercase(table.getName()) + ".java", false);
        ClassGenerator
                .Writer(aWriter)
                .PackageName(packageName)
                .Imports(new String[]{
                        "javax.persistence.Table;",
                        "javax.persistence.Id;",
                        "javax.persistence.Column;",
                        "javax.persistence.Entity;",
                        "javax.persistence.ManyToOne"})
                .ClassInformation(new ClassInformation(StringUtils.FirstLetterUppercase(table.getName()), """
/*
 * Generated with DbJFramework ${generatedDate}
 */
                        """, new String[]{"@Entity",
                        "@Table(schema = \"" + schema.getName() + "\", name = \"" + table.getName() + "\")"}))
                .Parameters(table.getColumns().stream().map(modelColumn -> createParameter(modelColumn, table))
                        .flatMap(Collection::stream).collect(Collectors.toList())).End();
    }

    private static List<ClassParameter> createParameter(ModelColumn modelColumn, ModelTable table) {
        List<ClassParameter> toReturn = new ArrayList<>();
        ClassParameter classParameter = new ClassParameter(modelColumn.getName(), Utils.getTypeFromDatabaseType(modelColumn.getType()).orElseThrow()).GetterSetter();
        toReturn.add(classParameter);
        if(modelColumn.isIdentifier()) classParameter.Annotation("@Id");
        if(modelColumn.isNullable()) classParameter.Annotation("@Column(nullable = false)");
        if(modelColumn.isRelation()){
            Optional<ModelForeignKey> foreignKeyOptional = table.getForeignKeys().stream().filter(foreignKey -> foreignKey.getColumnName().equals(modelColumn.getName())).findFirst();
            if(foreignKeyOptional.isPresent()){
                ModelForeignKey foreignKey = foreignKeyOptional.get();
                ClassParameter foreignParameter = new ClassParameter(foreignKey.getForeignTableName(), StringUtils.FirstLetterUppercase(foreignKey.getForeignTableName()))
                        .GetterSetter()
                        .Annotation("@ManyToOne");
                toReturn.add(foreignParameter);
            }
        }
        return toReturn;
    }
}
