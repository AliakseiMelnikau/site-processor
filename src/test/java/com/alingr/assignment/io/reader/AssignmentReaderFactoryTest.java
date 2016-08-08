package com.alingr.assignment.io.reader;

import com.alingr.assignment.exception.AssignmentException;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;


/**
 * @author Aliaksei Melnikau
 */
public class AssignmentReaderFactoryTest {

  public static final String RESOURCES_PATH = "./src/test/resources/";
  public static final String JSON_FILE_NAME = "input2.json";
  public static final String CSV_FILE_NAME = "input1.csv";
  public static final String TXT_FILE_NAME = "dummy.txt";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testGetReaderSuccess() throws Exception {
    Path csvFile = Paths.get(RESOURCES_PATH + CSV_FILE_NAME);
    Path jsonFile = Paths.get(RESOURCES_PATH + JSON_FILE_NAME);

    Assertions.assertThat(AssignmentReaderFactory.getReader(csvFile)).isInstanceOf(AssignmentCsvReader.class);
    Assertions.assertThat(AssignmentReaderFactory.getReader(jsonFile)).isInstanceOf(AssignmentJsonReader.class);
  }

  @Test
  public void testGetReaderFail() throws Exception {
    Path txtFile = Paths.get(RESOURCES_PATH + TXT_FILE_NAME);

    thrown.expect(AssignmentException.class);
    thrown.expectMessage(MessageFormat.format(AssignmentException.Key.NO_MATCHING_FILE_READER.getMessage(),
            txtFile.getFileName()));
    AssignmentReaderFactory.getReader(txtFile);
  }
}