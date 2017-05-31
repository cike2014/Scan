package com.jms.scan.param;

import java.util.ArrayList;
import java.util.List;

public class OrderInfoParam {

	private String code;

	private Long date;

	private Integer type;

	private Integer uid;

	private String ccode;

	private List<DockInfo> dockInfos = new ArrayList<>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code=code;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public List<DockInfo> getDockInfos() {
		return dockInfos;
	}

	public void setDockInfos(List<DockInfo> dockInfos) {
		this.dockInfos = dockInfos;
	}

	public class DockInfo {
		private String code;
		private Long date;
		private List<StockInfo> stockInfos = new ArrayList<>();

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public Long getDate() {
			return date;
		}

		public void setDate(Long date) {
			this.date = date;
		}


		public List<StockInfo> getStockInfos() {
			return stockInfos;
		}

		public void setStockInfos(List<StockInfo> stockInfos) {
			this.stockInfos = stockInfos;
		}

	}

	public class StockInfo {
		private String code;

		private Integer num;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public Integer getNum() {
			return num;
		}

		public void setNum(Integer num) {
			this.num=num;
		}
	}

}
