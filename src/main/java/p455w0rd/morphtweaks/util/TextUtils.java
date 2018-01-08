package p455w0rd.morphtweaks.util;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

/**
 * @author p455w0rd
 *
 */
public class TextUtils {

	public static List<TextFormatting> RAINBOW_COLORS = Lists.newArrayList(TextFormatting.AQUA, TextFormatting.YELLOW, TextFormatting.GOLD, TextFormatting.BLUE, TextFormatting.GREEN, TextFormatting.RED, TextFormatting.LIGHT_PURPLE);
	public static TextFormatting BOLD = TextFormatting.BOLD;
	public static int startIndex = 0;
	public static int ticks = 0;

	public static String rainbow(String text) {
		return rainbow(text, 1);
	}

	public static String rainbow(String text, int startPos) {
		StringBuilder sb = new StringBuilder(text.length() * 3);
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (startPos < 0 || startPos > RAINBOW_COLORS.size()) {
				startPos = 0;
			}
			sb.append(getColor(i, startPos).toString());
			sb.append(c);
		}
		return sb.toString();
	}

	public static TextFormatting getColor(int length, int startPos) {
		int offset = getOffset();
		int col = (length * startPos + RAINBOW_COLORS.size() - offset) % RAINBOW_COLORS.size();
		if (col > RAINBOW_COLORS.size()) {
			col = 0;
		}
		return RAINBOW_COLORS.get(col);
	}

	public static int getOffset() {
		return getOffset(120.0);
	}

	public static int getOffset(double delay) {
		return (int) Math.floor(Minecraft.getSystemTime() / delay) % RAINBOW_COLORS.size();
	}

}