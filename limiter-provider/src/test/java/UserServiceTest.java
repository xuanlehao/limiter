import com.xuan.dao.UserDao;
import com.xuan.pojo.UserVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Leo xuan
 * @date 2018/9/18
 */

@RunWith(SpringJUnit4ClassRunner.class)//表示整合JUnit4进行测试
@ContextConfiguration(locations = {"classpath:application-context.xml"})//加载spring配置文件
public class UserServiceTest {

	@Autowired
	UserDao userDao;

	@Test
	public void test() {
		UserVO users = userDao.selectById(1);
		System.out.println("success");
	}


	@Test
	public void cmdTest() throws IOException, InterruptedException {
		String cmd = "G:\\disconf\\test.bat";
		Process pc = Runtime.getRuntime().exec(cmd);
		InputStream in = pc.getInputStream();
		InputStream err = pc.getErrorStream();
		System.out.println("错误输出");
		BufferedReader errbr = new BufferedReader(new InputStreamReader(err));
		String errMsg;
		while ((errMsg = errbr.readLine()) != null) {
			System.out.println(errMsg);
		}
		System.out.println("正常输出");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = "";
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		System.out.println("------------------");
		System.out.println(pc.waitFor());
	}

	@Test
	public void testcmd2() throws IOException, InterruptedException {
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec("javac");
		InputStream stderr = proc.getErrorStream();
		InputStreamReader isr = new InputStreamReader(stderr);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		System.out.println("<error></error>");
		while ((line = br.readLine()) != null)
			System.out.println(line);
		System.out.println("");
		int exitVal = proc.waitFor();
		System.out.println("Process exitValue: " + exitVal);
	}
}
