package org.sundart.dbjframework.Database.Migration;

import org.sundart.dbjframework.Model.ModelSchema;
import org.sundart.dbjframework.Util.BaseCreator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class MigrationWriter extends BaseCreator {
    public void createFromSchema(String destinationDir, boolean createDir, ModelSchema schema, String packageName) {
        try {
            File dir = createDirectories(destinationDir, createDir, schema, packageName);
            MigrationCreator.createBaseMigation(dir, schema, packageName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
