package baptista.tiago.popularmovies.utils;

import android.content.Context;
import android.widget.Toast;

import baptista.tiago.popularmovies.R;
import baptista.tiago.popularmovies.ui.MainActivity;

/**
 * Created by Tiggi on 9/15/2015.
 */
public class NetworkUtils {

    private Context mContext;

    public void initialize(Context context) {
        mContext = context;
        //this.load();
    }

    public void popError() {
        //AlertDialogFragment dialog = new AlertDialogFragment();
        //dialog.show(getFragmentManager(), "error_dialog");
        //Toast.makeText(mContext, Context.getString(R.string.network_is_broken), Toast.LENGTH_SHORT).show();
    }
}
