package org.example.dataipclient.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dataipclient.bean.UpdateBean;
import org.example.dataipclient.models.Addresses;
import org.example.dataipclient.models.Client;
import org.example.dataipclient.service.ClientService;
import org.example.dataipclient.validator.Validator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "update", value = "/update_servlet")
public class Update extends HttpServlet {

    @EJB
    ClientService clientService;
    @EJB
    private UpdateBean updateBean;

    //static int addressNumber = 0;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        update(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getBody(request, response);
    }

    private void getBody(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String clientId = request.getParameter("clientId");

        String action = request.getParameter("action");
        String currentAddressIndexStr = request.getParameter("addressIndex");

        if (clientId != null && !clientId.trim().isEmpty()) {
            Integer id = Integer.parseInt(clientId);
            Client client = clientService.findById(id);
            if (client == null) {
                response.sendRedirect("viewList");
                return;
            }

            if (client != null && client.getAddresses() != null) {
                List<Addresses> addresses = client.getAddresses();

                int currentAddressIndex = 0;
                if (currentAddressIndexStr != null) {
                    currentAddressIndex = Integer.parseInt(currentAddressIndexStr);
                }
                if ("prev".equals(action) && currentAddressIndex > 0) {
                    currentAddressIndex--;
                } else if ("next".equals(action) && currentAddressIndex < addresses.size() - 1) {
                    currentAddressIndex++;
                }

                PrintWriter out = response.getWriter();
                out.println("<head><meta charset=\"UTF-8\"><title>Create Client</title></head>");
                out.println("<html><body>");
                out.println("<h2>Модификация клиента</h2>");

                List<String> errors = (List<String>) request.getAttribute("errors");
                if (errors != null && !errors.isEmpty()) {
                    out.println("<div style='color: red;'><ul>");
                    for (String error : errors) {
                        out.println("<li>" + error + "</li>");
                    }
                    out.println("</ul></div>");
                }

                out.println("<form action=\"update_servlet\" method=\"post\"><br>");

                out.println("<input type='hidden' name='clientId' value='" + client.getClientId() + "'>");
                out.println("<input type='hidden' name='addressIndex' value='" + currentAddressIndex + "'>");

                out.println("<label>Наименование клиента: ");
                out.println("<input type='text' name='clientName' value='" + client.getClientName() + "' required></label><br><br>");

                out.println("<label>Тип клиента:");
                out.println("<select name='type' required>");
                out.println("<option value='Corporate'" + ("Corporate".equals(client.getType()) ? " selected" : "") + ">Corporate</option>");
                out.println("<option value='Individual'" + ("Individual".equals(client.getType()) ? " selected" : "") + ">Individual</option>");
                out.println("</select></label><br><br>");

                //System.out.println(addresses.size());
                out.println("<h3>Информация об адресе устройства (адрес " + (currentAddressIndex + 1) + " из " + addresses.size() + ")</h3>");
                out.println("<label>IP-адрес: ");
                out.println("<input type='text' name='ip' value='" + addresses.get(currentAddressIndex).getIp() + "' placeholder='192.168.0.1' required></label><br><br>");
                out.println("<label>MAC-адрес: ");
                out.println("<input type='text' name='mac' value='" + addresses.get(currentAddressIndex).getMac() + "' placeholder='00-1A-2B-3C-4D-5E' required></label><br><br>");
                out.println("<label>Модель устройства: ");
                out.println("<input type='text' name='model' value='" + addresses.get(currentAddressIndex).getModel() + "' required></label><br><br>");
                out.println("<label>Адрес места нахождения: ");
                out.println("<input type='text' name='address' value='" + addresses.get(currentAddressIndex).getAddress() + "' required></label><br><br>");

                out.println("<input type=\"submit\" value=\"UPDATE\"/>");
                out.println("</form><br>");

                out.println("<form method='get' action='update_servlet' style='margin: 20px 0;'>");
                out.println("<input type='hidden' name='clientId' value='" + client.getClientId() + "'>");
                out.println("<input type='hidden' name='addressIndex' value='" + currentAddressIndex + "'>");
                out.println("<button type='submit' name='action' value='prev' " + (currentAddressIndex <= 0 ? "disabled " : "") +
                        ">Предыдущий</button>");
                //addressNumber--;
                out.println("<button type='submit' name='action' value='next' " + (currentAddressIndex >= addresses.size() - 1 ? "disabled " : "")  +
                        ">Следующий</button>");
                out.println("</form>");


                out.println("<br><a href='viewList'>Вернуться к списку клиентов</a>");
                out.println("</body></html>");
            }
        }
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        Integer clientId = Integer.parseInt(request.getParameter("clientId"));
        String clientName = request.getParameter("clientName");
        String type = request.getParameter("type");
        String ip = request.getParameter("ip");
        String mac = request.getParameter("mac");
        String model = request.getParameter("model");
        String address = request.getParameter("address");
        int currentAddressIndexStr = Integer.parseInt(request.getParameter("addressIndex"));

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
        if (!Validator.isValidAddress(address)) {
            errors.add("Некорректный адрес места нахождения");
        }

        if (errors.isEmpty()) {
            Client client = clientService.findById(clientId);
            if (client != null) {
                client.setClientName(clientName);
                client.setType(type);
                Addresses addresses = client.getAddresses().get(currentAddressIndexStr);
                addresses.setIp(ip);
                addresses.setMac(mac);
                addresses.setModel(model);
                addresses.setAddress(address);

                // 1 этап
                //clientService.update(client);
                // 2 этап
                updateBean.update(client);
                response.sendRedirect("viewList");
            }
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }
}
