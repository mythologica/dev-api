package com.devtools.worker.tools.excel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ExcelUploadSAXHandler extends DefaultHandler {
	enum xssfDataType {
		BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER,
	}

	/**
	 * Table with styles
	 */
	private StylesTable stylesTable;

	private SharedStringsTable sharedStringsTable;
	private int minColumnCount;

	private boolean vIsOpen;

	private xssfDataType nextDataType;

	private short formatIndex;
	private String formatString;
	private DataFormatter formatter;

	private int thisColumn = -1;
	// The last column printed to the output stream
	private int lastColumnNumber = -1;

	// Gathers characters as they are seen.
	private StringBuffer value;
	private String[] record;
	private List<String[]> rows = new ArrayList<String[]>();
	private boolean isCellNull = false;
	private SimpleDateFormat sdf = null;
	private static DecimalFormat df = new DecimalFormat("###########");


	public ExcelUploadSAXHandler(StylesTable styles,
                                  SharedStringsTable strings, int cols) {
            this.stylesTable = styles;
            this.sharedStringsTable = strings;
            this.minColumnCount = cols;
            this.value = new StringBuffer();
            this.nextDataType = xssfDataType.NUMBER;
            this.formatter = new DataFormatter();
            record = new String[this.minColumnCount];
            rows.clear();
        }

	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

		if ("inlineStr".equals(name) || "v".equals(name)) {
			vIsOpen = true;
			value.setLength(0);
		}
		// c => cell
		else if ("c".equals(name)) {
			// Get the cell reference
			String r = attributes.getValue("r");
			int firstDigit = -1;
			for (int c = 0; c < r.length(); ++c) {
				if (Character.isDigit(r.charAt(c))) {
					firstDigit = c;
					break;
				}
			}
			thisColumn = nameToColumn(r.substring(0, firstDigit));

			// Set up defaults.
			this.nextDataType = xssfDataType.NUMBER;
			this.formatIndex = -1;
			this.formatString = null;
			String cellType = attributes.getValue("t");
			String cellStyleStr = attributes.getValue("s");
			if ("b".equals(cellType))
				nextDataType = xssfDataType.BOOL;
			else if ("e".equals(cellType))
				nextDataType = xssfDataType.ERROR;
			else if ("inlineStr".equals(cellType))
				nextDataType = xssfDataType.INLINESTR;
			else if ("s".equals(cellType))
				nextDataType = xssfDataType.SSTINDEX;
			else if ("str".equals(cellType))
				nextDataType = xssfDataType.FORMULA;
			else if (cellStyleStr != null) {
				// It's a number, but almost certainly one
				// with a special style or format
				int styleIndex = Integer.parseInt(cellStyleStr);
				XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
				this.formatIndex = style.getDataFormat();
				this.formatString = style.getDataFormatString();
				if (this.formatString == null)
					this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
			}
		}

	}

	public void endElement(String uri, String localName, String name) throws SAXException {

		String thisStr = null;

		// v => contents of a cell
		if ("v".equals(name)) {
			// Process the value contents as required.
			// Do now, as characters() may be called more than once
			switch (nextDataType) {

			case BOOL:
				char first = value.charAt(0);
				thisStr = first == '0' ? "FALSE" : "TRUE";
				break;

			case ERROR:
				thisStr = "\"ERROR:" + value.toString() + '"';
				break;

			case FORMULA:
				// A formula could result in a string value,
				// so always add double-quote characters.
				thisStr = value.toString();
				break;

			case INLINESTR:
				// TODO: have seen an example of this, so it's untested.
				XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
				thisStr = rtsi.toString();
				break;

			case SSTINDEX:
				String sstIndex = value.toString();
				try {
					int idx = Integer.parseInt(sstIndex);
					
					RichTextString txt = sharedStringsTable.getItemAt(idx);
					
					XSSFRichTextString rtss = new XSSFRichTextString(txt.toString());
					thisStr = rtss.toString();
				} catch (NumberFormatException ex) {
					System.out.println("Failed to parse SST index '" + sstIndex + "': " + ex.toString());
				}
				break;

			case NUMBER:
				String n = value.toString();
				if (formatIndex == 14 || formatIndex == 31 || formatIndex == 57 || formatIndex == 58
						|| (176 <= formatIndex && formatIndex <= 178) || (182 <= formatIndex && formatIndex <= 196)
						|| (210 <= formatIndex && formatIndex <= 213) || (208 == formatIndex)) {
					sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(n));
					thisStr = sdf.format(date);
				} else if (formatIndex == 20 || formatIndex == 32 || formatIndex == 183
						|| (200 <= formatIndex && formatIndex <= 209)) {// 时间
					sdf = new SimpleDateFormat("HH:mm");
					Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(n));
					thisStr = sdf.format(date);
				} else {
					if (n.contains("E")) {
						String[] split = n.split("\\+");
						String e = split[0].replaceAll("E|e", "");
						thisStr = e.replace(".", "");
					} else {
						thisStr = n;
					}
				}
				break;
			default:
				thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
				break;
			}
			if (lastColumnNumber == -1) {
				lastColumnNumber = 0;
			}
			if (thisStr == null || "".equals(isCellNull)) {
				isCellNull = true;
			}
			record[thisColumn] = thisStr;
			if (thisColumn > -1)
				lastColumnNumber = thisColumn;
		} else if ("row".equals(name)) {
			// Print out any missing commas if needed
			if (minColumnCount > 0) {
				// Columns are 0 based
				if (lastColumnNumber == -1) {
					lastColumnNumber = 0;
				}
				if (record != null && record.length != 0) {
					rows.add(record.clone());
					isCellNull = false;
					for (int i = 0; i < record.length; i++) {
						record[i] = null;
					}
				}
			}
			lastColumnNumber = -1;
		}

	}

	public List<String[]> getRows() {
		return rows;
	}

	public void setRows(List<String[]> rows) {
		this.rows = rows;
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		if (vIsOpen)
			value.append(ch, start, length);
	}

	private int nameToColumn(String name) {
		int column = -1;
		for (int i = 0; i < name.length(); ++i) {
			int c = name.charAt(i);
			column = (column + 1) * 26 + c - 'A';
		}
		return column;
	}
}