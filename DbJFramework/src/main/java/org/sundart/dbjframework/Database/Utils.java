package org.sundart.dbjframework.Database;

import java.util.Optional;

public class Utils {
    public static Optional<String> getTypeFromDatabaseType(String type){
        switch (type.toLowerCase()){
            case "integer", "bigint": return Optional.of("int");
            case "string", "character varying": return Optional.of("String");
            default :
                System.out.println("Could not find type:" + type.toLowerCase());
                return Optional.empty();
        }
    }


    public static Optional<String> toSqlType(Class<?> type) {
        return toSqlType(type.getName());
    }
    public static Optional<String> toSqlType(String type) {
        return switch (type.toLowerCase()) {
            case "integer", "bigint" -> Optional.of("integer");
            case "string", "character varying" -> Optional.of("character varying");
            default -> Optional.empty();
        };
    }
}
