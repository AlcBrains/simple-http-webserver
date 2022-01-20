package com.christopher.javaserver.handlers.employee;

import com.christopher.javaserver.domain.Employee;
import com.christopher.javaserver.handlers.AbstractHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


public class EmployeeHandler extends AbstractHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        HashMap<String, Object> params = parseRequestQuery(exchange);
        HashMap<String, Object> data = new HashMap<>();

        if (params.get("path").equals("/employees")) {
            //write a json
            try {
                ResultSet resultset = connector.executeQuery("select * from employees e limit 4");
                ArrayList<Employee> employees = new ArrayList<>();
                while (resultset.next()) {
                    Employee employee = new Employee();
                    employee.setId(Integer.parseInt(resultset.getString("emp_no")));
                    employee.setFirstName(resultset.getString("first_name"));
                    employee.setLastName(resultset.getString("last_name"));
                    employee.setGender(resultset.getString("gender"));
                    employee.setHireDate(LocalDate.parse(resultset.getString("hire_date")));
                    employee.setBirthDate(LocalDate.parse(resultset.getString("birth_date")));
                    employees.add(employee);
                }
                data.put("employees", employees);
            } catch (SQLException e) {
                data.put("error", e.getMessage());
                e.printStackTrace();
            }
            writeResponseBody(exchange, data);
        }

    }
}
