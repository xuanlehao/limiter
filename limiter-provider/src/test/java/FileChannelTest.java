import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Leo xuan
 * @date 2018/10/9
 */
public class FileChannelTest {

	@Test
	public void testRead() throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(48);
		RandomAccessFile aFile = new RandomAccessFile("G:\\niotest.txt", "rw");
		FileChannel fileChannel = aFile.getChannel();
		int bytesRead = fileChannel.read(buffer);
		while (bytesRead != -1) {
			buffer.flip();
			while (buffer.hasRemaining())
				System.out.print((char) buffer.get());
			buffer.clear();
			bytesRead = fileChannel.read(buffer);
		}
		fileChannel.close();
	}

	@Test
	public void testWrite() throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(48);
		RandomAccessFile aFile = new RandomAccessFile("G:\\niotest.txt", "rw");
		FileChannel fileChannel = aFile.getChannel();
		int i = 3;
		while (i-- > 0) {
			String strs = "This words is writed by xuan " + System.currentTimeMillis();
			buf.clear();
			buf.put(strs.getBytes());
			buf.flip();
			while (buf.hasRemaining()) {
				fileChannel.write(buf);
			}
		}
		fileChannel.close();
	}
}
