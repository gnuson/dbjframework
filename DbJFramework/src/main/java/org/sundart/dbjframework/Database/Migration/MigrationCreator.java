package org.sundart.dbjframework.Database.Migration;
import org.sundart.dbjframework.Model.ModelColumn;
import org.sundart.dbjframework.Model.ModelForeignKey;
import org.sundart.dbjframework.Model.ModelSchema;
import org.sundart.dbjframework.Model.ModelTable;
import org.sundart.dbjframework.Util.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class MigrationCreator {

    public static void createBaseMigation(File dir, ModelSchema schema, String packageName) throws IOException {
        FileWriter aWriter = new FileWriter(dir.getAbsolutePath() + "/Initial.java", false);
        ClassGenerator.Writer(aWriter)
                .PackageName(packageName)
                .Imports(new String[]{
                        "org.sundart.dbjframework.Model.ModelColumn",
                        "org.sundart.dbjframework.Model.ModelForeignKey",
                        "org.sundart.dbjframework.Model.BaseMigration",
                        "org.sundart.dbjframework.Model.ModelTable;"})
                .ClassInformation(new ClassInformation("Initial")
                        .ClassExtends("BaseMigration")
                        .ClassConstructor(new ClassMethod("Initial").Parameter(new ClassParameter("schemaName", "String")).Body("\t\tsuper(\"schemaName\");")))
                .Method(new ClassMethod("Up").Type("void").AccessType(AccessType.Public).Body(createUp(schema)).Annotation("@Override"))
                .Method(new ClassMethod("Down").Type("void").AccessType(AccessType.Public).Body(createDown(schema)).Annotation("@Override")).End();
    }

    private static String createDown(ModelSchema schema) {
        return schema.getTables().stream().map(table -> "\t\tdeleteTable(\"" + table.getName() + "\");").collect(Collectors.joining(System.lineSeparator()));
    }

    private static String createUp(ModelSchema schema) {
        return schema.getTables().stream().map(table ->
          "\t\tcreateTable(new ModelTable(\"" + table.getName() + "\")" + System.lineSeparator() +
          "\t\t\t.setColumns(stringsToObject(new String[][]{" + table.getColumns().stream().map(MigrationCreator::createColumn).collect(Collectors.joining(", ")) + "}, ModelColumn::new))" + System.lineSeparator() +
          "\t\t\t.setForeignKeys(stringsToObject(new String[][]{" + table.getForeignKeys().stream().map(foreignKey -> createForeignKey(table, foreignKey)).collect(Collectors.joining(", ")) + "}, ModelForeignKey::new)));"
        ).collect(Collectors.joining());
    }

    private static String createForeignKey(ModelTable table, ModelForeignKey foreignKey) {
        if(foreignKey.getTableName().equals(table.getName())) return "";
        return System.lineSeparator() + "\t\t\t\t{\"table_name\", \"" + foreignKey.getTableName() + "\", " +
                "\"column_name\", \"" + foreignKey.getColumnName() + "\", " +
                "\"foreign_table_name\", \"" + foreignKey.getForeignTableName() + "\", " +
                "\"foreign_column_name\", \"" + foreignKey.getForeignColumnName() + "\"}";
    }

    private static String createColumn(ModelColumn column) {
        StringBuilder toReturn =new StringBuilder(System.lineSeparator() + "\t\t\t\t{\"column_name\", \"" + column.getName() + "\"");
        if(column.isIdentifier()){
            toReturn.append(", \"is_identity\", \"YES\"");
        }
        if(column.isNullable()){
            toReturn.append(", \"is_nullable\", \"YES\"");
        }
        toReturn.append(", \"data_type\", \"")
                .append(column.getType())
                .append("\"}");
        return toReturn.toString();
    }
}
