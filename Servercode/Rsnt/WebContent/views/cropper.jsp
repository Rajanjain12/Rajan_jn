<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Insert title here</title>
</head>
<body>

</body>
</html>



<%@ page import="java.awt.image.BufferedImage"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.IOException"%>
<%@ page import="javax.imageio.ImageIO"%>
<%!public String cropImage(int x, int y, int w, int h) {

		String absolutePath = "C:/apache-tomcat-5.5.26/webapps/JavaXp/";

		try {
			BufferedImage originalImgage = ImageIO.read(new File(absolutePath+"pool.jpg"));
			System.out.println("Original image dimension: "+originalImgage.getWidth()+"x"+originalImgage.getHeight());

			BufferedImage SubImgage = originalImgage.getSubimage(x, y, w, h);
			System.out.println("Cropped image dimension: "+SubImgage.getWidth()+"x"+SubImgage.getHeight());

			File outputfile = new File(absolutePath + "croppedImage.jpg");
			ImageIO.write(SubImgage, "jpg", outputfile);
			
			System.out.println("Image cropped successfully: "+outputfile.getPath());
			
			return outputfile.getName();

		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}%>
<%
	int x = Integer.parseInt(request.getParameter("x"));
	int y = Integer.parseInt(request.getParameter("y"));
	int w = Integer.parseInt(request.getParameter("w"));
	int h = Integer.parseInt(request.getParameter("h"));

	String croppedImage = cropImage(x, y, w, h);
%>
Cropped image : <%= croppedImage %>
<br />
<img src="<%= croppedImage %>" />