package org.example.dataipclient.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dataipclient.bean.UpdateBean;
import org.example.dataipclient.service.ClientService;

import java.io.IOException;

@WebServlet(name = "delete", value = "/delete_servlet")
public class Delete extends HttpServlet {

    @EJB
    ClientService clientService;
    @EJB
    private UpdateBean updateBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      //  Integer clientId = Integer.parseInt(request.getParameter("clientId"));
       // System.out.println("Delete client id: " + clientId);
       // clientService.delete(clientId);

        String clientId = request.getParameter("clientId");
        if (clientId != null && !clientId.trim().isEmpty()) {
            Integer id = Integer.parseInt(clientId);
            // 1 этап
            //clientService.delete(id);
            // 2 этап
            updateBean.delete(id);
        }
        response.sendRedirect("viewList");
    }
}
