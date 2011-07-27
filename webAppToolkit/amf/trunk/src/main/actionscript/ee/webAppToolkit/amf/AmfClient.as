package ee.webAppToolkit.amf
{
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLStream;
	import flash.utils.ByteArray;

	public class AmfClient extends URLStream
	{
		public function AmfClient()
		{
		}
		
		public function request(url:String, data:Object):void
		{
			var byteArray:ByteArray = new ByteArray();
			byteArray.writeObject(data);
			
			var urlRequest:URLRequest = new URLRequest(url);
			urlRequest.method = URLRequestMethod.POST;
			urlRequest.contentType = "application/x-amf";
			urlRequest.data = byteArray;
			
			load(urlRequest);
		}
	}
}