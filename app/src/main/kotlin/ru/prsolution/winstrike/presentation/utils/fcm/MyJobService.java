package ru.prsolution.winstrike.presentation.utils.fcm;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import timber.log.Timber;


@SuppressLint("Registered")
public class MyJobService extends JobService {


  @Override
  public boolean onStartJob(JobParameters jobParameters) {
    Timber.d("Performing long running task in scheduled job");
    // TODO(developer): add long running task here.
    return false;
  }

  @Override
  public boolean onStopJob(JobParameters jobParameters) {
    return false;
  }

}
