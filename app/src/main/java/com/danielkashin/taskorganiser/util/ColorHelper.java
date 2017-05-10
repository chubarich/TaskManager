package com.danielkashin.taskorganiser.util;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import java.util.Arrays;
import java.util.List;

public class ColorHelper {

  private static List<Integer> materialColors = Arrays.asList(
      0xffe57373,
      0xfff06292,
      0xffba68c8,
      0xff9575cd,
      0xff7986cb,
      0xff64b5f6,
      0xff4fc3f7,
      0xff4dd0e1,
      0xff4db6ac,
      0xff81c784,
      0xffff8a65,
      0xffffb74d,
      0xffa1887f,
      0xff90a4ae
  );

  public static int getMaterialColor(Object key) {
    return materialColors.get(Math.abs(key.hashCode()) % materialColors.size());
  }

  public static Drawable generateCircleBitmap(Context context, int circleColor, float diameterDP, String text) {
    final int textColor = 0xffffffff;

    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
    float diameterPixels = diameterDP * (metrics.densityDpi / 160f);
    float radiusPixels = diameterPixels / 2;

    // Create the bitmap
    Bitmap output = Bitmap.createBitmap((int) diameterPixels, (int) diameterPixels,
        Bitmap.Config.ARGB_8888);

    // Create the canvas to draw on
    Canvas canvas = new Canvas(output);
    canvas.drawARGB(0, 0, 0, 0);

    // Draw the circle
    final Paint paintC = new Paint();
    paintC.setAntiAlias(true);
    paintC.setColor(circleColor);
    canvas.drawCircle(radiusPixels, radiusPixels, radiusPixels, paintC);

    return new BitmapDrawable(context.getResources(), output);
  }
}
