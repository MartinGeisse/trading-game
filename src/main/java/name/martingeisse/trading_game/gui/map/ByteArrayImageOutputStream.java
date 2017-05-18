package name.martingeisse.trading_game.gui.map;

import javax.imageio.stream.ImageOutputStreamImpl;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 */
public class ByteArrayImageOutputStream extends ImageOutputStreamImpl {

	protected byte data[];
	protected int dataSize;

	public ByteArrayImageOutputStream() {
		this(32);
	}

	public ByteArrayImageOutputStream(int initialCapacity) {
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("initialCapacity is negative");
		}
		data = new byte[initialCapacity];
		dataSize = 0;
	}

	private void makeRoom(int minCapacity) {
		if (minCapacity > data.length) {
			int doubled = data.length << 1;
			data = Arrays.copyOf(data, doubled < minCapacity ? minCapacity : doubled);
		}
	}


	public int read() throws IOException {
		int n = beginRead(1);
		int value = (n == 0 ? -1 : data[(int) streamPos]);
		streamPos++;
		return value;
	}

	public int read(byte[] toBuffer, int toOffset, int readLength) throws IOException {
		if (toOffset < 0 || readLength < 0 || toOffset + readLength > toBuffer.length) {
			throw new IndexOutOfBoundsException();
		}
		if (readLength == 0) {
			return 0;
		}
		int n = beginRead(readLength);
		if (n == 0) {
			return -1;
		}
		System.arraycopy(data, (int) streamPos, toBuffer, toOffset, n);
		return n;
	}

	private int beginRead(int readLength) throws IOException {
		checkClosed();
		bitOffset = 0;
		int available = (int) (dataSize - streamPos);
		return (available < readLength) ? available : readLength;
	}

	public void write(int b) throws IOException {
		beginWrite(1);
		data[(int) streamPos] = (byte) b;
		finishWrite(1);
	}

	public void write(byte[] fromBuffer, int fromOffset, int writeLength) throws IOException {
		if (fromOffset < 0 || writeLength < 0 || fromOffset + writeLength > fromBuffer.length) {
			throw new IndexOutOfBoundsException();
		}
		beginWrite(writeLength);
		System.arraycopy(fromBuffer, fromOffset, data, (int) streamPos, writeLength);
		finishWrite(writeLength);
	}

	private void beginWrite(int writeLength) throws IOException {
		checkClosed();
		flushBits();
		makeRoom((int) streamPos + writeLength);
	}

	private void finishWrite(int writeLength) throws IOException {
		streamPos += writeLength;
		if (streamPos > dataSize) {
			dataSize = (int) streamPos;
		}
	}

	public long length() {
		try {
			checkClosed();
			return dataSize;
		} catch (IOException e) {
			return -1L;
		}
	}

	public void seek(long position) throws IOException {
		checkClosed();
		if (position < flushedPos) {
			throw new IndexOutOfBoundsException("pos < flushedPos!");
		}
		bitOffset = 0;
		streamPos = position;
	}

	public byte[] toByteArray() {
		return Arrays.copyOf(data, dataSize);
	}

}
