package com.alingr.assignment.io;

import com.alingr.assignment.exception.AssignmentException;
import com.alingr.assignment.io.writer.AssignmentJsonWriter;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Static utility class to perform IO operations related to processing {@link com.alingr.assignment.model.Site} objects
 *
 * @author Aliaksei Melnikau
 */
public class IOUtils {

  private static final Logger log = getLogger(IOUtils.class);


  // Thread-safe instance of Gson
  public static Gson gson;
  static {
     gson = new Gson();
  }

  /**
   * Returns files (represented by {@link Path}) that match provided mask in specified directory
   *
   * @param directory - directory where files should reside
   * @param fileMask - file mask to match
   * @return
   */
  public static List<Path> getFilesFromDirectoryByMask(Path directory, String fileMask) {
    List<Path> result = new ArrayList<>();
    try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(
            directory, fileMask)) {
      for (Path file : dirStream) {
        result.add(file);
      }
    } catch (IOException e) {
      throw new AssignmentException(AssignmentException.Key.ERROR_TRAVERSING_DIRECTORY, e, getAbsoluteFilePath(directory));
    }
    return result;
  }

  /**
   * Returns {@link OutputStream} of file
   *
   * @param file - path to the file
   * @return
   */
  private static OutputStream getFileOutputStream(Path file) {
    try {
      return new FileOutputStream(file.toFile(),true);
    } catch (IOException e) {
      throw new AssignmentException(AssignmentException.Key.ERROR_GETTING_FILE_OUTPUT_STREAM,e,getAbsoluteFilePath(file));
    }
  }

  /**
   * Converts provided <b>directoryPath</b> into {@link Path} and does basic validation (if exists and has permission to read)
   *
   * @param directoryPath
   * @return
   */
  public static Path toPathDirectory(String directoryPath) {
    Path directory = Paths.get(directoryPath);
    if (!Files.exists(directory)) {
      throw new AssignmentException(AssignmentException.Key.PATH_DOES_NOT_EXISTS,directoryPath);
    }
    if (!Files.isDirectory(directory)) {
      throw new AssignmentException(AssignmentException.Key.PATH_IS_NOT_A_DIRECTORY,directoryPath);
    }
    if (!Files.isReadable(directory)) {
      throw new AssignmentException(AssignmentException.Key.NO_PERMISSION_TO_READ_DIRECTORY,directoryPath);
    }

    return directory;
  }

  /**
   * Converts provided <b>filePath</b> into {@link Path} and does basic validation (checks if file and has permission to write)<br>
   * Creates new file if it doesn't yet exists.
   *
   * @param filePath
   * @return
   */
  public static Path toPathFile(String filePath) {
    Path file = Paths.get(filePath);
    if (Files.isDirectory(file)) {
      throw new AssignmentException(AssignmentException.Key.PATH_IS_NOT_A_FILE,filePath);
    }
    if (Files.exists(file) && !Files.isWritable(file)) {
      throw new AssignmentException(AssignmentException.Key.FILE_EXISTS_BUT_NOT_WRITABLE,filePath);
    }
    try {
      if (!Files.exists(file)) {
        file = Files.createFile(file);
      }
    } catch (IOException e) {
      throw new AssignmentException(AssignmentException.Key.ERROR_CREATING_FILE,e,filePath);
    }

    return file;
  }

  /**
   * Creates instance of {@link JsonReader} for provided file in UTF-8
   *
   * @param jsonFile
   * @return
   */
  public static JsonReader getJsonReader(Path jsonFile) {
    try {
      return new JsonReader(new InputStreamReader(Files.newInputStream(jsonFile), StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new AssignmentException(AssignmentException.Key.ERROR_OPENING_FILE, e, getAbsoluteFilePath(jsonFile));
    }
  }

  /**
   * Create instance of {@link AssignmentJsonWriter} for provided file in UTF-8
   *
   * @param outputFile
   * @return
   */
  public static AssignmentJsonWriter getJsonWriter(Path outputFile) {
      return new AssignmentJsonWriter(new OutputStreamWriter(IOUtils.getFileOutputStream(outputFile), StandardCharsets.UTF_8));
  }

  /**
   * Returns sharable thread-safe {@link Gson} instance
   *
   * @return
   */
  public static Gson getGson() {
    return gson;
  }

  /**
   * Return absolute path of {@link Path} in string representation
   *
   * @param filePath
   * @return
   */
  public static String getAbsoluteFilePath(Path filePath) {
    return filePath.toAbsolutePath().normalize().toString();
  }
}
