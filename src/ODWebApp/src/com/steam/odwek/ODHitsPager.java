package com.steam.odwek;

import java.util.Vector;

import com.ibm.edms.od.ODHit;

public class ODHitsPager {
	private Vector hits = null;
	private int pageSize = 10;
	private int currentPage = 1;
	private int index = 0;

	public ODHitsPager(Vector hits) {
		this.hits = hits;
	}

	public boolean hasNext() {
		int spaceIndex = ((currentPage - 1) * pageSize) + index;
		return (index < pageSize) && ((spaceIndex) < hits.size());
	}

	public ODHit nextHit() {
		int spaceIndex = ((currentPage - 1) * pageSize) + index;
		index++;
		return (ODHit) hits.get(spaceIndex);
	}

	public int getPageTotal() {
		return (int) Math.ceil((float) hits.size() / pageSize);
	}

	public int getHitsTotal() {
		return hits.size();
	}

	public Vector getHits() {
		return hits;
	}

	public void setHits(Vector hits) {
		this.hits = hits;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		this.index = 0;
	}

	public boolean setCurrentPage(String currentPage) {
		try {
			setCurrentPage(Integer.parseInt(currentPage));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
