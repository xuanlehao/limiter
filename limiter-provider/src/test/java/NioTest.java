import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Leo xuan
 * @date 2018/10/8
 */
public class NioTest {

	@Test
	public void nioTest() throws IOException {
		FileInputStream fin = new FileInputStream("G:\\niotest.txt");
		FileChannel fc = fin.getChannel();
		ByteBuffer buf = ByteBuffer.allocate(1024);
		fc.read(buf);
		System.out.println(buf.get());
	}
}
