package org.sundart.dbjframework.Util;

import org.sundart.dbjframework.Model.ModelSchema;
import org.sundart.dbjframework.Model.ModelTable;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;

public abstract class BaseCreator {
    public static void createFromSchema(String dstDir, boolean createDir, ModelSchema schema, String packageName, BiConsumer<ModelTable, File> callback) {
        try {
            File dir = createDirectories(dstDir, createDir, schema, packageName);
            schema.getTables().forEach(table -> callback.accept(table, dir));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File createDirectories(String dstDir, boolean createDir, ModelSchema schema, String packageName) throws IOException {
        File dir = new File(dstDir + packageName.replace(".", "/"));
        if (createDir) {
            if (dir.exists()) {
                FileSystemUtils.deleteRecursively(dir);
            }
            if (!dir.exists()) {
                if(!dir.mkdirs()){
                    throw new IOException("Could not create dirs");
                }
            }
        } else {
            if (dir.exists()) {
                Arrays.stream(Objects.requireNonNull(dir.listFiles())).forEach(File::delete);
            } else {
                throw new IOException("Folder " + dir.getAbsolutePath() + " required before");
            }
        }
        return dir;
    }
}
