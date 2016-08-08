package com.alingr.assignment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

/**
 * Dummy implementation of {@link KeywordService}
 *
 * @author Aliaksei Melnikau
 */
public class KeywordServiceImpl implements KeywordService{

  private static final Logger log = LoggerFactory.getLogger(KeywordServiceImpl.class);

  /**
   * Resolves a list of keywords associated with a site.
   *
   * @param site
   * @return a comma delimited string or an empty string if there are no keywords associated with the site.
   */
  @Override
  public String resolveKeywords(Object site) {
    int keywordsLimit = new Random().nextInt(5);
    StringBuilder result = new StringBuilder();
    for (int i=0; i< keywordsLimit;i++) {
      try {
        // imitate processing of website to fetch keywords
        Thread.sleep(500);
      } catch (InterruptedException e) {
        log.error(String.format("Keywords were not resolved for site %s", site), e);
      }
      result.append(UUID.randomUUID().toString());
      result.append(", ");
    }

    // trim last ", "
    return result.length()>0?result.toString().substring(0,result.length()-2):result.toString();
  }
}
