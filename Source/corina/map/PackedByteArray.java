package corina.map;

public final class PackedByteArray {

    // arrays are 8-byte aligned, so the only way to store a large
    // amount of raw data in an array without holes is to use longs,
    // which are 8 bytes long.
    private long data[];

    // number of bytes (not longs)
    private int size;

    // create a new packedbytearray capable of holding |size| bytes,
    // initially full of zeros
    public PackedByteArray(int size) {
	this.size = size;
	data = new long[(int) Math.ceil(size/8.)];
    }

    // get byte |index|
    public byte get(int index) {
	// bounds checking
	if (index < 0 || index >= size)
	    throw new ArrayIndexOutOfBoundsException();

	// get the long
	int location = index / 8;
	long x = data[location];

	// extract one of its 8 bytes
	int which = index % 8;

	return (byte) ((x >>> (7-which)*8) & 0xff);
    }

    // set byte |index| to |value|
    public void set(int index, byte value) {
	// bounds checking
	if (index < 0 || index >= size)
	    throw new ArrayIndexOutOfBoundsException();

	// get the location
	int location = index / 8;

	// pick one of its 8 bytes
	int which = index % 8;

	// get old value
	long old = data[location];

	// mask-off what was there before
	long mask = 0xffL << (7 - which)*8;
	old &= ~mask;

	// or-on the new value
	old |= (((long) value) << (7 - which)*8) & mask;

	// writeback
	data[location] = old;
    }

    // the number of bytes this array is, in case you forgot
    public int size() {
	return size;
    }

    // print out the raw contents, in hex
    private void print() {
	System.out.println("PackedByteArray, size = " + size);
	for (int i=0; i<data.length; i++)
	    System.out.println("-- data[" + i + "] = " + Long.toHexString(data[i]));
    }

    // test the packedbytearray
    public static void main(String args[]) {
	// make an array this big
	final int N = 10000000;

	if (args.length>=1 && args[0].equalsIgnoreCase("packed")) {
	    System.out.println("Using a PackedByteArray");
	    PackedByteArray a = new PackedByteArray(N);

	    for (int i=0; i<N; i++) {
		byte x = (byte) (200 * Math.random());
		a.set(i, x);
	    }

	    used();
	} else {
	    System.out.println("Using a byte[]");
	    byte b[] = new byte[N];

	    for (int i=0; i<N; i++) {
		byte x = (byte) (200 * Math.random());
		b[i] = x;
	    }

	    used();
	}
    }

    public static void used() {
	long total = Runtime.getRuntime().totalMemory();
	long free = Runtime.getRuntime().freeMemory();
	System.out.println("total = " + total);
	System.out.println("free = " + free);
	System.out.println("used = " + (total-free));
    }
}
