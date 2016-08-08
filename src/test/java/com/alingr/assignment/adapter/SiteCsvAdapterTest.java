package com.alingr.assignment.adapter;

import com.alingr.assignment.model.Site;
import org.apache.commons.csv.CSVRecord;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Aliaksei Melnikau
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CSVRecord.class)
public class SiteCsvAdapterTest {

  public static final Long DUMMY_SITE_ID = 1l;
  public static final String DUMMY_NAME = "DummyName";
  public static final Long DUMMY_SCORE = 100l;
  public static final Boolean DUMMY_MOBILE = true;

  @Test
  public void testRead() throws Exception {
    CSVRecord csvRecord = PowerMockito.mock(CSVRecord.class);
    Mockito.when(csvRecord.get(SiteCsvAdapter.SITE_ID)).thenReturn(String.valueOf(DUMMY_SITE_ID));
    Mockito.when(csvRecord.get(SiteCsvAdapter.SCORE)).thenReturn(String.valueOf(DUMMY_SCORE));
    Mockito.when(csvRecord.get(SiteCsvAdapter.MOBILE)).thenReturn(String.valueOf(DUMMY_MOBILE));
    Mockito.when(csvRecord.get(SiteCsvAdapter.NAME)).thenReturn(DUMMY_NAME);

    Site convertedSite = SiteCsvAdapter.read(csvRecord);

    Site site = new Site();
    site.setId(DUMMY_SITE_ID);
    site.setMobile(DUMMY_MOBILE);
    site.setScore(DUMMY_SCORE);
    site.setName(DUMMY_NAME);

    Assertions.assertThat(convertedSite).isEqualTo(site);
  }
}