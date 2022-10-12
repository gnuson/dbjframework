package org.sundart.dbjframework.Model;

import java.util.List;

public class ModelTable {
    private String name;
    private List<ModelColumn> columns;
    private List<ModelForeignKey> foreignKeys;

    public ModelTable(String testTable, String[][] strings, String[][] strings1) {
    }

    public ModelTable(String tableName) {
        setName(tableName);
    }

    public List<ModelForeignKey> getForeignKeys() {
        return foreignKeys;
    }

    public ModelTable setForeignKeys(List<ModelForeignKey> foreignKeys) {
        this.foreignKeys = foreignKeys;
        return this;
    }

    public ModelTable(String name, List<ModelColumn> columns, List<ModelForeignKey> foreignKeys) {
        this.name = name;
        this.columns = columns;
        this.foreignKeys = foreignKeys;
        fixForeignKey();
    }

    private void fixForeignKey() {
        columns.forEach(column -> column.setRelation(
                foreignKeys.stream().anyMatch(foreignKey -> foreignKey.getColumnName().equals(column.getName()) ||
                foreignKey.getForeignColumnName().equals(column.getName()))));
    }

    public String getName() {
        return name;
    }

    public ModelTable setName(String name) {
        this.name = name;
        return this;
    }

    public List<ModelColumn> getColumns() {
        return columns;
    }

    public ModelTable setColumns(List<ModelColumn> columns) {
        this.columns = columns;
        return this;
    }
}
