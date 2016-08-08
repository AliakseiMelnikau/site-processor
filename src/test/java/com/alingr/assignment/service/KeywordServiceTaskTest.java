package com.alingr.assignment.service;

import com.alingr.assignment.model.Site;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Aliaksei Melnikau
 */
public class KeywordServiceTaskTest {

  private BlockingQueue<Site> queue;
  private KeywordService keywordService;

  @Before
  public void setUp() throws Exception {
    queue = new ArrayBlockingQueue<>(100);
    keywordService = Mockito.mock(KeywordService.class);
  }


  @Test
  public void testRunTask() throws Exception {
    //when
    Site siteOne = new Site();
    siteOne.setName("siteOne");

    Site siteTwo = new Site();
    siteTwo.setName("siteTwo");

    Site siteException = new Site();
    siteException.setName("siteException");
    Mockito.when(keywordService.resolveKeywords(siteOne)).thenReturn("siteOneKeywords");
    Mockito.when(keywordService.resolveKeywords(siteTwo)).thenReturn("siteTwoKeywords");
    Mockito.when(keywordService.resolveKeywords(siteException)).thenThrow(new RuntimeException("Some calculation error"));

    //then
    new KeywordServiceTask(siteOne, keywordService, queue).run();
    new KeywordServiceTask(siteTwo, keywordService, queue).run();
    new KeywordServiceTask(siteException, keywordService, queue).run();

    Assertions.assertThat(siteOne.getKeywords()).isEqualTo("siteOneKeywords");
    Assertions.assertThat(siteTwo.getKeywords()).isEqualTo("siteTwoKeywords");
    Assertions.assertThat(siteException.getKeywords()).isEqualTo("");
    Assertions.assertThat(queue.size()).isEqualTo(3);
    Assertions.assertThat(queue).contains(siteOne, siteTwo, siteException);
  }
}