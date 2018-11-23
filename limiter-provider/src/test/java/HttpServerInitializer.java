import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author Leo xuan
 * @date 2018/11/2
 */
public class HttpServerInitializer  extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
//		pipeline.addLast("decoder", new HttpRequestDecoder());
//		pipeline.addLast("encoder", new HttpResponseEncoder());
//		pipeline.addLast("httpServerCodec",new HttpServerCodec());
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("aggregator", new HttpObjectAggregator(2147483647));
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		pipeline.addLast("deflater", new HttpContentCompressor());
		pipeline.addLast("httpHandler",new NettyServer2.HttpHandler());
	}
}
