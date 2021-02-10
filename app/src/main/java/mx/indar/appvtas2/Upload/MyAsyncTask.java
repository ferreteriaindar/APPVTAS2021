package mx.indar.appvtas2.Upload;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

public  class MyAsyncTask extends AsyncTask<Integer,String,Boolean> {

    Context context;
    ProgressDialog pd ;
    boolean ConDialog=true;
    public MyAsyncTask(Context context,boolean ConDialog) {
        this.context=context;
        pd =new ProgressDialog(context);
        this.ConDialog=ConDialog;
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {

        if(integers[0]==1)
        {
            UploadList ul = new UploadList(context);
            ul.uploadVisitasV2();

        }
        switch (integers[0])
        {
            case 1:  UploadList ul = new UploadList(context);
                ul.uploadVisitasV2();
                ul.uploadCobros();

                break;

            case 2: UploadList ul2 = new UploadList(context);
                ul2.uploadCobros();
                break;

        }
        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(ConDialog)
            pd.show();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(ConDialog)
            pd.dismiss();


    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
