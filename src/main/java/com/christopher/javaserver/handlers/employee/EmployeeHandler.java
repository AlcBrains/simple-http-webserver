package com.christopher.javaserver.handlers.employee;

import com.christopher.javaserver.domain.Employee;
import com.christopher.javaserver.handlers.AbstractHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class EmployeeHandler extends AbstractHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        HashMap<String, Object> params = parseRequestQuery(exchange);
        String path = (String) params.get("path");
        String method = (String) params.get("method");


        //Get all employees
        if (path.equals("/employees")) {
            writeResponseBody(exchange, getAllEmployees(), 200);
            //Get single employee
        } else if (path.contains("/employees/employee/") && method.equals("GET")) {
            int empNo = Integer.parseInt(path.split("/")[path.split("/").length - 1]);
            writeResponseBody(exchange, getEmployee(Integer.toString(empNo)), 200);
            //Create an employee
        } else if (path.equals("/employees/employee/create")) {
            writeResponseBody(exchange, List.of(createEmployee(params)), 201);
            //Delete Employee
        } else if (path.contains("/employees/employee/") && method.equals("DELETE")) {
            int empNo = Integer.parseInt(path.split("/")[path.split("/").length - 1]);
            writeResponseBody(exchange, List.of(deleteEmployee(Integer.toString(empNo))), 204);
            //Update Employee
        } else if (path.contains("/employees/employee/") && method.equals("PATCH")) {
            Integer empNo = Integer.parseInt(path.split("/")[path.split("/").length - 1]);
            params.put("empNo", empNo);
            writeResponseBody(exchange, List.of(updateEmployee(params)), 204);
        } else {
            writeResponseBody(exchange, Collections.emptyList(), 404);
        }


    }

    private List<Object> getAllEmployees() {
        ArrayList<Object> employees = new ArrayList<>();
        try {
            ResultSet resultset = connector.executeQuery("select * from employees e limit 4");
            while (resultset.next()) {
                Employee employee = new Employee();
                getEmployeeData(resultset, employee);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    private List<Object> getEmployee(String empNo) {
        ArrayList<Object> singletonList = new ArrayList<>();
        try {
            ResultSet resultset = connector.executeQuery("select * from employees e where emp_no = " + empNo);
            Employee employee = new Employee();
            while (resultset.next()) {
                getEmployeeData(resultset, employee);
            }
            singletonList.add(employee);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return singletonList;
    }

    private String createEmployee(HashMap<String, Object> data) {
        try {
            //get highest id
            ResultSet rs = connector.executeQuery("SELECT emp_no from employees e order by emp_no desc limit 1");

            String birthDate = (String) data.get("birthDate");
            String lastName = (String) data.get("lastName");
            String firstName = (String) data.get("firstName");
            String hireDate = (String) data.get("hireDate");

            int newId = rs.getInt("emp_no") + 1;
            connector.executeQuery("Insert into employees values (" + newId +
                    "," + birthDate +
                    "," + firstName +
                    "," + lastName +
                    "," + hireDate + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Employee Created Successfully";
    }

    private String updateEmployee(HashMap<String, Object> data) {
        try {
            String birthDate = (String) data.get("birthDate");
            String firstName = (String) data.get("firstName");
            String lastName = (String) data.get("lastName");
            int empNo = Integer.parseInt((String) data.get("empNo"));
            connector.executeQuery("update employees set " +
                    "birth_date=" + birthDate +
                    " first_name=" + firstName +
                    " last_name=" + lastName +
                    " where emp_no=" + empNo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Employee Updated Successfully";
    }

    private String deleteEmployee(String data) {
        try {
            int empNo = Integer.parseInt(data);
            connector.executeQuery("delete from employees where emp_no=" + empNo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Employee Deleted Successfully";
    }

    private void getEmployeeData(ResultSet resultset, Employee employee) throws SQLException {
        employee.setId(resultset.getInt("emp_no"));
        employee.setFirstName(resultset.getString("first_name"));
        employee.setLastName(resultset.getString("last_name"));
        employee.setGender(resultset.getString("gender"));
        employee.setHireDate(LocalDate.parse(resultset.getString("hire_date")));
        employee.setBirthDate(LocalDate.parse(resultset.getString("birth_date")));
    }
}
