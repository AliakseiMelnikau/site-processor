package com.alingr.assignment.io;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Aliaksei Melnikau
 */
public class IOUtilsTest {

  public static final String RESOURCES_PATH = "./src/test/resources/";
  public static final String JSON_FILE_NAME = "input2.json";

  @Test
  public void testGetFilesFromDirectoryByMaskExactName() throws Exception {
    Path filesDirectory = IOUtils.toPathDirectory(RESOURCES_PATH);

    List<Path> jsonInput = IOUtils.getFilesFromDirectoryByMask(filesDirectory, "input2.json");

    Assertions.assertThat(jsonInput).size().isEqualTo(1);
    Assertions.assertThat(jsonInput.get(0)).isEqualTo(Paths.get(RESOURCES_PATH + JSON_FILE_NAME));
  }

  @Test
  public void testGetFilesFromDirectoryByMask() throws Exception {
    Path filesDirectory = IOUtils.toPathDirectory(RESOURCES_PATH);

    List<Path> jsonInput = IOUtils.getFilesFromDirectoryByMask(filesDirectory, "input*.json");

    Assertions.assertThat(jsonInput).size().isEqualTo(1);
    Assertions.assertThat(jsonInput.get(0)).isEqualTo(Paths.get(RESOURCES_PATH + JSON_FILE_NAME));
  }
}