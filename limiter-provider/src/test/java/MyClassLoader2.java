import java.io.*;

/**
 * @author Leo xuan
 * @date 2018/11/22
 */
public class MyClassLoader2 extends  ClassLoader{

	public Class<?> defineMyClass( byte[] b, int off, int len)
	{
		return super.defineClass(b, off, len);
	}

}
