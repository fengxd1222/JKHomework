import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供。
 * x=255-x
 */
public class HelloClassLoader extends ClassLoader {

    private static final byte STANDARD = (byte) 255;

    private String path;

    public HelloClassLoader(String path) {
        this.path = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = getBytes();
        if(Objects.isNull(bytes)){
            throw new ClassNotFoundException();
        }
        return defineClass(name,bytes,0,bytes.length);
    }

    public byte[] getBytes(){
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            return handleBytes(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] handleBytes(byte[] originBytes){
        if(originBytes!=null){
            byte[] realBytes = new byte[originBytes.length];

            int length = originBytes.length;
            for (int i = 0; i < length; i++) {
                realBytes[i] = (byte) (STANDARD-originBytes[i]);
            }
            return realBytes;
        }
        return new byte[0];
    }

    public static void main(String[] args) throws Exception {
        HelloClassLoader helloClassLoader = new HelloClassLoader("D:/JAVA/Jk/Hello.xlass");
        Class<?> hello = helloClassLoader.loadClass("Hello");

        Object obj = hello.newInstance();
        Method method = hello.getDeclaredMethod("hello");
        method.invoke(obj);
    }
}
