package com.alingr.assignment.io.reader;

import com.alingr.assignment.exception.AssignmentException;
import com.alingr.assignment.io.IOUtils;
import com.alingr.assignment.model.Site;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Implementation of {@link AssignmentReader} for <b>json</b> files with {@link Site} data.
 *
 * @author Aliaksei Melnikau
 */
public class AssignmentJsonReader implements AssignmentReader {
  private AssignmentJsonReader() {}

  private JsonReader reader;
  private Path jsonFile;

  /**
   * Creates instance of {@link AssignmentJsonReader} for provided json file.
   *
   * @param jsonFile
   * @return {@link AssignmentJsonReader}
   */
  public static AssignmentJsonReader getInstance(Path jsonFile) {
    AssignmentJsonReader jsonReader = new AssignmentJsonReader();
    try {
      jsonReader.jsonFile = jsonFile;
      jsonReader.reader = IOUtils.getJsonReader(jsonFile);
      jsonReader.reader.beginArray();
    } catch (IOException e) {
      throw new AssignmentException(AssignmentException.Key.ERROR_OPENING_FILE, e, jsonFile);
    }
    return jsonReader;
  }

  /**
   * Returns {@link Site} one be one from json file. Null is returned when no more {@link Site} are available.
   *
   * @return {@link Site}
   */
  @Override
  public Site getNext() {
    try {
      while (reader.hasNext()) {
        return  IOUtils.getGson().fromJson(reader, Site.class);
      }
      reader.endArray();
    } catch (IOException e) {
      throw new AssignmentException(AssignmentException.Key.ERROR_READING_FILE, e, jsonFile);
    }
    try {
      reader.close();
      return null;
    } catch (IOException e) {
      throw new AssignmentException(AssignmentException.Key.ERROR_CLOSING_FILE, e, jsonFile);
    }
  }
}
