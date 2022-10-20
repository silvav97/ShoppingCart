package com.finalproject.service;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        response.getWriter().append("Full Name: " + firstName + " " + lastName);
    }
}
