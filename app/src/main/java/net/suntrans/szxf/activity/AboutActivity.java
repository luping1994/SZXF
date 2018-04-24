package net.suntrans.szxf.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import net.suntrans.szxf.BuildConfig;
import net.suntrans.szxf.R;

import java.io.IOException;

/**
 * Created by Looney on 2017/7/24.
 */
public class AboutActivity extends BasedActivity {

    private TextView guangwang;
    private SoundPool sp;
    private int load_id;
    private AssetFileDescriptor assetFileDescriptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.fanhui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView textView = (TextView) findViewById(R.id.version);
        textView.setText(getString(R.string.tx_version_code) + BuildConfig.VERSION_NAME);
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(getString(R.string.tx_share_app));
            }
        });



    }

    private void load() {
        try {
            assetFileDescriptor = getAssets().openFd("sound/"+"dev.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }
        load_id = sp.load(assetFileDescriptor,1);

         int play = sp.play(load_id, 0.8f, 0.8f, 1, 0, 1.0f);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void share(String desc) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.tx_share_app_des));
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, desc));
    }


}
