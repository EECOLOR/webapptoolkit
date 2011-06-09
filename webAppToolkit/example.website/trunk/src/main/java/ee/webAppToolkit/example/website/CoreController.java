package ee.webAppToolkit.example.website;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import ee.webAppToolkit.core.RequestMethod;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.Context;
import ee.webAppToolkit.core.annotations.Delete;
import ee.webAppToolkit.core.annotations.Get;
import ee.webAppToolkit.core.annotations.Optional;
import ee.webAppToolkit.core.annotations.Post;
import ee.webAppToolkit.core.annotations.Put;
import ee.webAppToolkit.parameters.annotations.Parameter;
import ee.webAppToolkit.render.RenderingController;

public class CoreController extends RenderingController {
	
	public Result index()
	{
		return render(new CoreModel((String) flash.get("result")));
	}
	
	@Post
	public void indexHandler(
			@Parameter("requestMethod") RequestMethod requestMethod, 
			@Optional @Parameter("key") String key,
			@Optional @Parameter("value") String value,
			HttpServletRequest httpServletRequest,
			@Context String context) throws IOException
	{
		
		URL url = new URL("http", httpServletRequest.getServerName(), httpServletRequest.getServerPort(), context + "/" + "rest");
		
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
        connection.setDoOutput(true); 
        connection.setInstanceFollowRedirects(false); 
        connection.setRequestMethod(requestMethod.toString()); 

        OutputStream outputStream = connection.getOutputStream();
        //TODO write output
        outputStream.flush();
        
        final char[] buffer = new char[0x10000];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(connection.getInputStream(), "UTF-8");
        int read;
        do {
          read = in.read(buffer, 0, buffer.length);
          if (read>0) {
            out.append(buffer, 0, read);
          }
        } while (read>=0);
        
        connection.getResponseCode();
        connection.disconnect(); 
		
		flash.put("result", out.toString());
		redirect("index");
	}
	
	@Get
	public Result rest(@Named("store") Map<String, String> store)
	{
		return render(store, "restGET", true);
	}
	
	@Put
	public Result rest(@Named("store") Map<String, String> store, 
		@Parameter("key") String key,
		@Parameter("value") String value) 
	{
		store.put(key, value);
		return output(key + " stored", true);
	}
	
	@Delete
	public Result rest(@Named("store") Map<String, String> store, 
			@Parameter("key") String key) 
	{
		store.remove(key);
		return output(key + " removed", true);
	}
	
	public class CoreModel
	{
		public String result;
		
		CoreModel(String result)
		{
			this.result = result;
		}
	}
}
