package ee.webAppToolkit.example.website;

import java.util.Map.Entry;

import javax.inject.Inject;

import ee.webAppToolkit.core.BasicController;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.SiteMap;
import ee.webAppToolkit.core.WrappingController;
import ee.webAppToolkit.core.annotations.Post;

public class MainController extends BasicController implements WrappingController {

	@Inject
	protected SiteMap siteMap;

	public Result index() {
		return output("<h1>Core features</h1>");
	}

	@Post
	public Result get()
	{
		return output("<h1>Get request</h1>");
		
	}

	@Override
	public void beforeHandling(String memberName, Object controller) {
		// not implemented
	}

	private String _createNavigation(SiteMap siteMap, String context) {
		String str = "";
		for (Entry<String, SiteMap> siteMapEntry : siteMap.entrySet()) {
			SiteMap value = siteMapEntry.getValue();
			String key = siteMapEntry.getKey();
			String url = (context.length() > 0 ? "/" : "") + key;
			if (value.isEmpty())
			{
				str += "<a href=\"" + url + "\">" + url + "</a><br />";
			} else
			{
				str += _createNavigation(value, url);
			}
		}
		
		return str;
	}

	@Override
	public Result wrapResult(Result result, String memberName, Object controller) {
		result = output("<html><head></head><body>" +
		_createNavigation(siteMap, "/") +
		result.getContent() +
		"</body></html>");
		return result;
	}

}
