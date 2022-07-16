package javax.imageio;

import com.sun.xml.bind.v2.util.ByteArrayOutputStreamEx;

import java.awt.Image;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.stream.ImageOutputStream;

public class ImageIO {
    public static Iterator<ImageWriter> getImageWritersByMIMEType(String mimeType) {
        return null;
    }

    public static ImageOutputStream createImageOutputStream(ByteArrayOutputStreamEx imageData) {
        return null;
    }

    public static Image read(InputStream is) {
        return null;
    }
}
