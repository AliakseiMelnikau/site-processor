package com.alingr.assignment.io.writer;

import com.alingr.assignment.adapter.SiteJsonAdapter;
import com.alingr.assignment.exception.AssignmentException;
import com.alingr.assignment.io.IOUtils;
import com.alingr.assignment.model.Site;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Writer;

/**
 * Json writer that writes {@link com.alingr.assignment.model.Collection} in json format. One {@link com.alingr.assignment.model.Collection} object per line.
 *
 * @author Aliaksei Melnikau
 */
public class AssignmentJsonWriter extends JsonWriter {

  private Writer out;

  public AssignmentJsonWriter(Writer out) {
    super(out);
    this.out = out;
  }

  /**
   * Begins json object with name of collection and placeholder for sites array
   *
   * @param collectionId
   */
  public void beginCollection(String collectionId) {
    try {
      setLenient(true);
      beginObject();
      name(SiteJsonAdapter.COLLECTION_ID).value(collectionId);
      name(SiteJsonAdapter.SITES);
      beginArray();
    } catch (IOException e) {
      throw new AssignmentException(AssignmentException.Key.ERROR_WRITING_JSON, e);
    }
  }

  /**
   * Writes single {@link Site} object into json array
   *
   * @param site
   */
  public void writeSite(Site site) {
    IOUtils.getGson().toJson(site, Site.class, this);
  }

  /**
   * Ends single json object {@link com.alingr.assignment.model.Collection} and adds new line
   *
   */
  public void endCollection() {
    try {
      endArray();
      endObject();
      out.write("\n");
    } catch (IOException e) {
      throw new AssignmentException(AssignmentException.Key.ERROR_WRITING_JSON, e);
    }
  }
}
