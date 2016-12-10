package uk.ac.ed.epcc.mountain;

//import java.util.Random;

public final class Mountain {
	final double mean = 0.0; // Mean height
	boolean rg1 = false;
	boolean rg2 = false;
	boolean rg3 = true;
	boolean cross = true;
	int force_front = 1;
	int force_back = 0;
	final double forceval = 0.0;
	double mix = 0.0;
	double midmix = 0.0;
	double fdim = 0.65;
	int levels = 10;
	int stop = 2;
	int width; /* width of surface in points */
	double fwidth; /*
					 * width of surface in same units as height
					 */
	public final double mwidth = 1.0; /*
										 * longest fractal lengthscale in same
										 * units as height
										 */
	// public Random r=new Random();
	public Uni r = new Uni();

	private Fold f = null;
	private boolean initialised = false;
	private final boolean debug = false;

	public synchronized void set_seed(int i) {
		r = new Uni(i);
	}

	public synchronized void set_size(int l, int s) {
		if (s < 0 || l < s)
			return;
		if (initialised) {
			clear();
		}
		levels = l;
		stop = s;
	}

	public synchronized void set_rg(boolean r1, boolean r2, boolean r3) {
		// Changing this would mess up the pipeline so re-initialise
		if (initialised) {
			clear();
		}
		rg1 = r1;
		rg2 = r2;
		rg3 = r3;
	}

	public synchronized void set_cross(boolean c) {
		// We can change this during the update
		cross = c;
	}

	public synchronized void set_fdim(double fd) {
		// We can change this during the update
		if (fd < 0.5 || fd > 1.0)
			return;
		fdim = fd;
		if (initialised) {
			f.sync();
		}
	}

	public synchronized void set_front(int l) {
		force_front = l;
	}

	public synchronized void set_back(int l) {
		force_back = l;
	}

	public int get_width() {
		if (!initialised)
			init();

		return width;
	}

	public double get_fwidth() {
		if (!initialised)
			init();

		return fwidth;
	}

	public synchronized void init() {
		int pwid;
		double len;

		if (initialised) {
			if (debug)
				System.out.println("Mountain.init called multiple times");
			return;
		} else {
			if (debug)
				System.out.println("Mountain.init called once");
		}
		/* the fractal width should be 1.0 */
		pwid = 1 + (1 << (levels - stop));
		width = 1 + (1 << levels);
		fwidth = mwidth * (double) width / (double) pwid;
		len = mwidth / (double) pwid;
		f = new Fold(this, levels, stop, len);
		f.sync();
		initialised = true;
	}

	public synchronized void clear() {
		if (!initialised)
			return;

		f.clear();
		f = null;
		initialised = false;
	}

	public double[] next_strip() {
		double[] s;
		if (!initialised) {
			init();
		}
		if (debug)
			System.out.println("Mountain.next_strip called");
		s = f.next_strip();
		return s;
	}

}
