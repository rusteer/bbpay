package com.bbpay.android.manager;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.FROYO;
import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.bbpay.android.utils.ResourceUtils;

public class PayDialog extends ProgressDialog {
    private static final int THEME = THEME_HOLO_DARK;
    public static class LightAlertDialog extends AlertDialog {
        public static AlertDialog create(final Context context) {
            return SDK_INT >= ICE_CREAM_SANDWICH ? new LightAlertDialog(context, THEME) : new LightAlertDialog(context);
        }
        private LightAlertDialog(final Context context) {
            super(context);
        }
        private LightAlertDialog(final Context context, final int theme) {
            super(context, theme);
        }
    }
    public static AlertDialog create(Context context, String message, boolean cancelable) {
        if (SDK_INT > FROYO) {
            ProgressDialog dialog;
            if (SDK_INT >= ICE_CREAM_SANDWICH) dialog = new PayDialog(context, message);
            else {
                dialog = new ProgressDialog(context);
            }
            dialog.setMessage(message);
            dialog.setIndeterminate(true);
            dialog.setProgressStyle(STYLE_SPINNER);
            dialog.setIndeterminateDrawable(context.getResources().getDrawable(ResourceUtils.getDrawableIndex(context, "bbpay_spinner")));
            dialog.setCancelable(cancelable);
            return dialog;
        } else {
            AlertDialog dialog = LightAlertDialog.create(context);
            View view = LayoutInflater.from(context).inflate(ResourceUtils.getLayoutIndex(context, "bbpay_progress_dialog"), null);
            ((TextView) view.findViewById(ResourceUtils.getIdIndex(context, "bbpay_tv_loading"))).setText(message);
            dialog.setView(view);
            dialog.setCancelable(cancelable);
            return dialog;
        }
    }
    private PayDialog(Context context, CharSequence message) {
        super(context, THEME);
    }
}
