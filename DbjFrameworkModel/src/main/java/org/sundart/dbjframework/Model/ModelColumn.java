package org.sundart.dbjframework.Model;

import java.util.Map;

import static org.sundart.dbjframework.Util.Utils.toSqlType;

public class ModelColumn {
    private String name;
    private String type;
    private boolean identifier;
    private boolean unique;
    private boolean nullable;
    private boolean relation;

    public ModelColumn(String columnName, boolean isIdentity, boolean isNullable, String dataType) {
        this.name = columnName;
        this.type = dataType;

    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isRelation() {
        return relation;
    }

    public void setRelation(boolean relation) {
        this.relation = relation;
    }


    public ModelColumn(Map<String, Object> columnValues) {
        this.setName(String.valueOf(columnValues.get("column_name")));
        this.setIdentifier(isValue(columnValues.get("is_identity"), "YES"));
        this.setNullable(isValue(columnValues.get("is_nullable"), "YES"));
        this.setType(toSqlType((String) columnValues.get("data_type")).orElseThrow());
    }

    private boolean isValue(Object value, String validate) {
        if(value == null) return false;
        if(validate.equals(value)) return true;
        return false;
    }


    public boolean isIdentifier() {
        return identifier;
    }

    public void setIdentifier(boolean identifier) {
        this.identifier = identifier;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
