package com.alingr.assignment.io.reader;

import com.alingr.assignment.adapter.SiteCsvAdapter;
import com.alingr.assignment.exception.AssignmentException;
import com.alingr.assignment.io.IOUtils;
import com.alingr.assignment.model.Site;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * Implementation of {@link AssignmentReader} for <b>csv</b> files with {@link Site} data.
 *
 * @author Aliaksei Melnikau
 */
public class AssignmentCsvReader implements AssignmentReader {
  private AssignmentCsvReader() {
  }

  private CSVParser parser;
  private Iterator<CSVRecord> iterator;
  private Path csvFile;


  /**
   * Creates instance of {@link AssignmentCsvReader} for provided csv file.
   *
   * @param csvFile
   * @return {@link AssignmentCsvReader}
   */
  public static AssignmentCsvReader getInstance(Path csvFile) {
    AssignmentCsvReader csvReader = new AssignmentCsvReader();
    try {
      csvReader.csvFile = csvFile;
      csvReader.parser = new CSVParser(
              new FileReader(csvFile.toFile()),
              CSVFormat.DEFAULT.withHeader());
      csvReader.iterator = csvReader.parser.iterator();
    } catch (IOException e) {
      throw new AssignmentException(AssignmentException.Key.ERROR_OPENING_FILE, e, csvFile);
    }
    return csvReader;
  }


  /**
   * Returns {@link Site} one be one from csv file. Null is returned when no more {@link Site} are available.
   *
   * @return {@link Site}
   */
  @Override
  public Site getNext() {
    while (iterator.hasNext()) {
      return SiteCsvAdapter.read(iterator.next());
    }
    try {
      parser.close();
      return null;
    } catch (IOException e) {
      throw new AssignmentException(AssignmentException.Key.ERROR_CLOSING_FILE, e, IOUtils.getAbsoluteFilePath(csvFile));
    }
  }
}
