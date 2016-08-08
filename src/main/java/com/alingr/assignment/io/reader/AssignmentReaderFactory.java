package com.alingr.assignment.io.reader;

import com.alingr.assignment.exception.AssignmentException;

import java.nio.file.Path;

/**
 * Factory that produces {@link AssignmentReader} based on provided file extension
 *
 * @author Aliaksei Melnikau
 */
public class AssignmentReaderFactory {

  /**
   * Factory method that instantiates {@link AssignmentReader} based on file extension.
   *
   * @param file
   * @return
   */
  public static AssignmentReader getReader(Path file) {
    if (file.getFileName().toString().endsWith("csv")) {
      return AssignmentCsvReader.getInstance(file);
    } else if (file.getFileName().toString().endsWith("json")) {
      return AssignmentJsonReader.getInstance(file);
    } else {
      throw new AssignmentException(AssignmentException.Key.NO_MATCHING_FILE_READER, file.getFileName());
    }
  }
}
