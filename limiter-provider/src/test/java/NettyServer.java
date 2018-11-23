import com.xuan.common.limiter.ManagerLimiter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;

/**
 * @author Leo xuan
 * @date 2018/10/31
 */
public class NettyServer {

	private static final ServerHandler myHandler = new ServerHandler();

	static ManagerLimiter manager = new ManagerLimiter();

	public void bind(int port) throws Exception {
		//配置服务端的NIO线程组
		//实际上EventLoopGroup就是Reactor线程组
		//两个Reactor一个用于服务端接收客户端的连接，另一个用于进行SocketChannel的网络读写
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			//ServerBootstrap对象是Netty用于启动NIO服务端的辅助启动类，目的是降低服务端开发的复杂度
			ServerBootstrap b = new ServerBootstrap();
			//Set the EventLoopGroup for the parent (acceptor) and the child (client).
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port))
					//绑定I/O事件的处理类ChildChannelHandler,作用类似于Reactor模式中的Handler类
					//主要用于处理网络I/O事件，例如记录日志，对消息进行编解码等
					.childHandler(new MyInitializer());
			//绑定监听端口，调用sync同步阻塞方法等待绑定操作完成，完成后返回ChannelFuture类似于JDK中Future
			ChannelFuture f = b.bind(port).sync();
			//使用sync方法进行阻塞，等待服务端链路关闭之后Main函数才退出
			f.channel().closeFuture().sync();
		} finally {
			//优雅退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}


	}

	public static void main(String[] args) throws Exception {
		int port = 8080;
		new NettyServer().bind(port);
	}

	@ChannelHandler.Sharable
	static class ServerHandler extends ChannelInboundHandlerAdapter {
		//每个信息入站都会调用
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			String url = null;
			FullHttpResponse response;
			if (msg instanceof HttpRequest) {
				DefaultHttpRequest request = (DefaultHttpRequest) msg;
				url = request.getUri();
//				System.out.println(url);
//				System.out.println("URI:" + request.getUri());
//				System.err.println(msg);
			}
			boolean res = manager.getPermit(url);
			System.out.println(res);
			if (res) {
				response = new DefaultFullHttpResponse(
						HttpVersion.HTTP_1_1,
						HttpResponseStatus.OK,
						Unpooled.wrappedBuffer("true".getBytes()));
			} else {
				response = new DefaultFullHttpResponse(
						HttpVersion.HTTP_1_1,
						HttpResponseStatus.OK,
						Unpooled.wrappedBuffer("false".getBytes()));
			}
			response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain;charset=UTF-8");
			response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
			response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
			ctx.writeAndFlush(response);
		}

		//通知处理器最后的channelread()是当前批处理中的最后一条消息时调用
		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		}

		//读操作时捕获到异常时调用
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			if(null != cause) cause.printStackTrace();
			if(null != ctx) ctx.close();
		}
	}


	class MyInitializer extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast(
					new HttpResponseEncoder(),
					new HttpRequestDecoder(),
					myHandler);
		}
	}

}
