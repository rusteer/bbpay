package test;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.hypay.android.InitCallback;
import com.hypay.android.Pay;
import com.hypay.android.PayCallback;

public class MainActivity extends Activity {
    private static final long appId = 10001;
    private static final int channelId = 1;
    void doAction() {
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final android.widget.EditText etPrice = new android.widget.EditText(this);
        etPrice.setText("1");
        layout.addView(etPrice);
        android.widget.Button pay = new android.widget.Button(this);
        pay.setText("pay");
        pay.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Integer.valueOf(etPrice.getText().toString());
                Pay.pay(MainActivity.this, price, new PayCallback() {
                    @Override
                    public void onResult(int result, String message) {
                        showResult(result == Pay.PAY_SUCCESS, message);
                    }
                });
            }
        });
        layout.addView(pay);
        setContentView(layout);
    }
    private boolean initSuccess = false;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Pay.init(MainActivity.this, appId, channelId, new InitCallback() {
            @Override
            public void onResult(int result, String message) {
                initSuccess = result == Pay.INIT_SUCCESS;
            }
        });
        doAction();
    }
    private void showResult(boolean success, String message) {
        String s = success ? "支付成功" : "支付失败";
        if (!success) {
            s = s + "\n错误原因:" + message;
        }
        new AlertDialog.Builder(this).setMessage(s).setPositiveButton("确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {}
        }).create().show();
    }
}
