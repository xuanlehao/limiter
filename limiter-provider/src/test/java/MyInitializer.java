import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author Leo xuan
 * @date 2018/11/2
 */
//public class MyInitializer extends ChannelInitializer<SocketChannel> {
//
//	private static final NettyServer.ServerHandler handler = new NettyServer.ServerHandler();
//
//	@Override
//	protected void initChannel(SocketChannel ch) throws Exception {
//		ch.pipeline().addLast(
//				new HttpResponseEncoder(),
//				new HttpRequestDecoder(),
//				new NettyServer.ServerHandler());
//	}
//}
