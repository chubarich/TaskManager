package com.danielkashin.taskorganiser.domain_layer.helper;


import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.widget.TextView;

public class TextViewHelper {

  public static void animateStrikeThrough(final TextView tv) {
    final int ANIM_DURATION = 1000;              //duration of animation in millis
    final int length = tv.getText().length();
    new CountDownTimer(ANIM_DURATION, ANIM_DURATION/length) {
      Spannable span = new SpannableString(tv.getText());
      StrikethroughSpan strikethroughSpan = new StrikethroughSpan();

      @Override
      public void onTick(long millisUntilFinished) {
        //calculate end position of strikethrough in textview
        int endPosition = (int) (((millisUntilFinished-ANIM_DURATION)*-1)/(ANIM_DURATION/length));
        endPosition = endPosition > length ?
            length : endPosition;
        span.setSpan(strikethroughSpan, 0, endPosition,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(span);
      }

      @Override
      public void onFinish() {

      }
    }.start();
  }

}
