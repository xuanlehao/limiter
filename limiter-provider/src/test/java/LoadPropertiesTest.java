import com.xuan.common.limiter.ManagerLimiter;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Leo xuan
 * @date 2018/10/24
 */
public class LoadPropertiesTest {

	public static void main(String[] args) throws IOException {
		Properties properties = new Properties();
		// 使用InPutStream流读取properties文件
		BufferedReader bufferedReader = new BufferedReader(new FileReader("G:/jdbc.properties"));
		properties.load(bufferedReader);
		// 获取key对应的value值
		String str = properties.getProperty("jdbc.minIdle");
		System.out.println(str);
	}

	@Test
	public void test(){
		ManagerLimiter manager = new ManagerLimiter();
		for(int i=0;i<200;i++) {
			boolean res = manager.getPermit("sdasdadaadsa");
			System.out.println("sdasdadaadsa："+res);
		}

		for(int i=0;i<200;i++) {
			boolean res = manager.getPermit("qqqqqqqq");
			System.out.println("qqqqqqqq："+res);
		}
	}
}
