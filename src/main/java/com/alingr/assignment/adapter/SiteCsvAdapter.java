package com.alingr.assignment.adapter;

import com.alingr.assignment.model.Site;
import org.apache.commons.csv.CSVRecord;

/**
 * Adapter that converts csv record into {@link Site}
 *
 * @author Aliaksei Melnikau
 */
public class SiteCsvAdapter {

  public static final String SITE_ID = "id";
  public static final String NAME = "name";
  public static final String MOBILE = "is mobile";
  public static final String SCORE = "score";

  /**
   * Reads {@link CSVRecord} and converts it into {@link Site}
   *
   * @param record
   * @return
   */
  public static Site read(CSVRecord record) {
    Site site = new Site();
    site.setId(Long.valueOf(record.get(SITE_ID)));
    site.setName(record.get(NAME));
    site.setScore(Long.valueOf(record.get(SCORE)));
    site.setMobile(Boolean.valueOf(record.get(MOBILE)));

    return site;
  }
}
