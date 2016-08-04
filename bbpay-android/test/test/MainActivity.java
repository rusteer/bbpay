package test;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.export.InitCallback;
import com.export.Pay;
import com.export.PayCallback;

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
                        Toast.makeText(MainActivity.this, "payResult:" + result + "\nmessage:" + message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        layout.addView(pay);
        setContentView(layout);
    }
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Pay.init(MainActivity.this, appId, channelId, new InitCallback() {
            @Override
            public void onResult(int result, String message) {
                Toast.makeText(MainActivity.this, "initResult:" + result + "\nmessage:" + message, Toast.LENGTH_LONG).show();
            }
        });
        doAction();
    }
}
