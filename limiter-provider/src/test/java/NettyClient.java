import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @author Leo xuan
 * @date 2018/10/31
 */
public class NettyClient {
	private final String host;
	private final int port;

	public NettyClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group)
					.channel(NioSocketChannel.class)
					.remoteAddress(new InetSocketAddress(host,port))
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch)
								throws Exception {
							ch.pipeline().addLast(new ClientHandler());
						}
					});
			ChannelFuture f = b.connect().sync();

			f.channel().closeFuture().sync();
		}finally {
			group.shutdownGracefully().sync();
		}
	}

	public static void main(String[] args) throws Exception {
		new NettyClient("127.0.0.1",8080).start();
	}

	class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

		@Override
		public void channelActive(ChannelHandlerContext ctx){
			ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
		}
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
			System.out.println("Client received: "+in.toString(CharsetUtil.UTF_8));
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx,
		                            Throwable cause) {
			cause.printStackTrace();
			ctx.close();
		}

	}
}
