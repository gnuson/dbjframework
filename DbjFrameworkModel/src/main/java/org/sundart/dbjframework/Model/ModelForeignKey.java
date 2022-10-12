package org.sundart.dbjframework.Model;

import java.util.Map;

public class ModelForeignKey {
    private String tableName;
    private String columnName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getForeignTableName() {
        return foreignTableName;
    }

    public void setForeignTableName(String foreignTableName) {
        this.foreignTableName = foreignTableName;
    }

    public String getForeignColumnName() {
        return foreignColumnName;
    }

    public void setForeignColumnName(String foreignColumnName) {
        this.foreignColumnName = foreignColumnName;
    }

    private String foreignTableName;
    private String foreignColumnName;
    public ModelForeignKey(Map<String, Object> foreignKey) {
        this.tableName = String.valueOf(foreignKey.get("table_name"));
        this.columnName = String.valueOf(foreignKey.get("column_name"));
        this.foreignTableName = String.valueOf(foreignKey.get("foreign_table_name"));
        this.foreignColumnName = String.valueOf(foreignKey.get("foreign_column_name"));
    }

    @Override
    public String toString() {
        return tableName + "_" + columnName + "_" + foreignTableName + "_" + foreignColumnName;
    }
}
