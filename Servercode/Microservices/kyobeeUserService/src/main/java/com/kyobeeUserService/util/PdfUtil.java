package com.kyobeeUserService.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.kyobeeUserService.dto.OrganizationDTO;
import com.kyobeeUserService.dto.TableRowSettingDTO;
import com.kyobeeUserService.entity.PlanFeatureCharge;

@Component
public class PdfUtil {

	@Autowired
	AWSUtil awsUtil;

	public String generateInvoice(OrganizationDTO orgDTO, List<PlanFeatureCharge> plan, Integer orgSubscriptionId)
			throws DocumentException, IOException, ParseException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, out);
		document.open();

		TableRowSettingDTO settingDTO = null;

		// Adding logo
		Image img = Image.getInstance(UserServiceConstants.IMG_PATH);
		img.scaleAbsolute(140, 70);
		document.add(img);

		// font to be used in whole file
		BaseFont baseFont = BaseFont.createFont(UserServiceConstants.FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

		// Adding Seller's address and invoice details
		Paragraph par = new Paragraph(UserServiceConstants.INVOICE,
				new Font(baseFont, 18, Font.BOLD, new BaseColor(242, 7, 7)));
		par.setAlignment(Element.ALIGN_RIGHT);
		document.add(par);

		par = new Paragraph(UserServiceConstants.ADD_LINE1, new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK));
		par.setAlignment(Element.ALIGN_LEFT);
		document.add(par);

		Chunk glue = new Chunk(new VerticalPositionMark()); // acts like a separator that separates two (or more) other
															// Chunk objects
		par = new Paragraph(UserServiceConstants.ADD_LINE2, new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK));
		par.add(new Chunk(glue));
		par.add(UserServiceConstants.INVOICE_NO + orgSubscriptionId);
		document.add(par);

		par = new Paragraph(UserServiceConstants.ADD_LINE3, new Font(baseFont, 14, Font.NORMAL, BaseColor.BLACK));
		par.add(new Chunk(glue));
		par.add(UserServiceConstants.INVOICE_DATE + CommonUtil.getDateWithFormat(UserServiceConstants.DATE_FORMAT));
		document.add(par);

		// Adding Buyer's Address
		PdfPTable table1 = new PdfPTable(2);
		table1.setHorizontalAlignment(Element.ALIGN_LEFT);
		table1.setWidthPercentage(100);
		table1.setSpacingBefore(20f);
		table1.setSpacingAfter(20f);

		settingDTO = new TableRowSettingDTO();
		settingDTO.setBaseFont(baseFont);
		settingDTO.setBgColor(new BaseColor(255, 204, 204));
		settingDTO.setFontColor(BaseColor.BLACK);
		settingDTO.setFontSize(12);
		settingDTO.setLeftRightSize(15f);
		settingDTO.setTopBottomSize(0);
		settingDTO.setFontStyle(Font.NORMAL);

		addRow(table1, " ", Element.ALIGN_LEFT, settingDTO);
		addRow(table1, " ", Element.ALIGN_LEFT, settingDTO);
		addRow(table1, "Bill To", Element.ALIGN_LEFT, settingDTO);
		addRow(table1, "", Element.ALIGN_LEFT, settingDTO);
		addRow(table1, orgDTO.getOrganizationName(), Element.ALIGN_LEFT, settingDTO);
		addRow(table1, orgDTO.getAddressDTO().getAddressLineOne(), Element.ALIGN_RIGHT, settingDTO);
		addRow(table1, "Branch " + orgDTO.getAddressDTO().getCity(), Element.ALIGN_LEFT, settingDTO);
		addRow(table1, orgDTO.getAddressDTO().getCity() + "," + orgDTO.getAddressDTO().getState() + ","
				+ orgDTO.getAddressDTO().getZipcode(), Element.ALIGN_RIGHT, settingDTO);
		addRow(table1, " ", Element.ALIGN_LEFT, settingDTO);
		addRow(table1, " ", Element.ALIGN_LEFT, settingDTO);

		document.add(table1);

		// Invoice details table
		PdfPTable table = new PdfPTable(3);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidthPercentage(100);

		settingDTO = new TableRowSettingDTO();
		settingDTO.setBaseFont(baseFont);
		settingDTO.setBgColor(new BaseColor(242, 7, 7));
		settingDTO.setFontColor(BaseColor.WHITE);
		settingDTO.setFontSize(14);
		settingDTO.setLeftRightSize(5f);
		settingDTO.setTopBottomSize(5f);
		settingDTO.setFontStyle(Font.NORMAL);

		// setting header for invoice table
		List<String> headerList = UserServiceConstants.HEADER_lIST;
		for (String header : headerList) {
			addRow(table, header, Element.ALIGN_CENTER, settingDTO);
		}

		// creating invoice table row
		IntStream.range(0, plan.size()).forEach(i -> {

			TableRowSettingDTO rowsettingDTO = new TableRowSettingDTO();
			rowsettingDTO.setBaseFont(baseFont);
			rowsettingDTO.setFontColor(BaseColor.BLACK);
			rowsettingDTO.setFontSize(12);
			rowsettingDTO.setLeftRightSize(5f);
			rowsettingDTO.setTopBottomSize(5f);
			rowsettingDTO.setFontStyle(Font.NORMAL);
			if (i % 2 == 0) {
				rowsettingDTO.setBgColor(BaseColor.WHITE);
				addRow(table,
						plan.get(i).getFeature().getFeatureName() + "-" + plan.get(i).getPlan().getPlanName() + "("
								+ plan.get(i).getPlanterm().getPlanTermName() + ")",
						Element.ALIGN_CENTER, rowsettingDTO);
				addRow(table, "1", Element.ALIGN_CENTER, rowsettingDTO);
				addRow(table, UserServiceConstants.CURRENCY + plan.get(i).getTermChargeAmt(), Element.ALIGN_CENTER,
						rowsettingDTO);

			} else {
				rowsettingDTO.setBgColor(new BaseColor(239, 239, 239));
				addRow(table,
						plan.get(i).getFeature().getFeatureName() + "-" + plan.get(i).getPlan().getPlanName() + "("
								+ plan.get(i).getPlanterm().getPlanTermName() + ")",
						Element.ALIGN_CENTER, rowsettingDTO);
				addRow(table, "1", Element.ALIGN_CENTER, rowsettingDTO);
				addRow(table, UserServiceConstants.CURRENCY + plan.get(i).getTermChargeAmt(), Element.ALIGN_CENTER,
						rowsettingDTO);
			}
		});

		document.add(table);

		BigDecimal subTotal = plan.get(0).getTermChargeAmt().add(plan.get(1).getTermChargeAmt());
		BigDecimal tax = UserServiceConstants.TAX_AMT;
		BigDecimal total = subTotal.add(tax);

		PdfPTable table2 = new PdfPTable(3);
		table2.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table2.setSpacingBefore(20f);
		table2.setSpacingAfter(20f);

		settingDTO = new TableRowSettingDTO();
		settingDTO.setBaseFont(baseFont);
		settingDTO.setFontColor(BaseColor.RED);
		settingDTO.setFontSize(12);
		settingDTO.setLeftRightSize(5f);
		settingDTO.setTopBottomSize(5f);
		settingDTO.setFontStyle(Font.BOLD);

		addRow(table2, " ", Element.ALIGN_CENTER, settingDTO);
		addRow(table2, UserServiceConstants.SUBTOTAL, Element.ALIGN_RIGHT, settingDTO);
		settingDTO.setFontColor(BaseColor.BLACK);
		addRow(table2, UserServiceConstants.CURRENCY + subTotal, Element.ALIGN_CENTER, settingDTO);
		addRow(table2, " ", Element.ALIGN_CENTER, settingDTO);
		settingDTO.setFontColor(BaseColor.RED);
		addRow(table2, UserServiceConstants.TAX, Element.ALIGN_RIGHT, settingDTO);
		settingDTO.setFontColor(BaseColor.BLACK);
		addRow(table2, UserServiceConstants.CURRENCY + tax, Element.ALIGN_CENTER, settingDTO);
		addRow(table2, "", Element.ALIGN_CENTER, settingDTO);
		settingDTO.setFontColor(BaseColor.RED);
		addRow(table2, UserServiceConstants.TOTAL, Element.ALIGN_RIGHT, settingDTO);
		settingDTO.setFontColor(BaseColor.BLACK);
		addRow(table2, UserServiceConstants.CURRENCY + total, Element.ALIGN_CENTER, settingDTO);

		document.add(table2);

		document.close();

		ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());

		return awsUtil.uploadInvoice(inputStream, orgSubscriptionId + UserServiceConstants.FILE_EXTENSION);
	}

	public void addRow(PdfPTable pdftable, String phrase, Integer align, TableRowSettingDTO settingDTO) {

		PdfPCell cell = new PdfPCell();
		cell.setHorizontalAlignment(align);

		cell.setBackgroundColor(settingDTO.getBgColor());
		cell.setBorder(0);
		cell.setPaddingLeft(settingDTO.getLeftRightSize());
		cell.setPaddingRight(settingDTO.getLeftRightSize());
		if (settingDTO.getTopBottomSize() != 0) {
			cell.setPaddingTop(settingDTO.getTopBottomSize());
			cell.setPaddingBottom(settingDTO.getTopBottomSize());
		}
		cell.setPhrase(new Phrase(phrase, new Font(settingDTO.getBaseFont(), settingDTO.getFontSize(),
				settingDTO.getFontStyle(), settingDTO.getFontColor())));

		pdftable.addCell(cell);
	}

}
