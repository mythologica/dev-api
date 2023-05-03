package com.devtools.worker.tools.excel;

import java.util.Map;

public class ExcelUploadVO {

	private int startRow;
	private int batchCount;

	private String dBHandler;
	private Map<String, ExcelCellColumn> excelColumns;

	public int getStartRow() {

		return startRow;
	}

	public void setStartRow(int startRow) {

		this.startRow = startRow;
	}

	public int getBatchCount() {

		return batchCount;
	}

	public void setBatchCount(int batchCount) {

		this.batchCount = batchCount;
	}

	public String getdBHandler() {

		return dBHandler;
	}

	public void setdBHandler(String dBHandler) {

		this.dBHandler = dBHandler;
	}

	public Map<String, ExcelCellColumn> getExcelColumns() {

		return excelColumns;
	}

	public void setExcelHeader(Map<String, ExcelCellColumn> excelColumns) {

		this.excelColumns = excelColumns;
	}
}
