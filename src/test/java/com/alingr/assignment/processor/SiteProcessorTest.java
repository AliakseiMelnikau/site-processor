package com.alingr.assignment.processor;

import com.alingr.assignment.exception.AssignmentException;
import com.alingr.assignment.io.IOUtils;
import com.alingr.assignment.model.Collection;
import com.alingr.assignment.service.KeywordService;
import com.google.gson.stream.JsonReader;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Aliaksei Melnikau
 */
public class SiteProcessorTest {

  public static final String RESOURCES_PATH = "./src/test/resources/";
  public static final String JSON_FILE_NAME = "input2.json";
  public static final String CSV_FILE_NAME = "input1.csv";
  public static final String OUTPUT_FILE_NAME = "output.json";

  @Rule
  public ExpectedException thrown = ExpectedException.none();


  @Before
  public void setUp() throws Exception {
    Files.deleteIfExists(Paths.get(RESOURCES_PATH + OUTPUT_FILE_NAME));
  }

  @Test
  public void testProcessFilesSuccess() throws Exception {
    SiteProcessor processorSpy = Mockito.spy(new SiteProcessor());
    KeywordService mockKeywordService = Mockito.mock(KeywordService.class);
    Mockito.doReturn(mockKeywordService).when(processorSpy).getKeywordService();

    processorSpy.processFiles(Paths.get(RESOURCES_PATH), Paths.get(RESOURCES_PATH + OUTPUT_FILE_NAME), Arrays.asList("input*.csv", "input*.json"));

    assertTrue(Files.exists(Paths.get(RESOURCES_PATH + OUTPUT_FILE_NAME)));

    List<String> jsons = Files.readAllLines(Paths.get(RESOURCES_PATH + OUTPUT_FILE_NAME));
    Assertions.assertThat(jsons.size()).isEqualTo(2);

    Collection csvCollection = IOUtils.getGson().fromJson(new JsonReader(new StringReader(jsons.get(0))), Collection.class);
    Assertions.assertThat(csvCollection.getCollectionId()).isEqualTo(CSV_FILE_NAME);
    Assertions.assertThat(csvCollection.getSites().size()).isEqualTo(3);
    Collection jsonCollection = IOUtils.getGson().fromJson(new JsonReader(new StringReader(jsons.get(0))), Collection.class);
    Assertions.assertThat(jsonCollection.getCollectionId()).isEqualTo(CSV_FILE_NAME);
    Assertions.assertThat(jsonCollection.getSites().size()).isEqualTo(3);

  }

  @Test
  public void testProcessFilesOutputFileEqualsInputFile()  {
    SiteProcessor processor = new SiteProcessor();


    thrown.expect(AssignmentException.class);
    thrown.expectMessage(MessageFormat.format(AssignmentException.Key.INPUT_FILE_MATCHES_OUTPUT_FILE.getMessage(),
            IOUtils.getAbsoluteFilePath(Paths.get(RESOURCES_PATH + JSON_FILE_NAME))));
    processor.processFiles(Paths.get(RESOURCES_PATH), Paths.get(RESOURCES_PATH + JSON_FILE_NAME), Arrays.asList("input*.json"));
  }

}