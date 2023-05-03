package com.devtools.worker.tools.excel;

import java.text.SimpleDateFormat;

import com.devtools.worker.tools.excel.ExcelCellTypes.CellTypes;

public class ExcelCellColumn {

	private String columnName;
	private CellTypes cellType;
	private Object valueFormat;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public CellTypes getCellType() {
		return cellType;
	}

	public void setCellType(CellTypes cellType) {
		this.cellType = cellType;
		if (this.cellType == CellTypes.DATE || this.cellType == CellTypes.CALENDAR || this.cellType == CellTypes.TIMESTAMP) {
			throw new RuntimeException("(DATE, TIMESTAMP, CALENDAR) type must to use 'setCellType(CellTypes cellType, String format)'! ");
		}
		valueFormat = null;
	}

	public void setCellType(CellTypes cellType, String format) {
		this.cellType = cellType;
		if (this.cellType == CellTypes.DATE) {
			valueFormat = new SimpleDateFormat(format);
		} else if (this.cellType == CellTypes.CALENDAR) {
			valueFormat = new SimpleDateFormat(format);
		} else if (this.cellType == CellTypes.TIMESTAMP) {
			valueFormat = new SimpleDateFormat(format);
		} else {
			valueFormat = null;
		}
	}

	public Object getValueFormat() {
		return valueFormat;
	}
}
