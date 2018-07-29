public class JavaTest {
	public static void main(String[] args) {
		int availProcessors = Runtime.getRuntime().availableProcessors();
		System.out.println("avail processors count: " + availProcessors);
		for (int i = 0; i < availProcessors; i++) {
			new Thread() {
				@Override
				public void run() {
					long start = System.currentTimeMillis();
					long ent = start + 6000;
					long index = 0;
					while (true) {
						Math.sqrt(index);
						long now = System.currentTimeMillis();
						if (now > ent) {
							break;
						}
						index++;
					}
					System.out.println(Thread.currentThread().getName() + " => " + index / 1e7);
				}
			}.start();
		}
	}
}