package name.martingeisse.trading_game.game.generate;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 *
 */
public final class StarNaming {

	private static final Random random = new Random();

	private final StringBuilder builder = new StringBuilder();

	static boolean decide(int percentage) {
		return random.nextInt(100) < percentage;
	}

	void add(char c) {
		builder.append(c);
	}

	void add(String s) {
		builder.append(s);
	}

	void addOneOf(String s) {
		builder.append(s.charAt(random.nextInt(s.length())));
	}

	void addOneOfStrings(String... strings) {
		builder.append(strings[random.nextInt(strings.length)]);
	}

	void addConsonant() {
		addOneOf("bcdfghjklmnpqrstvwxz");
	}

	void addVowel() {
		if (decide(20)) {
			addOneOfStrings("ai", "ei", "oi", "ui", "ou", "ay", "ey", "uy", "oy");
		} else {
			addOneOf("aeiouy");
			if (decide(10)) {
				repeatLast();
			}
		}
	}

	char getLast() {
		return (builder.length() == 0 ? ' ' : builder.charAt(builder.length() - 1));
	}

	void repeatLast() {
		if (getLast() != ' ') {
			add(getLast());
		}
	}

	void addLiquidConsonant() {
		add(decide(50) ? 'r' : 'l');
	}

	void addOptionalLiquidConsonant() {
		if (decide(30)) {
			addLiquidConsonant();
		}
	}

	void addOptionalStartConsonant() {
		if (decide(50)) {
			addConsonant();
			addOptionalLiquidConsonant();
		}
	}

	void addOptionalEndConsonant() {
		if (decide(50)) {
			addOptionalLiquidConsonant();
			addConsonant();
		}
	}

	void addMiddleConsonant() {
		addOptionalLiquidConsonant();
		addConsonant();
		addOptionalLiquidConsonant();
	}

	void capitalize() {
		if (builder.length() > 0) {
			builder.setCharAt(0, Character.toUpperCase(builder.charAt(0)));
		}
	}

	@Override
	public String toString() {
		return builder.toString();
	}

	/**
	 * Computes a random star name.
	 *
	 * @return the star name
	 */
	public static String compute() {
		StarNaming naming = new StarNaming();
		naming.addOptionalStartConsonant();
		naming.addVowel();
		int extraSyllables = random.nextInt(3);
		for (int i = 0; i < extraSyllables; i++) {
			naming.addMiddleConsonant();
			naming.addVowel();
		}
		naming.addOptionalEndConsonant();
		return capitalize(sanitize(naming.toString()));
	}

	private static String sanitize(String s) {
		s = StringUtils.replace(s, "xr", "x");
		s = StringUtils.replace(s, "rll", "rl");
		s = StringUtils.replace(s, "lr", "l");
		s = StringUtils.replace(s, "lll", "ll");
		s = StringUtils.replace(s, "lll", "ll");
		s = StringUtils.replace(s, "jl", "j");
		s = StringUtils.replace(s, "lj", "j");
		if (s.startsWith("rr")) {
			s = "r" + s.substring(2);
		}
		if (s.startsWith("rl")) {
			s = "r" + s.substring(2);
		}
		if (s.startsWith("lr")) {
			s = "l" + s.substring(2);
		}
		return s;
	}

	private static String capitalize(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

}
