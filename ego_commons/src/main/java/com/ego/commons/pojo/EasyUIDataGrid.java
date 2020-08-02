package com.ego.commons.pojo;

import java.util.List;

public class EasyUIDataGrid {

    private List<?> rows;
    private long total;
    public EasyUIDataGrid() {
    }

    public EasyUIDataGrid(List<?> rows, long total) {
        this.rows = rows;
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
