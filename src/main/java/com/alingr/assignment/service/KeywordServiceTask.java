package com.alingr.assignment.service;

import com.alingr.assignment.model.Site;
import org.slf4j.Logger;

import java.util.concurrent.BlockingQueue;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Concurrent keywords resolution task that stores {@link Site} with resolved keywords in provided {@link BlockingQueue}.
 * Later on {@link Site} is fetched from that queue and written to output file by another service.
 *
 * @author Aliaksei Melnikau
 */
public class KeywordServiceTask implements Runnable {

  private static final Logger log = getLogger(KeywordServiceTask.class);

  private Site site;
  private KeywordService keywordService;
  private BlockingQueue<Site> queue;

  public KeywordServiceTask(Site site, KeywordService keywordService, BlockingQueue<Site> queue) {
    this.site = site;
    this.keywordService = keywordService;
    this.queue = queue;
  }

  @Override
  public void run() {
    try {
      site.setKeywords(keywordService.resolveKeywords(site));
    } catch (Exception e) {
      site.setKeywords("");
      log.error("Keywords were not resolved for site" + site);
    }
    try {
      queue.put(site);
      log.debug("Keywords resolved for site: " + site);
    } catch (InterruptedException e) {
      log.error("Site wasn't added to process queue" + site);
    }
  }
}
