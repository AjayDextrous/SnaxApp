package com.makeathon.snax.fragments;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.makeathon.snax.R;

public class PermissionsFragmentDirections {
  private PermissionsFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionPermissionsToCamera() {
    return new ActionOnlyNavDirections(R.id.action_permissions_to_camera);
  }

  @NonNull
  public static NavDirections actionPermissionsToSplash() {
    return new ActionOnlyNavDirections(R.id.action_permissions_to_splash);
  }
}
