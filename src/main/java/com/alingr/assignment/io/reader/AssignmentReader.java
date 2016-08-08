package com.alingr.assignment.io.reader;

import com.alingr.assignment.model.Site;

/**
 *
 * {@link Site} data streaming reader interface. Recourse handling is done automatically.<br>
 * Underlying resource is automatically closed when all data is read (when {@code null} is returned).
 *
 * @author Aliaksei Melnikau
 */
public interface AssignmentReader {
  /**
   * Returns {@link Site} one be one from underlying resource. Null is returned when no more {@link Site} are available.
   *
   * @return {@link Site}
   */
  Site getNext();
}
