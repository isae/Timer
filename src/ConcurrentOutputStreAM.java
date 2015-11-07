import java.io.*;
import java.util.Random;

/**
 * Provides opportunity to write concurrently into same file with wait-free guarantee, output from
 * only a single thread will actually be written. File will not be seen before stream is closed.
 * <p>
 * The main guarantee of this class is the following: when some threads write to the same file via
 * this stream and no one tries to delete it, one of those threads will successfully create and fill
 * the file.
 * <p>
 * Note: while this stream is opened, some temporal file will exist in directory which
 * contains target file.
 * <p>
 * Note: instance of this class cannot be used concurrently.
 */
public class ConcurrentOutputStream extends OutputStream implements Closeable {
    private final File targetFile;

    private final File tempFile;

    private final OutputStream os;

    private boolean failedToWrite = false;

    /**
     * Creates a stream. If file already exists, writing to stream have no sense, so
     * it is recommended to check target file presence on disk before calling this
     * constructor.
     *
     * @throws IOException if I/O error occurs
     */
    public ConcurrentOutputStream(String filename) throws IOException {
        this(new File(filename));
    }

    /**
     * Equivalent to calling {@code ConcurrentOutputStream}(file.getPath()).
     */
    public ConcurrentOutputStream(File file) throws IOException {
        targetFile = file;
        tempFile = createTempFile(targetFile.toString() + ".%s.tmp");
        os = new FileOutputStream(tempFile);
    }

    private File createTempFile(String format) throws IOException {
        Random rand = new Random();
        while (true) {
            File tempFile = new File(String.format(format, String.valueOf(Math.abs(rand.nextInt()))));
            if (tempFile.createNewFile())
                return tempFile;
        }

    }

    @Override
    public void write(byte[] b) throws IOException {
        try {
            os.write(b);
        } catch (IOException e) {
            failedToWrite = true;
            throw e;
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        try {
            os.write(b, off, len);
        } catch (IOException e) {
            failedToWrite = true;
            throw e;
        }
    }

    @Override
    public void write(int b) throws IOException {
        try {
            os.write(b);
        } catch (IOException e) {
            failedToWrite = true;
            throw e;
        }
    }

    @Override
    public void flush() throws IOException {
        try {
            os.flush();
        } catch (IOException e) {
            failedToWrite = true;
            throw e;
        }
    }

    /**
     * Closes the stream, target file appears at due location.
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        try {
            os.close();
            if (!failedToWrite) {
                tempFile.renameTo(targetFile);
            }
        } finally {
            tempFile.delete();
        }
    }
}