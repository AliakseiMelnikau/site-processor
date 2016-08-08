package com.alingr.assignment;

import com.alingr.assignment.exception.AssignmentException;
import com.alingr.assignment.io.IOUtils;
import com.alingr.assignment.processor.SiteProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * Application entry point. It expects two arguments:<br>
 * <ol>
 *   <li><i>pathToDirectory</i> - path to directory that contains csv and json input files</li>
 *   <li><i>outputFile</i> - file to which output result is written</li>
 * </ol>
 *
 * @author Aliaksei Melnikau
 */
public class Application {

  private static final Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) throws IOException {
    log.info("Application started");
    if (args.length != 2) {
      throw new AssignmentException(AssignmentException.Key.UNEXPECTED_INPUT_PARAMETERS, args.length);
    }
    new SiteProcessor().processFiles(IOUtils.toPathDirectory(args[0]), IOUtils.toPathFile(args[1]), Arrays.asList("input*.csv", "input*.json"));
    log.info("Application completed");
  }
}
