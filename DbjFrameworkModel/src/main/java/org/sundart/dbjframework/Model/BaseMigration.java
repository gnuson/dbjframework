package org.sundart.dbjframework.Model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.sundart.dbjframework.Util.Utils.toSqlType;

public abstract class BaseMigration {
    private final String schemaName;

    public abstract void Up() throws IOException;
    public abstract void Down() throws IOException;

    public BaseMigration(String schemaName){
        this.schemaName = schemaName;
    }

    public String createSchema(){
        return "CREATE SCHEMA IF NOT EXISTS " + schemaName + "\n" +
                "    AUTHORIZATION postgres;";
    }
    public String createTable(ModelTable table){
        String sql = "CREATE TABLE IF NOT EXISTS " + this.schemaName + "." + table.getName() + "(";
        final String[] pk = {null};
        Object[] isIdentity = new Object[]{false};
        sql += table.getColumns().stream().map(column -> {
            isIdentity[0] = false;
            String sqls = columnToString(column, isIdentity);
            if((boolean) isIdentity[0]){
                pk[0] = column.getName();
            }
            return sqls;
        }).collect(Collectors.joining(", "));
        if(pk[0] != null) {
            sql += ", CONSTRAINT \"" + table.getName() + "_pkey\" PRIMARY KEY (" + pk[0] + ")";
        }
        sql += table.getForeignKeys().stream().map(foreignKey -> ", CONSTRAINT " + foreignKey.toString() +" FOREIGN KEY (" + foreignKey.getColumnName() + ")" +
                " REFERENCES " + schemaName + ".\"" + foreignKey.getForeignTableName() + "\" (" + foreignKey.getForeignColumnName() + ") MATCH SIMPLE").collect(Collectors.joining());
        sql += ")";
        return sql;
    }

    private String columnToString(ModelColumn column, Object[] isIdenty) {
        StringBuilder sb = new StringBuilder(column.getName() + " " + toSqlType(column.getType()).orElseThrow());
        if(!column.isNullable()){
            sb.append(" NOT NULL");
        }
        if(column.isIdentifier()){
            sb.append(" GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 )");
            isIdenty[0] = true;
        }
        return sb.toString();
    }

    private String fieldToString(Field field, Object[] isIdenty) {
        StringBuilder sb = new StringBuilder(field.getName() + " " + toSqlType(field.getType()));
        if(field.getAnnotation(Column.class) != null && !field.getAnnotation(Column.class).nullable()){
            sb.append(" NOT NULL");
        }
        if(field.getAnnotation(Id.class) != null){
            sb.append(" GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 )");
            isIdenty[0] = true;
        }
        return sb.toString();
    }

    public void deleteTable(String table){

    }
    protected Optional<Map<String, Object>> Mapof(String... values) {
        if(values.length % 2 != 0){
            return Optional.empty();
        }
        Map<String, Object> toReturn = new HashMap<>();
        for(int i = 0; i < (values.length/2); i++){
            toReturn.put(values[i*2], values[1+i*2]);
        }
        return Optional.of(toReturn);
    }

    protected <R> List<R> stringsToObject(String[][] values, Function<Map<String, Object>, R> constructor) {
        return Arrays.stream(values)
                .map(this::Mapof)
                .filter(Optional::isPresent).map(Optional::get)
                .map(constructor).toList();
    }

}
