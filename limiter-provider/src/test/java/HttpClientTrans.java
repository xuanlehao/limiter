import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpVersion;
import org.apache.http.*;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Leo xuan
 * @date 2018/11/16
 */
public class HttpClientTrans {


	public static HttpRequest castHttpRequest(FullHttpRequest fullHttpRequest) throws Exception {
		String methodName = fullHttpRequest.method().name();
		String uri = fullHttpRequest.uri();
		String url = "http://10.202.36.84:8888" + uri;

		if (methodName.equals("GET")) {
			HttpGet httpRequest = new HttpGet(url);
			//设置请求头
			for (String name : fullHttpRequest.headers().names()) {
				for (String value : fullHttpRequest.headers().getAll(name)) {
					httpRequest.addHeader(name, value);
				}
			}
			return httpRequest;
		}

		if (methodName.equals("POST")) {
			HttpPost httpRequest = new HttpPost(url);
			//设置请求头
			for (String name : fullHttpRequest.headers().names()) {
				for (String value : fullHttpRequest.headers().getAll(name)) {
					httpRequest.addHeader(name, value);
				}
			}
			//HttpRequest会重新设置长度
			httpRequest.removeHeaders(HTTP.CONTENT_LEN);
			//设置body
			ByteBuf buf = fullHttpRequest.content();
			httpRequest.setEntity(new StringEntity(buf.toString(io.netty.util.CharsetUtil.UTF_8)));

			return httpRequest;
		}
		return null;
	}


	public static DefaultFullHttpResponse castDFHResponse(CloseableHttpResponse response) throws Exception {
		//判断是否keepAlived
		boolean keepAlived = true;
		Header[] s = response.getHeaders("Connection");
		if (s == null || s.length == 0 || !s[0].getValue().equals("keep-alive"))
			keepAlived = false;

		//获取http协议状态
		StatusLine statusLine = response.getStatusLine();
		ProtocolVersion protocolVersion = statusLine.getProtocolVersion();

		//获取返回内容
		HttpEntity entity = response.getEntity();
		byte[] bytes;
		if(entity != null) {
			InputStream in = entity.getContent();
			bytes = new byte[(int) response.getEntity().getContentLength()];
			in.read(bytes);
		}else
			bytes = "".getBytes();

		DefaultFullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(
				new HttpVersion(protocolVersion.getProtocol(), protocolVersion.getMajor(), protocolVersion.getMinor(), keepAlived),
				new HttpResponseStatus(statusLine.getStatusCode(), statusLine.getReasonPhrase()),
				Unpooled.wrappedBuffer(bytes));

		//设置返回头信息
		HttpHeaders headers = fullHttpResponse.headers();
		Header[] allHeader = response.getAllHeaders();
		for (Header a : allHeader) {
			headers.add(a.getName(), a.getValue());
		}

		return fullHttpResponse;
	}

	@Test
	public void test() throws IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet("http://10.202.36.84:8888/");
		CloseableHttpResponse response = client.execute(get);
		System.out.println(response.getStatusLine());
	}


}
