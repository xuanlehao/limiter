
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.objectweb.asm.Opcodes.*;
import static org.springframework.cglib.core.ReflectUtils.defineClass;

/**
 * FIXME 字节码生成后，new实例报错
 * Created by wangzhiyuan on 2018/7/3
 */
public class Tsas {

	public static void main(String[] args) throws Exception {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		String classNameInternal = "Target";
		String className = classNameInternal.replace("/", ".");


		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, classNameInternal, null, null, null);

		MethodVisitor mv;
		// add constructor
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
			mv.visitInsn(RETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		// add method hello
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "hello", "()V", null, null);
			Label L0 = new Label();
			Label L1 = new Label();
			Label L2 = new Label();
			Label L3 = new Label();
			Label L4 = new Label();
			mv.visitTryCatchBlock(L0, L1, L2, "java/lang/Exception");

			mv.visitLabel(L0);
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitLdcInsn("hello world");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");

			mv.visitLabel(L1);
			mv.visitJumpInsn(GOTO, L3);

			mv.visitLabel(L2);
			mv.visitFrame(F_APPEND, 1, new Object[]{"java/lang/Exception"}, 0, null);
			mv.visitVarInsn(ASTORE, 0);

			mv.visitLabel(L4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V");

			mv.visitLabel(L3);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);

			mv.visitMaxs(2, 1);
			mv.visitEnd();

		}

		cw.visitEnd();

		byte[] data = cw.toByteArray();
		File file = new File("G://Target.class");
		FileOutputStream fout = new FileOutputStream(file);
		fout.write(data);
		fout.close();


		FileInputStream in = new FileInputStream(new File(("G://Target.class")));
		byte[] result = new byte[1024];
		int count = in.read(result);
		System.out.println((char)result[0]);
		MyClassLoader2 loader = new MyClassLoader2();
		Class clazz = loader.defineMyClass( result, 0,count);
		clazz.getMethod("hello",null).invoke(null);
//		MyClassLoader.output2File(className, data);
//
//		MyClassLoader classLoader = new MyClassLoader();
//		Class<?> target = classLoader.defineClassForName(className, data);
//		target.getMethod("hello", null).invoke(null);
	}




}
