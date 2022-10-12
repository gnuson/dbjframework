package org.sundart.dbjframework.mavenplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.sundart.dbjframework.Database.DatabaseReader;
import org.sundart.dbjframework.Database.Migration.MigrationWriter;
import org.sundart.dbjframework.Model.ModelSchema;
import org.sundart.dbjframework.Model.ModelWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Mojo(name = "Creator")
public class Creator extends AbstractMojo {
    @Parameter
    public String basepackage;
    @Parameter
    public String directory;
    @Parameter
    public boolean createDir;
    @Parameter
    public DatabaseConfiguration database;
    @Parameter
    public String modelPackageName;
    @Parameter
    public String repositoryPackageName;
    @Parameter
    public String simpletestPackageName;
    @Parameter
    public String migrationPackageName;

    @Parameter(defaultValue = "false")
    public boolean disabled;


    public Creator() {
    }

    public void execute() throws MojoExecutionException {
        if(disabled){
            getLog().info("Not running Creator because disabled");
            return;
        }
        try {
            getLog().info("Start ModelCreator " + database.getSchema());
            DatabaseReader databaseReader = new DatabaseReader(getConnection());
            ModelWriter modelWriter = new ModelWriter();
            ModelSchema schema = databaseReader.GetSchema(database.getSchema());
            if(!directory.endsWith("/")) directory += "/";
            if(modelPackageName != null) {
                modelWriter.createModelFromSchema(directory, createDir, schema, basepackage + "." + modelPackageName);
                if (repositoryPackageName != null) {
                    getLog().info("Start RepositoryCreator");
                    modelWriter.createRepositoryFromSchema(directory, createDir, schema, basepackage + "." + repositoryPackageName, basepackage + "." + modelPackageName);
                    if (simpletestPackageName != null) {
                        getLog().info("Start SimpletestCreator");
                        modelWriter.createTestFromSchema(directory, createDir, schema, basepackage + "." + simpletestPackageName, basepackage + "." + repositoryPackageName);
                    }
                }
                if (migrationPackageName != null) {
                    MigrationWriter migrationWriter = new MigrationWriter();
                    migrationWriter.createFromSchema(directory, createDir, schema, basepackage + "." + migrationPackageName);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Connection getConnection()
            throws SQLException {
        Properties info = new Properties();
        info.put("user", database.getUsername());
        info.put("password", database.getPassword());

        Connection connection = DriverManager.getConnection(database.getUrl(), info);

        return connection;
    }
}