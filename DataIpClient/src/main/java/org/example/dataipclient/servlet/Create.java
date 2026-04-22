package org.example.dataipclient.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.dataipclient.bean.UpdateBean;
import org.example.dataipclient.models.Addresses;
import org.example.dataipclient.models.Client;
import org.example.dataipclient.service.ClientService;
import org.example.dataipclient.validator.Validator;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "creater", value = "/create_servlet")
public class Create extends HttpServlet {

   @EJB
   ClientService clientService;

   @EJB
   private UpdateBean updateBean;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        checkAndCreate(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getBody(req, resp);
    }

    private void getBody(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<head><meta charset=\"UTF-8\"><title>Create Client</title></head>");
        out.println("<html><body>");
        out.println("<h2>Создание нового клиента</h2>");

        List<String> errors = (List<String>) request.getAttribute("errors");
        if (errors != null && !errors.isEmpty()) {
            out.println("<div style='color: red;'><ul>");
            for (String error : errors) {
                out.println("<li>" + error + "</li>");
            }
            out.println("</ul></div>");
        }

        out.println("<form action=\"create_servlet\" method=\"post\"><br>");
        out.println("<label>Наименование<input type=\"text\" name=\"clientName\"/></label><br><br>");
        out.println("<label>Тип клиента:");
        out.println("<select name='type' required>");
        out.println("<option value='Corporate'>Corporate</option>");
        out.println("<option value='Individual'>Individual</option>");
        out.println("</select></label><br><br>");

        out.println("<h4>Информация об адресе устройства</h4>");
        out.println("<label>IP-адрес: <input type='text' name='ip' placeholder='192.168.0.1' required></label><br><br>");
        out.println("<label>MAC-адрес: <input type='text' name='mac' placeholder='00:1A:2B:3C:4D:5E' required></label><br><br>");
        out.println("<label>Модель устройства: <input type='text' name='model' required></label><br><br>");
        out.println("<label>Адрес места нахождения: <input type='text' name='address' required></label><br><br>");

        out.println("<input type=\"submit\" value=\"CREATE\"/>");
        out.println("</form><br>");
        out.println("<br><a href='viewList'>Вернуться к списку клиентов</a>");
        out.println("</body></html>");
    }

    private void checkAndCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String clientName = request.getParameter("clientName");
        String type = request.getParameter("type");
        String ip = request.getParameter("ip");
        String mac = request.getParameter("mac");
        String model = request.getParameter("model");
        String location = request.getParameter("address");

        List<String> errors = new ArrayList<>();

        if (!Validator.isValidClientName(clientName)) {
            errors.add("Некорректное наименование клиента" /*+ clientName*/);
        }
        if (!Validator.isValidType(type)) {
            errors.add("Недопустимый тип клиента");
        }
        if (!Validator.isValidIP(ip)) {
            errors.add("Некорректный IP-адрес");
        }
        if (!Validator.isValidMAC(mac)) {
            errors.add("Некорректный MAC-адрес");
        }
        if (!Validator.isValidModel(model)) {
            errors.add("Некорректная модель устройства");
        }
        if (!Validator.isValidAddress(location)) {
            errors.add("Некорректный адрес места нахождения");
        }


        if (errors.isEmpty()) {
            Client client = new Client();
            client.setClientName(clientName);
            client.setType(type);
            client.setAdded(LocalDate.now());

            Addresses address = new Addresses();
            address.setIp(ip);
            address.setMac(mac);
            address.setModel(model);
            address.setAddress(location);
            address.setClient(client);

            client.getAddresses().add(address);
            // 1 этап
            //   clientService.create(client);

            //2 этап
                updateBean.createClient(client);

            response.sendRedirect("/" + ViewList.APP_URI + "/");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }
}