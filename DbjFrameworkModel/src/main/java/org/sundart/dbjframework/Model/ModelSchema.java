package org.sundart.dbjframework.Model;

import java.util.List;

public class ModelSchema {

    private String name;

    public ModelSchema(String name, List<ModelTable> tables) {
        this.name = name;
        this.tables = tables;
    }

    private List<ModelTable> tables;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ModelTable> getTables() {
        return tables;
    }

    public void setTables(List<ModelTable> tables) {
        this.tables = tables;
    }
}
