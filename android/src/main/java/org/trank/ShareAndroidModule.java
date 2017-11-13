package org.trank;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;

import java.io.File;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;

/**
 * Intent module. Launch other activities or open URLs.
 */
@ReactModule(name = "ShareAndroidModule")
public class ShareAndroidModule extends ReactContextBaseJavaModule {

  /* package */ static final String ACTION_SHARED = "sharedAction";
  /* package */ static final String ERROR_INVALID_CONTENT = "E_INVALID_CONTENT";
  /* package */ static final String ERROR_UNABLE_TO_OPEN_DIALOG = "E_UNABLE_TO_OPEN_DIALOG";
  /* package */ static final int SHARE_REQUEST_CODE = 42;
  private final ReactApplicationContext reactContext;


  public ShareAndroidModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "ShareAndroidModule";
  }

  /**
   * Open a chooser dialog to send text content to other apps.
   *
   * Refer http://developer.android.com/intl/ko/training/sharing/send.html
   *
   * @param content the data to send
   * @param dialogTitle the title of the chooser dialog
   */
  @ReactMethod
  public void shareAndroid(ReadableMap content, String dialogTitle, Promise promise) {
    if (content == null) {
      promise.reject(ERROR_INVALID_CONTENT, "Content cannot be null");
      return;
    }

    try {
      Intent intent = new Intent(Intent.ACTION_SEND);
      intent.setTypeAndNormalize("text/plain");

      if (content.hasKey("url")) {
        intent.setTypeAndNormalize("image/jpeg");
        String strPath=content.getString("url");
        String fileName = Uri.parse(strPath).getLastPathSegment();
        File f = new File(reactContext.getCacheDir().getPath(), fileName);
        String packageName = reactContext.getApplicationContext().getPackageName();
        String providerAuthority = new StringBuilder(packageName).append(".ShareAndroidProvider").toString();
        Uri u = ShareAndroidFileProvider.getUriForFile(reactContext, providerAuthority, f);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra(Intent.EXTRA_STREAM, u);
      }
      if (content.hasKey("title")) {
        intent.putExtra(Intent.EXTRA_SUBJECT, content.getString("title"));
      }
      if (content.hasKey("message")) {
        intent.putExtra(Intent.EXTRA_TEXT, content.getString("message"));
      }

      Intent chooser = Intent.createChooser(intent, dialogTitle);
      chooser.addCategory(Intent.CATEGORY_DEFAULT);

      Activity currentActivity = getCurrentActivity();
      if (currentActivity == null) {
        currentActivity = getReactApplicationContext().getCurrentActivity();
      }
      currentActivity.startActivity(chooser);
      WritableMap result = Arguments.createMap();
      result.putString("action", ACTION_SHARED);
      promise.resolve(result);
    } catch (Exception e) {
      promise.reject(ERROR_UNABLE_TO_OPEN_DIALOG, e.getMessage());
    }
  }

}
