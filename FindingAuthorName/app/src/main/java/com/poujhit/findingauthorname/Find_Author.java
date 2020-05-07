package com.poujhit.findingauthorname;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Find_Author extends AsyncTask<String, Integer, String> {

    Context ctx;
    ProgressDialog pd;
    private int j=0;


    Find_Author(Context ct){
        ctx=ct;
    }

    private void UpdateProgressBar(){
        j++;
        publishProgress(j);
    }

    @Override
    protected void onPreExecute() {
        pd= new ProgressDialog(ctx);
        pd.setTitle("Getting Info");
        pd.setMessage("Loading.....");
        pd.setMax(10);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setButton(ProgressDialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });

        pd.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
              pd.setProgress(values[0]);
              if(values[0]==10)
                  pd.dismiss();
    }

    @Override
    protected String doInBackground(String... strings) {
        String baseUrl = "https://www.googleapis.com/books/v1/volumes?";
        Uri built= Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter("q",strings[0])
                .appendQueryParameter("maxResults","10")
                .appendQueryParameter("printType","books")
                .build();

                     UpdateProgressBar();


        try {
            URL url= new URL(built.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

                      UpdateProgressBar();
                      UpdateProgressBar();
                      UpdateProgressBar();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String l="";

                       UpdateProgressBar();
                       UpdateProgressBar();

            while((l= reader.readLine())!=null){
                stringBuilder.append(l);
                stringBuilder.append("\n");
            }


                       UpdateProgressBar();


            if(stringBuilder.length()==0){
                return null;
            }
            String JSONString= stringBuilder.toString();


                      UpdateProgressBar();



            if(reader !=null) {
                try{
                    reader.close();
                }
                catch (Exception e){
                e.printStackTrace();
                }
            }
            if(httpURLConnection !=null)
            httpURLConnection.disconnect();


                         UpdateProgressBar();

                         UpdateProgressBar();


            return JSONString;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s==null){
            Toast.makeText(ctx, "Entry Empty",Toast.LENGTH_SHORT).show();
        }

        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray items = jsonObject.getJSONArray("items");
            int i=0;
            String title=null, author=null;

            while(i<items.length()&& title==null&& author==null){

                JSONObject books= items.getJSONObject(i);
                JSONObject volume= books.getJSONObject("volumeInfo");

                try{
                    title= volume.getString("title");
                    author = volume.getString("authors");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                i++;
            }
            if(title!=null && author!=null){
                MainActivity.output.setText(title);
                MainActivity.output.append("\n");
                MainActivity.output.append(author);
            }
            else {
                Toast.makeText(ctx, " Not Found", Toast.LENGTH_LONG).show();
                MainActivity.output.setText("Not Found");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ctx, " Not Found", Toast.LENGTH_LONG).show();
            MainActivity.output.setText("Not Found");
        }

    }
}
