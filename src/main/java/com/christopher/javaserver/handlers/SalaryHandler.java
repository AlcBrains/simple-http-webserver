package com.christopher.javaserver.handlers;

import com.christopher.javaserver.domain.Salary;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SalaryHandler extends AbstractHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        HashMap<String, Object> params = parseRequestQuery(exchange);
        String path = (String) params.get("path");
        String method = (String) params.get("method");

        if (path.equals("/salaries")) {
            writeResponseBody(exchange, getAllSalaries(), 200);
            //Get single Salary
        } else if (path.contains("/salaries/salary/") && method.equals("GET")) {
            String empNo = path.split("/")[path.split("/").length - 1];
            params.put("empNo", empNo);
            writeResponseBody(exchange, getSalary(params), 200);
            //Create a Salary
        } else if (path.equals("/salaries/salary/create")) {
            writeResponseBody(exchange, List.of(createSalary(params)), 201);
            //Delete Salary
        } else if (path.contains("/salaries/salary/") && method.equals("PATCH")) {
            String empNo = path.split("/")[path.split("/").length - 1];
            params.put("empNo", empNo);
            writeResponseBody(exchange, List.of(updateSalary(params)), 204);
        } else {
            writeResponseBody(exchange, Collections.emptyList(), 404);
        }

    }

    private List<Object> getAllSalaries() {
        ArrayList<Object> salaries = new ArrayList<>();
        try {
            ResultSet resultset = connector.executeQuery("select * from salaries s");
            while (resultset.next()) {
                Salary salary = new Salary();
                getSalaryData(resultset, salary);
                salaries.add(salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connector.closeConnection();
        }
        return salaries;
    }

    private List<Object> getSalary(HashMap<String, Object> params) {
        ArrayList<Object> singletonList = new ArrayList<>();
        try {
            String empNo = (String) params.get("empNo");
            String fromDate = (String) params.get("fromDate");
            ResultSet resultset = connector.executeQuery("select * from salaries e where emp_no =" + empNo + " and from_date= " + fromDate);
            Salary salary = new Salary();
            while (resultset.next()) {
                getSalaryData(resultset, salary);
            }
            singletonList.add(salary);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connector.closeConnection();
        }
        return singletonList;
    }

    private String createSalary(HashMap<String, Object> data) {


        String empNo = (String) data.get("empNo");
        String salary = (String) data.get("salary");
        String fromDate = (String) data.get("fromDate");
        String toDate = (String) data.get("toDate");

        connector.executeQuery("Insert into salaries values (" + empNo + ", " + salary + ", " + fromDate + ", " + toDate + ")");

        return "Salary Created Successfully";
    }

    private String updateSalary(HashMap<String, Object> data) {

        int salary = (int) data.get("salary");
        String empNo = (String) data.get("empNo");
        String fromDate = (String) data.get("fromDate");
        connector.executeQuery("update salaries set " +
                " salary=" + salary +
                " where emp_no=" + empNo +
                " and from_date=" + fromDate);
        connector.closeConnection();
        return "Salary Updated Successfully";
    }

    private String deleteSalary(HashMap<String, Object> data) {

        String empNo = (String) data.get("empNo");
        String fromDate = (String) data.get("fromDate");
        connector.executeQuery("delete from salaries where emp_no=" + empNo + " and from_date=" + fromDate);
        connector.closeConnection();
        return "Salary Deleted Successfully";
    }

    private void getSalaryData(ResultSet resultset, Salary salary) throws SQLException {
        salary.setEmpNo(resultset.getInt("emp_no"));
        salary.setSalary(resultset.getInt("salary"));
        salary.setFromDate(LocalDate.parse(resultset.getString("from_date")));
        salary.setToDate(LocalDate.parse(resultset.getString("to_date")));
    }
}
