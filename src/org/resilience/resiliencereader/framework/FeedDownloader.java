package org.resilience.resiliencereader.framework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.resilience.resiliencereader.entities.ArticleList;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.ProgressDialog;
import android.os.AsyncTask;

// Beetje overbodig, deze class: De gebruiker moet er toch op wachten, waarom ophalen niet op de UI thread doen?
public class FeedDownloader extends AsyncTask<String, Integer, ArticleList> {

	private ProgressDialog progressDialog;
	private OnFeedDownloaderDoneListener feedDownloaderCallbackReceiver;

	public FeedDownloader(ProgressDialog progressDialog, OnFeedDownloaderDoneListener feedDownloaderCallbackReceiver) {
		this.progressDialog = progressDialog;
		this.feedDownloaderCallbackReceiver = feedDownloaderCallbackReceiver;
	}
	
	@Override
	protected void onPreExecute () {
		if(progressDialog != null) {
			progressDialog.show();
		}
	}

	@Override
	protected ArticleList doInBackground(String... urlString) {
		ArticleList result = null;
		try
		{
			// TODO: check if we have an internet connection
			URL url = new URL(urlString[0]);
			URLConnection connection = url.openConnection();
	        connection.connect();
	        // this will be useful so that you can show a typical 0-100% progress bar
	        int fileLength = connection.getContentLength();
	        
	        // download the file
	        InputStream input = connection.getInputStream();
	        int streamLength = 0;
	        if(fileLength > -1)
	        {
	        	streamLength = fileLength;
	        }
	        else
	        {
	        	streamLength = 102400; // 100 kilobyte
	        }
	        ByteArrayOutputStream output = new ByteArrayOutputStream(streamLength);
	        
	        // Lezen per kilobyte
	        byte data[] = new byte[1024];
	        long total = 0;
	        int count;
	        while ((count = input.read(data)) != -1) {
	            total += count;
	            // publishing the progress....
	            publishProgress((int) (total * 100 / streamLength));
	            output.write(data, 0, count);
	        }
	        publishProgress(100);
	        
	        input.close();
	        output.flush();
	        InputSource is = new InputSource(new StringReader(output.toString())); // TODO: Possible risk: Wrong charset
	        
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    	Document doc = dBuilder.parse(is);
	    	
	        result = new ArticleList(doc);
		}
		catch(MalformedURLException ex)
		{
			ex.printStackTrace();
			// TODO: URL is not well formed
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			// TODO: Difficulty reading or writing
		}
		catch(SAXException ex)
		{
			ex.printStackTrace();
			// TODO: DocumentBuilder could not be made
		}
		catch (ParserConfigurationException ex) {
			// TODO: XML could not be parsed
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		progressDialog.setProgress(progress[0]);
	}
	
	@Override
	protected void onPostExecute(ArticleList result)
	{
		feedDownloaderCallbackReceiver.onFeedDownloaderDone(result);
		progressDialog.hide();
	}

}
