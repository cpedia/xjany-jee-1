package com.lti.action.verifycode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.opensymphony.xwork2.ActionSupport;
import com.lti.action.Action;

public class GenerateAction extends ActionSupport implements ServletResponseAware, ServletRequestAware, Action {

	private static final long serialVersionUID = 1L;

	private HttpServletRequest req;

	private HttpSession session;

	private HttpServletResponse resp;

	private int width = 90;
	private int height = 40;
	private int codeCount = 4;
	private int x = 16;

	private int fontHeight = 28;
	private int codeY = 30;
	char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public String execute() throws Exception {
		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();

		Random random = new Random();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		g.setFont(font);

		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);

		g.setColor(Color.GRAY);
		for (int i = 0; i < 1; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;

		for (int i = 0; i < codeCount; i++) {
			String strRand = String.valueOf(codeSequence[random.nextInt(36)]);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);

			g.setColor(new Color(red, green, blue));
			g.drawString(strRand, (i + 1) * x, codeY);

			randomCode.append(strRand);
		}
		HttpSession session = req.getSession();
		session.setAttribute("validateCode", randomCode.toString());

		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);

		resp.setContentType("image/jpeg");

		ServletOutputStream sos = resp.getOutputStream();
		ImageIO.write(buffImg, "jpeg", sos);
		sos.close();
		return Action.SUCCESS;
	}

	public HttpServletRequest getReq() {
		return req;
	}

	public void setReq(HttpServletRequest request) {
		this.req = request;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.req = request;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getCodeCount() {
		return codeCount;
	}

	public void setCodeCount(int codeCount) {
		this.codeCount = codeCount;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getFontHeight() {
		return fontHeight;
	}

	public void setFontHeight(int fontHeight) {
		this.fontHeight = fontHeight;
	}

	public int getCodeY() {
		return codeY;
	}

	public void setCodeY(int codeY) {
		this.codeY = codeY;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		this.resp = arg0;
	}

}
