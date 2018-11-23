import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author Leo xuan
 * @date 2018/11/14
 */
public class ForwardServerInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("httpServerCodec",new HttpServerCodec());
		pipeline.addLast("ForwardHandler",new ForwardServer.ForwardHandler());
	}
}
