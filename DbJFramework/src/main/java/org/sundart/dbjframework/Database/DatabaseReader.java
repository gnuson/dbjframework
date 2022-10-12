package org.sundart.dbjframework.Database;

import org.sundart.dbjframework.Model.ModelColumn;
import org.sundart.dbjframework.Model.ModelSchema;
import org.sundart.dbjframework.Model.ModelTable;
import org.sundart.dbjframework.Model.ModelForeignKey;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatabaseReader {

    private final Connection connection;

    public DatabaseReader(Connection connection) {
        this.connection = connection;
    }

    public List<ModelColumn> getColumns(String table) throws SQLException {
        List<Map<String, Object>> columns =  ToMap(connection.createStatement().executeQuery("SELECT * FROM information_schema.columns WHERE table_name = '" + table + "';"));
        return columns.stream().map(item -> new ModelColumn(item)).collect(Collectors.toList());
    }

    private List<Map<String, Object>> ToMap(ResultSet rs) throws SQLException {
        List<Map<String, Object>> toReturn = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int nbColumns = rsmd.getColumnCount();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 1; i <= nbColumns; i++) {
                String columnName = rsmd.getColumnName(i);
                String columnValue = rs.getString(i);
                map.put(columnName, columnValue);
            }
            toReturn.add(map);
        }
        return toReturn;
    }

    public List<ModelForeignKey> getForeignKeys(String table) throws SQLException {
        return getForeignKeys().stream().filter(foreignKey -> foreignKey.getForeignTableName().equals(table) || foreignKey.getTableName().equals(table)).toList();
    }

    List<ModelForeignKey> foreignKeys;
    public List<ModelForeignKey> getForeignKeys() throws SQLException {
        if(foreignKeys == null) {
            foreignKeys = queryForList("""
                    SELECT
                        tc.table_name,
                            kcu.column_name,
                            ccu.table_name AS foreign_table_name,
                            ccu.column_name AS foreign_column_name
                    FROM
                        information_schema.table_constraints AS tc
                        JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name
                        JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name
                    WHERE
                        constraint_type = 'FOREIGN KEY';""").stream().map(ModelForeignKey::new).collect(Collectors.toList());
        }
        return foreignKeys;
    }

    public Stream<ModelTable> GetTables(String schema) throws SQLException {
        List<Map<String, Object>> tables = queryForList("SELECT * FROM pg_catalog.pg_tables WHERE schemaname = '" + schema + "'");
        return tables.stream().map(propertyMaps -> (String) propertyMaps.get("tablename")).map(table -> {
            try {
                return new ModelTable(table, getColumns(table).stream().toList(), getForeignKeys(table));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<Map<String, Object>> queryForList(String sql) throws SQLException {
        return  ToMap(connection.createStatement().executeQuery(sql));
    }

    public ModelSchema GetSchema(String schema) throws SQLException {
        return new ModelSchema(schema, GetTables(schema).collect(Collectors.toList()));
    }
}
