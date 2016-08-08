package com.alingr.assignment.adapter;

import com.alingr.assignment.model.Site;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Adapter that maps json object into {@link Site}
 *
 * @author Aliaksei Melnikau
 */
public class SiteJsonAdapter extends TypeAdapter<Site> {

  public static final String SITE_ID = "site_id";
  public static final String NAME = "name";
  public static final String MOBILE = "mobile";
  public static final String SCORE = "score";
  public static final String KEYWORDS = "keywords";
  public static final String COLLECTION_ID = "collectionId";
  public static final String SITES = "sites";

  @Override
  public void write(JsonWriter out, Site site) throws IOException {
    out.beginObject();
    out.name(SITE_ID).value(site.getId());
    out.name(NAME).value(site.getName());
    out.name(MOBILE).value(site.isMobile()?1:0);
    out.name(SCORE).value(site.getScore());
    out.name(KEYWORDS).value(site.getKeywords());
    out.endObject();
  }

  @Override
  public Site read(JsonReader in) throws IOException {
    final Site site = new Site();

    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
        case SITE_ID:
          site.setId(in.nextLong());
          break;
        case NAME:
          site.setName(in.nextString());
          break;
        case SCORE:
          site.setScore(in.nextLong());
          break;
        case MOBILE:
          site.setMobile(in.nextInt()==0?false:true);
      }
    }
    in.endObject();

    return site;
  }
}
