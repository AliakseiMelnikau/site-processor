package com.alingr.assignment.processor;

import com.alingr.assignment.exception.AssignmentException;
import com.alingr.assignment.io.IOUtils;
import com.alingr.assignment.io.reader.AssignmentReader;
import com.alingr.assignment.io.reader.AssignmentReaderFactory;
import com.alingr.assignment.io.writer.AssignmentJsonWriter;
import com.alingr.assignment.io.writer.AssignmentWriteTask;
import com.alingr.assignment.model.Site;
import com.alingr.assignment.service.KeywordService;
import com.alingr.assignment.service.KeywordServiceImpl;
import com.alingr.assignment.service.KeywordServiceTask;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Processor that reads {@link Site} data from given input files, resolves keywords for them and stores
 * result in output file
 *
 * @author Aliaksei Melnikau
 */
public class SiteProcessor {

  private static final Logger log = getLogger(SiteProcessor.class);

  private static final int CORE_POOL_SIZE = 2;
  private static final int MAX_POOL_SIZE = 5;
  private static final int KEYWORD_RESOLUTION_QUEUE_SIZE = 10;
  private static final int OUTPUT_QUEUE_SIZE = 100;

  /**
   * Process files (read {@link Site} objects from file, resolve keyword for them and write them to output file)
   * that match <b>fileMasks</b> in provided input folder
   *
   * @param inputFolder - folder with input files
   * @param outputFile  - output file that should hold processing result
   * @param fileMasks   - list of masks for input file (ex. "input*.json")
   */
  public void processFiles(Path inputFolder, Path outputFile, List<String> fileMasks) {
    ExecutorService executorService = initKeywordExecutorService();
    AssignmentJsonWriter jsonWriter = IOUtils.getJsonWriter(outputFile);
    KeywordService keywordService = getKeywordService();
    try {
      for (String fileMask : fileMasks) {
        List<Path> matchingFiles = IOUtils.getFilesFromDirectoryByMask(inputFolder, fileMask);
        for (Path fileWithSites : matchingFiles) {
          processFile(outputFile, executorService, jsonWriter, keywordService, fileWithSites);
        }
      }
    } finally {
      executorService.shutdownNow();
      try {
        jsonWriter.close();
      } catch (IOException e) {
        log.debug("Json writer wasn't closed", e);
      }
    }
  }

  /**
   * Process single file (read {@link Site} objects from file, resolve keyword for them and write them to output file)
   *
   * @param outputFile      - file to write result to
   * @param executorService - {@link ExecutorService} that concurrently executes keywords resolution
   * @param jsonWriter      - writer that writes {@link Site} to output file
   * @param keywordService  - service that resolves keywords for a given {@link Site}
   * @param inputFile       - file with input data
   */
  private void processFile(Path outputFile, ExecutorService executorService, AssignmentJsonWriter jsonWriter,
                           KeywordService keywordService, Path inputFile) {
    if (IOUtils.getAbsoluteFilePath(outputFile).equals(IOUtils.getAbsoluteFilePath(inputFile))) {
      throw new AssignmentException(AssignmentException.Key.INPUT_FILE_MATCHES_OUTPUT_FILE, IOUtils.getAbsoluteFilePath(outputFile));
    }
    jsonWriter.beginCollection(inputFile.getFileName().toString());
    AssignmentReader reader = AssignmentReaderFactory.getReader(inputFile);
    BlockingQueue<Site> processedSites = new ArrayBlockingQueue<>(OUTPUT_QUEUE_SIZE);
    AssignmentWriteTask assignmentWriteTask = launchWriteTask(processedSites, jsonWriter, inputFile.getFileName().toString());
    Site site;
    int sitesRead = 0;
    // concurrently launch keyword resolution for sites within file
    while ((site = reader.getNext()) != null) {
      sitesRead++;
      executorService.execute(new KeywordServiceTask(site, keywordService, processedSites));
    }
    // block until all sites are written
    while (assignmentWriteTask.getNumberOfWrittenSites() != sitesRead) {
    }
    assignmentWriteTask.stop();

    jsonWriter.endCollection();
  }

  /**
   * Launches single instance of {@link AssignmentWriteTask}. Only one instance can be concurrently executed for a given
   * {@link AssignmentJsonWriter}
   *
   * @param processedSites - queue that hold {@link Site} with resolved keywords
   * @param writer
   * @param collectionId   - id of currently processed collection
   */
  private AssignmentWriteTask launchWriteTask(BlockingQueue<Site> processedSites, AssignmentJsonWriter writer, String collectionId) {
    AssignmentWriteTask assignmentWriteTask = new AssignmentWriteTask(processedSites, writer, collectionId);
    new Thread(assignmentWriteTask).start();
    return assignmentWriteTask;
  }

  /**
   * Initializes {@link ExecutorService} to concurrently resolve keywords for {@link Site}
   */
  private ExecutorService initKeywordExecutorService() {
    BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(KEYWORD_RESOLUTION_QUEUE_SIZE);
    RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
    return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
            0L, TimeUnit.MILLISECONDS, blockingQueue, rejectedExecutionHandler);
  }

  KeywordService getKeywordService() {
    return new KeywordServiceImpl();
  }
}
