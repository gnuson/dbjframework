package org.sundart.dbjframework.Model;

import org.springframework.stereotype.Service;
import org.sundart.dbjframework.Model.Creator.ModelCreator;
import org.sundart.dbjframework.Model.Creator.RepoCreator;
import org.sundart.dbjframework.Model.Creator.TestCreator;
import org.sundart.dbjframework.Util.BaseCreator;

import java.io.IOException;

@Service
public class ModelWriter {
    public void createModelFromSchema(String destinationDir, boolean createDir, ModelSchema schema, String packageName) {
        BaseCreator.createFromSchema(destinationDir, createDir, schema, packageName, (table, dir) -> {
            try {
                ModelCreator.createModelTable(packageName, dir, schema, table);
            } catch (IOException e) {
                e.printStackTrace();
            }});
    }

    public void createRepositoryFromSchema(String destinationDir, boolean createDir, ModelSchema schema, String repositoryPackage, String modelPackage) {
        BaseCreator.createFromSchema(destinationDir, createDir, schema, repositoryPackage, (table, dir) -> {
            try {
                RepoCreator.createRepository(dir, table, repositoryPackage, modelPackage);
            } catch (IOException e) {
                e.printStackTrace();
            }});
    }

    public void createTestFromSchema(String destinationDir, boolean createDir, ModelSchema schema, String testPackage, String repoPackage) {
        BaseCreator.createFromSchema(destinationDir, createDir, schema, testPackage, (table, dir) -> {
            try {
                TestCreator.createTest(dir, table, testPackage, repoPackage);
            } catch (IOException e) {
                e.printStackTrace();
            }});
    }
}
