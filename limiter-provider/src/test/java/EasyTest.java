import org.junit.Test;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leo xuan
 * @date 2018/10/12
 */
public class EasyTest {

	public static void main(String[] args) {
		String str = System.getProperty("ssl");
		System.out.println(Runtime.getRuntime().availableProcessors());
		JButton jButton = new JButton();
		jButton.addActionListener((e) -> System.out.println("按钮被点击了"));
		new Thread(() -> System.out.println()).start();
	}

	@Test
	public void mapTest() {
		List<Double> cost = Arrays.asList(10.0, 20.0,30.0);
		List<Double> b = cost.stream().map(x -> x + x*0.05).filter(n-> n>30).collect(Collectors.toList());

	}
}
