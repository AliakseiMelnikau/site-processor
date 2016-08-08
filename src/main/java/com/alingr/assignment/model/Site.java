package com.alingr.assignment.model;

import com.alingr.assignment.adapter.SiteJsonAdapter;
import com.google.gson.annotations.JsonAdapter;

/**
 * Representation of "site" object with relevant metadata including list of keywords
 *
 * @author Aliaksei Melnikau
 */
@JsonAdapter(SiteJsonAdapter.class)
public class Site {
  private Long id;
  private String name;
  private Long score;
  private boolean mobile;
  private String keywords;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getScore() {
    return score;
  }

  public void setScore(Long score) {
    this.score = score;
  }

  public boolean isMobile() {
    return mobile;
  }

  public void setMobile(boolean mobile) {
    this.mobile = mobile;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Site site = (Site) o;
    return mobile == site.mobile &&
            java.util.Objects.equals(id, site.id) &&
            java.util.Objects.equals(name, site.name) &&
            java.util.Objects.equals(score, site.score) &&
            java.util.Objects.equals(keywords, site.keywords);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(id, name, score, mobile, keywords);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Site{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", score=").append(score);
    sb.append(", mobile=").append(mobile);
    sb.append(", keywords='").append(keywords).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
