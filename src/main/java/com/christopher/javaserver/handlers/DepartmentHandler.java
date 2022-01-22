package com.christopher.javaserver.handlers;

import com.christopher.javaserver.domain.Department;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DepartmentHandler extends AbstractHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HashMap<String, Object> params = parseRequestQuery(exchange);
        String path = (String) params.get("path");
        String method = (String) params.get("method");

        if (path.equals("/departments")) {
            writeResponseBody(exchange, getAllDepartments(), 200);
            //Get single Department
        } else if (path.contains("/departments/department/") && method.equals("GET")) {
            String deptNo = path.split("/")[path.split("/").length - 1];
            writeResponseBody(exchange, getDepartment(deptNo), 200);
            //Create a Department
        } else if (path.equals("/departments/department/create")) {
            writeResponseBody(exchange, List.of(createDepartment(params)), 201);
            //Delete Department
        } else if (path.contains("/departments/department/") && method.equals("DELETE")) {
            String deptNo = path.split("/")[path.split("/").length - 1];
            writeResponseBody(exchange, List.of(deleteDepartment(deptNo)), 204);
            //Update Department
        } else if (path.contains("/departments/department/") && method.equals("PATCH")) {
            String deptNo = path.split("/")[path.split("/").length - 1];
            params.put("deptNo", deptNo);
            writeResponseBody(exchange, List.of(updateDepartment(params)), 204);
        } else {
            writeResponseBody(exchange, Collections.emptyList(), 404);
        }

    }

    private List<Object> getAllDepartments() {
        ArrayList<Object> departments = new ArrayList<>();
        try {
            ResultSet resultset = connector.executeQuery("select * from departments d");
            while (resultset.next()) {
                Department department = new Department();
                getDepartmentData(resultset, department);
                departments.add(department);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    private List<Object> getDepartment(String deptNo) {
        ArrayList<Object> singletonList = new ArrayList<>();
        try {
            ResultSet resultset = connector.executeQuery("select * from departments d where dept_no = " + deptNo);
            Department department = new Department();
            while (resultset.next()) {
                getDepartmentData(resultset, department);
            }
            singletonList.add(department);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return singletonList;
    }

    private String createDepartment(HashMap<String, Object> data) {
        try {
            String deptNo = (String) data.get("deptNo");
            String deptName = (String) data.get("deptName");

            connector.executeQuery("Insert into departments values (" + deptNo + ", " + deptName + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Department Created Successfully";
    }

    private String updateDepartment(HashMap<String, Object> data) {
        try {
            String deptName = (String) data.get("deptName");
            String deptNo = (String) data.get("deptNo");
            connector.executeQuery("update departments set " +
                    " dept_name=" + deptName +
                    " where dept_no=" + deptNo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Department Updated Successfully";
    }

    private String deleteDepartment(String data) {
        try {
            connector.executeQuery("delete from departments where dept_no=" + data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Department Deleted Successfully";
    }

    private void getDepartmentData(ResultSet resultset, Department department) throws SQLException {
        department.setDeptNo(resultset.getString("dept_no"));
        department.setDeptName(resultset.getString("dept_name"));
    }
}
