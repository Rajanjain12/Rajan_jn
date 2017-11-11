package com.kyobee.dto.common;

import javax.xml.bind.annotation.XmlRootElement;

import org.bouncycastle.jce.provider.JDKDSASigner.stdDSA;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class ScreensaverDTO {
private String screensaverFlag;
private String screensaverFile;
public String getScreensaverFlag() {
	return screensaverFlag;
}
public void setScreensaverFlag(String screensaverFlag) {
	this.screensaverFlag = screensaverFlag;
}
public String getScreensaverFile() {
	return screensaverFile;
}
public void setScreensaverFile(String screensaverFile) {
	this.screensaverFile = screensaverFile;
}
}
