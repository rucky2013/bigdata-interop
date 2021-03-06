package com.google.cloud.hadoop.io.bigquery.mapred;

import static org.junit.Assert.assertEquals;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.JobContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

/**
 * Unit tests for {@link BigQueryMapredJobContext}.
 */
@RunWith(JUnit4.class)
public class BigQueryMapredJobContextTest {

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Test public void testConstructor() {
    // This is a utility class, so there isn't a use for an instance,
    // but this test gives us 100% test coverage on the class, which is nice.
    new BigQueryMapredJobContext();
  }

  @Test public void testFrom() throws IOException {
    Configuration jobConf = new Configuration();
    String originalJobIdString = "job_201408201023_0001";
    jobConf.set("mapreduce.job.id", originalJobIdString);
    String jobDir = "//some/stuff/" + originalJobIdString;
    jobConf.set("mapreduce.job.dir", jobDir);
    JobContext jobContext = BigQueryMapredJobContext.from(jobConf);
    String jobIdString = jobContext.getJobID().toString();
    assertEquals(originalJobIdString, jobIdString);
  }

  @Test public void testFromNoJobDir() throws IOException {
    JobConf jobConf = new JobConf();
    expectedException.expect(IllegalArgumentException.class);
    BigQueryMapredJobContext.from(jobConf);
  }

  @Test public void testFromBadJobName() throws IOException {
    JobConf jobConf = new JobConf();
    String originalJobIdString = "invalid_job_format";
    String jobDir = "//some/stuff/" + originalJobIdString;
    jobConf.set("mapreduce.job.dir", jobDir);
    expectedException.expect(IllegalArgumentException.class);
    BigQueryMapredJobContext.from(jobConf);
  }
}
