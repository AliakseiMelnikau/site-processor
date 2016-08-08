package com.alingr.assignment.exception;

import java.text.MessageFormat;

/**
 * Business Exception that encapsulates possible file-handling errors<br>
 * Usage example:<br>
 * {@code throw new AssignmentException(AssignmentException.Key.ERROR_READING_FILE, e, jsonFile)}
 *
 * @author Aliaksei Melnikau
 */
public class AssignmentException extends RuntimeException {
  public enum Key {

    PATH_DOES_NOT_EXISTS("Path: {0} does not exist"),
    PATH_IS_NOT_A_DIRECTORY("Provided path: {0} is not a directory"),
    PATH_IS_NOT_A_FILE("Provided path: {0} is not a file"),
    FILE_EXISTS_BUT_NOT_WRITABLE("No permission to write to file: {0}"),
    NO_MATCHING_FILE_READER("No FileReader found for file type: {0}"),
    NO_PERMISSION_TO_READ_DIRECTORY("No permission to read directory: {0}"),
    INPUT_FILE_MATCHES_OUTPUT_FILE("Input file matches output file: {0}"),
    UNEXPECTED_INPUT_PARAMETERS("Expected exactly two arguments <input_folder> <output_file>, but was provided {0} parameter(s)"),
    ERROR_CREATING_FILE("Error creating file: {0}"),
    ERROR_READING_FILE("Error reading file: {0}"),
    ERROR_OPENING_FILE("Error opening file: {0}"),
    ERROR_CLOSING_FILE("Error closing file: {0}"),
    ERROR_GETTING_FILE_OUTPUT_STREAM("Error getting file output stream: {0}"),
    ERROR_WRITING_JSON("Error while writing json"),
    ERROR_TRAVERSING_DIRECTORY("Error while traversing directory: {0}");

    Key(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }

    private final String message;
  }


  public AssignmentException(Key key, Object... params) {
    super(MessageFormat.format(key.getMessage(),params));
  }


  public AssignmentException(Key key, Throwable cause, Object... params) {
    super(MessageFormat.format(key.getMessage(),params),cause);
  }
}
