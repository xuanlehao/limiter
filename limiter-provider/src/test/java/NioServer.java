import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author Leo xuan
 * @date 2018/10/8
 */
public class NioServer {

	private static final int port = 8888;

	private static final int size = 1024;

	private static final int timeout = 10000;

	public static void main(String[] args) throws IOException {

		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.socket().bind(new InetSocketAddress(port));
		serverChannel.configureBlocking(false);

		Selector selector = Selector.open();
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);

		while (true) {
			if (selector.select(timeout) == 0) {
				System.out.println(".");
				continue;
			}
			// 获得就绪信道的键迭代器
			Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();

			//使用迭代器进行遍历就绪信道
			while (keyIter.hasNext()) {
				SelectionKey key = keyIter.next();

				// 这种情况是有客户端连接过来,准备一个clientChannel与之通信
				if (key.isAcceptable()) {
					SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
					clientChannel.configureBlocking(false);
					clientChannel.register(selector, SelectionKey.OP_READ,
							ByteBuffer.allocate(size));
				}
				// 客户端有写入时
				if (key.isReadable()) {
					// 获得与客户端通信的信道
					SocketChannel clientChannel = (SocketChannel) key.channel();
					// 得到并重置缓冲区的主要索引值
					ByteBuffer buffer = (ByteBuffer) key.attachment();
					buffer.clear();
					// 读取信息获得读取的字节数
					long bytesRead = clientChannel.read(buffer);
					if (bytesRead == -1) {
						// 没有读取到内容的情况
						clientChannel.close();
					} else {
						// 将缓冲区准备为数据传出状态
						buffer.flip();
						// 将获得字节字符串(使用Charset进行解码)
						String receivedString = Charset
								.forName("utf-8").newDecoder().decode(buffer).toString();
						// 控制台打印出来
						System.out.println("接收到信息:" + receivedString);
						// 准备发送的文本
						String sendString = "你好,客户端. 已经收到你的信息" + receivedString;
						// 将要发送的字符串编码(使用Charset进行编码)后再进行包装
						buffer = ByteBuffer.wrap(sendString.getBytes("utf-8"));
						// 发送回去
						clientChannel.write(buffer);
						// 设置为下一次读取或是写入做准备
						key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
					}
				}
				keyIter.remove();
			}


		}
	}
}
