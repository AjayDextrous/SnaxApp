package com.makeathon.snax.fragments;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.makeathon.snax.R;

public class SplashFragmentDirections {
  private SplashFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionSplashToEnterdetails() {
    return new ActionOnlyNavDirections(R.id.action_splash_to_enterdetails);
  }
}
