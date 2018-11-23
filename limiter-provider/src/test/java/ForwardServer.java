import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

import java.net.InetSocketAddress;

/**
 * @author Leo xuan
 * @date 2018/11/14
 */
public class ForwardServer {

	public void bind0(int port) throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup(128);

		try {
			ServerBootstrap bs = new ServerBootstrap();
			bs.group(bossGroup,workGroup)
					.channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port))
					.childHandler(new HttpServerInitializer());
			// Start the server.
			// 绑定端口，并同步等待成功，即启动服务端
			ChannelFuture channelFuture = bs.bind(port).sync();
			// Wait until the server socket is closed.
			// 监听服务端关闭，并阻塞等待
			channelFuture.channel().closeFuture().sync();
		}finally {
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	static class ForwardHandler extends SimpleChannelInboundHandler { //1

		private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

		}


		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			if(null != cause) cause.printStackTrace();
			if(null != ctx) ctx.close();
		}

	}
}
