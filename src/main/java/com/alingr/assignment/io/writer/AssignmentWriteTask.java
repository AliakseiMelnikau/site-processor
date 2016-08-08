package com.alingr.assignment.io.writer;

import com.alingr.assignment.model.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

/**
 * Concurrent write task that monitors <b>queue</b> for available tasks to write.<br>
 * <i>WARNING</i>. Only one instance for each {@link AssignmentJsonWriter} should exist as writer is not thread safe.
 *
 * @author Aliaksei Melnikau
 */
public class AssignmentWriteTask implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(AssignmentWriteTask.class);

  private BlockingQueue<Site> queue;
  private volatile int sitesWritten;
  private AssignmentJsonWriter writer;
  private volatile boolean stop = false;
  private String collectionId;

  public AssignmentWriteTask(BlockingQueue<Site> queue, AssignmentJsonWriter writer, String collectionId) {
    this.queue = queue;
    this.writer = writer;
    this.collectionId = collectionId;
  }

  @Override
  public void run() {
    log.info("Write task launched for collection: " + collectionId);
    while (!stop) {
      Site site = queue.poll();
      if (site != null) {
        writer.writeSite(site);
        log.debug("Site written: " + site);
        sitesWritten++;
      }
    }
    log.info("Write task completed for collection: " + collectionId);
  }

  /**
   * Sets flag to stop execution
   */
  public void stop() {
    this.stop = true;
  }

  /**
   * Return number of written sites by writer.
   */
  public int getNumberOfWrittenSites() {
    return sitesWritten;
  }
}
