package com.brentondurkee.ccm.inbox;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brentondurkee.ccm.R;
import com.brentondurkee.ccm.provider.DataContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MsgDetail extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MsgDetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MsgDetailFragment extends Fragment {

        final String[] PROJECTION = new String[]{
            DataContract.Msg.COLUMN_NAME_FROM,
                DataContract.Msg.COLUMN_NAME_TO,
                DataContract.Msg.COLUMN_NAME_SUBJECT,
                DataContract.Msg.COLUMN_NAME_DATE,
                DataContract.Msg.COLUMN_NAME_MESSAGE
        };

        public MsgDetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle extras = getActivity().getIntent().getExtras();
            String id = extras.getString("id");
            Cursor mCursor = getActivity().getContentResolver().query(DataContract.Msg.CONTENT_URI, PROJECTION, DataContract.Msg._ID + "='" + id + "'", null, null);
            mCursor.moveToFirst();
            String from = mCursor.getString(0);
            String to = mCursor.getString(1);
            String subject = mCursor.getString(2);
            String date = mCursor.getString(3);
            String message = mCursor.getString(4);
            Date time;
            try {
                date = date.replace("Z", " GMT");
                time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS zzz").parse(date);
                long millis = (time.getTime() - System.currentTimeMillis());
                long seconds = (millis/1000)%60;
                long mins = (millis/60000)%60;
                long hours = (millis/3600000)%24;
                long days = (millis/86400000);
                date = String.format("%d days %d:%d:%d", days, hours, mins, seconds);
                Log.v("Time Parse", time.toString());
            }
            catch (ParseException e){
                Log.w("Time Parse Exception", e.toString());
            }

            View rootView = inflater.inflate(R.layout.fragment_msg_detail, container, false);
            ((TextView) rootView.findViewById(R.id.msgDetailSubject)).setText(subject);
            ((TextView) rootView.findViewById(R.id.msgDetailFrom)).setText(from);
            ((TextView) rootView.findViewById(R.id.msgDetailTo)).setText(to);
            ((TextView) rootView.findViewById(R.id.msgDetailTime)).setText(date);
            ((TextView) rootView.findViewById(R.id.msgDetailMsg)).setText(message);
            return rootView;
        }
    }
}
