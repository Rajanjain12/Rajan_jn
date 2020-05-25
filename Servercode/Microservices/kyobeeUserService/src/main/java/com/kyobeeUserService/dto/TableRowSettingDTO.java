package com.kyobeeUserService.dto;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;

public class TableRowSettingDTO {

	private float leftRightSize;
	private float topBottomSize;
	private BaseColor bgColor;
	private BaseColor fontColor;
	private BaseFont baseFont;
	private float fontSize;
	private Integer fontStyle;

	public float getLeftRightSize() {
		return leftRightSize;
	}

	public void setLeftRightSize(float leftRightSize) {
		this.leftRightSize = leftRightSize;
	}

	public float getTopBottomSize() {
		return topBottomSize;
	}

	public void setTopBottomSize(float topBottomSize) {
		this.topBottomSize = topBottomSize;
	}

	public BaseColor getBgColor() {
		return bgColor;
	}

	public void setBgColor(BaseColor bgColor) {
		this.bgColor = bgColor;
	}

	public BaseColor getFontColor() {
		return fontColor;
	}

	public void setFontColor(BaseColor fontColor) {
		this.fontColor = fontColor;
	}

	public BaseFont getBaseFont() {
		return baseFont;
	}

	public void setBaseFont(BaseFont baseFont) {
		this.baseFont = baseFont;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public Integer getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(Integer fontStyle) {
		this.fontStyle = fontStyle;
	}

}
