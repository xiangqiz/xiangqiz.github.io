package com;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class test{
	public static int FONT_SIZE=30;
	public static int WIDTH=80;
	public static int HEIGHT=30;
	
	public static Color getRandomColor(int fc, int bc) {// ������Χ��������ɫ
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public Map<String, BufferedImage> getImg() {
		int width = 60, height = 20;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// ��ȡͼ��������
		Graphics graphics = image.getGraphics();
		// �趨����ɫ
		graphics.setColor(getRandomColor(200, 250));
		graphics.fillRect(0, 0, width, height);
		// ���߿�
		graphics.setColor(new Color(0, 0, 0));
		graphics.drawRect(0, 0, width - 1, height - 1);
		// �趨����
		graphics.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		// ������������ߣ�ʹͼ���е���֤�벻�ױ���������̽�⵽
		graphics.setColor(getRandomColor(150, 200));
		// ���������
		Random random = new Random();
		for (int i = 0; i < 55; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			graphics.drawLine(x, y, x + xl, y + yl);
		}
		// ȡ�����������֤��(4λ����)
		graphics.setColor(getRandomColor(100, 150));
		graphics.setFont(new Font("Atlantic Inline", Font.PLAIN, 16));
		// String s = "0123456789"; // ���ñ�ѡ��֤��:����"0-9"
		String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // ���ñ�ѡ��֤��:����"a-z"������"0-9"
		// String s="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; //
		// ���ñ�ѡ��֤��:����"a-z"������"0-9"
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			String ch = String.valueOf(s.charAt(random.nextInt(s.length())));
			graphics.drawString(ch, i * 13 + 6, (random.nextInt(1) - 1) + 16);
			// valid_code += ch;
			sb.append(ch);
		}
		/*
		 * for (int i=0; i<4; i++){ code = String.valueOf(random.nextInt(10));
		 * //����֤����ʾ��ͼ���� graphics.drawString(code,i*13+6,16); valid_code += code;
		 * }
		 */
		// ����֤�����SESSION
		// ͼ����Ч
		graphics.dispose();

		Map<String, BufferedImage> map = new HashMap<String, BufferedImage>();
		map.put(sb.toString(), image);
		return map;
	}
}
