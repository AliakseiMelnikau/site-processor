package com.alingr.assignment.model;

import java.util.List;
import java.util.Objects;

/**
 * Model that represents named collection of {@link Site} objects
 *
 * @author Aliaksei Melnikau
 */
public class Collection {
  private String collectionId;
  private List<Site> sites;

  public String getCollectionId() {
    return collectionId;
  }

  public void setCollectionId(String collectionId) {
    this.collectionId = collectionId;
  }

  public List<Site> getSites() {
    return sites;
  }

  public void setSites(List<Site> sites) {
    this.sites = sites;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Collection that = (Collection) o;
    return Objects.equals(collectionId, that.collectionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(collectionId);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Collection{");
    sb.append("collectionId='").append(collectionId).append('\'');
    sb.append(", sites=").append(sites);
    sb.append('}');
    return sb.toString();
  }
}
