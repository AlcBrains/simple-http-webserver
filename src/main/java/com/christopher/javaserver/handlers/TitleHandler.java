package com.christopher.javaserver.handlers;

import com.christopher.javaserver.domain.Title;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TitleHandler extends AbstractHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        HashMap<String, Object> params = parseRequestQuery(exchange);
        String path = (String) params.get("path");
        String method = (String) params.get("method");

        if (path.equals("/titles")) {
            writeResponseBody(exchange, getAllTitles(), 200);
            //Get single Title
        } else if (path.contains("/titles/title/") && method.equals("GET")) {
            String empNo = path.split("/")[path.split("/").length - 1];
            params.put("empNo", empNo);
            writeResponseBody(exchange, getTitle(params), 200);
            //Create a Title
        } else if (path.equals("/titles/title/create")) {
            writeResponseBody(exchange, List.of(createTitle(params)), 201);
            //Delete Title
        } else if (path.contains("/titles/title/") && method.equals("PATCH")) {
            String empNo = path.split("/")[path.split("/").length - 1];
            params.put("empNo", empNo);
            writeResponseBody(exchange, List.of(updateTitle(params)), 204);
        } else {
            writeResponseBody(exchange, Collections.emptyList(), 404);
        }

    }

    private List<Object> getAllTitles() {
        ArrayList<Object> titles = new ArrayList<>();
        try {
            ResultSet resultset = connector.executeQuery("select * from titles t");
            while (resultset.next()) {
                Title title = new Title();
                getTitleData(resultset, title);
                titles.add(title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connector.closeConnection();
        }
        return titles;
    }

    private List<Object> getTitle(HashMap<String, Object> params) {
        ArrayList<Object> singletonList = new ArrayList<>();
        try {
            String empNo = (String) params.get("empNo");
            String fromDate = (String) params.get("fromDate");
            String toDate = (String) params.get("toDate");
            ResultSet resultset = connector.executeQuery("select * from titles e where emp_no =" + empNo
                    + " and from_date= " + fromDate
                    + " and to_date=" + toDate);
            Title title = new Title();
            while (resultset.next()) {
                getTitleData(resultset, title);
            }
            singletonList.add(title);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connector.closeConnection();
        }
        return singletonList;
    }

    private String createTitle(HashMap<String, Object> data) {

        String empNo = (String) data.get("empNo");
        String title = (String) data.get("title");
        String fromDate = (String) data.get("fromDate");
        String toDate = (String) data.get("toDate");

        connector.executeQuery("Insert into titles values (" + empNo + ", " + title + ", " + fromDate + ", " + toDate + ")");
        connector.closeConnection();
        return "Title Created Successfully";
    }

    private String updateTitle(HashMap<String, Object> data) {

        int title = (int) data.get("title");
        String empNo = (String) data.get("empNo");
        String fromDate = (String) data.get("fromDate");
        String toDate = (String) data.get("toDate");
        connector.executeQuery("update titles set " +
                " title=" + title +
                " where emp_no=" + empNo +
                " and from_date=" + fromDate +
                " and to_date=" + toDate);
        connector.closeConnection();
        return "Title Updated Successfully";
    }

    private String deleteTitle(HashMap<String, Object> data) {
        String empNo = (String) data.get("empNo");
        String fromDate = (String) data.get("fromDate");
        String toDate = (String) data.get("toDate");
        connector.executeQuery("delete from titles where emp_no=" + empNo + " and from_date=" + fromDate + " and to_date=" + toDate);
        connector.closeConnection();
        return "Title Deleted Successfully";
    }

    private void getTitleData(ResultSet resultset, Title title) throws SQLException {
        title.setEmpNo(resultset.getInt("emp_no"));
        title.setTitle(resultset.getString("title"));
        title.setFromDate(LocalDate.parse(resultset.getString("from_date")));
        title.setToDate(LocalDate.parse(resultset.getString("to_date")));
    }
}
