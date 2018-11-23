import com.xuan.common.limiter.ManagerLimiter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Leo xuan
 * @date 2018/11/2
 */
public class NettyServer2 {

	static ManagerLimiter manager = new ManagerLimiter();
	static AtomicLong count= new AtomicLong(0);
	public void bind(int port) throws Exception {

		EventLoopGroup bossGroup = new NioEventLoopGroup(1); // 创建 boss 线程组 用于服务端接受客户端的连接
		EventLoopGroup workGroup = new NioEventLoopGroup();// 创建 worker 线程组 用于进行 SocketChannel 的数据读写
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup)
					.channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port))
					.option(ChannelOption.SO_BACKLOG, 128)
					.childHandler(new HttpServerInitializer());
			// Start the server.
			// 绑定端口，并同步等待成功，即启动服务端
			ChannelFuture f = b.bind(port).sync();
			// Wait until the server socket is closed.
			// 监听服务端关闭，并阻塞等待
			f.channel().closeFuture().sync();
		} finally {
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new NettyServer2().bind(8080);
	}


	static class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

		private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
				String uri = request.getUri();
				boolean res = manager.getPermit(uri);
				DefaultFullHttpResponse response;

				if (res) {
					long start = System.currentTimeMillis();
					CloseableHttpClient client = HttpClients.createDefault();
					org.apache.http.HttpRequest httpRequest = HttpClientTrans.castHttpRequest(request);

					if(httpRequest == null)
						throw new NullPointerException("HttpClient请求错误,http请求为空");
					CloseableHttpResponse httpResponse = client.execute((HttpUriRequest) httpRequest);
					long end = System.currentTimeMillis();
					response = HttpClientTrans.castDFHResponse(httpResponse);
					System.out.println("httpClient 耗时" + (end - start));
				} else {
					response = new DefaultFullHttpResponse(
							HttpVersion.HTTP_1_1,
							HttpResponseStatus.OK,
							Unpooled.wrappedBuffer("false:你被限流了！".getBytes()));


					HttpHeaders heads = response.headers();
					heads.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
					heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes()); // 3
					heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
				}

				ctx.writeAndFlush(response);
			}


		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			if(null != cause) cause.printStackTrace();
			if(null != ctx) ctx.close();
		}

	}
}
