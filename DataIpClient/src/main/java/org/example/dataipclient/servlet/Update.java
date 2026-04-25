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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

        String currentAddressIdStr = request.getParameter("curAdrId");
        //Integer currentAddressId = Integer.parseInt(request.getParameter("curAdrId"));

        if (clientId != null && !clientId.trim().isEmpty()) {
            Integer id = Integer.parseInt(clientId);
            Client client = clientService.findById(id);
            if (client == null) {
                response.sendRedirect("viewList");
                return;
            }

            if (client != null && client.getAddresses() != null) {
                List<Addresses> addresses = client.getAddresses();

                //=====поиск следующего id адреса в коллекции данного клиента
                Integer currentAddressId = 0;
                if (currentAddressIdStr != null) {
                    currentAddressId = Integer.parseInt(currentAddressIdStr);
                }
                int result= 0;
                for (Addresses adr : addresses){
                    if ("next".equals(action) || !("prev".equals(action))){
                    if (adr.getAddressId() > currentAddressId){
                        if (result == 0 || adr.getAddressId() < result)
                            result = adr.getAddressId();
                    }
                    } else  if ("prev".equals(action)){
                        if (adr.getAddressId() < currentAddressId){
                            if (result == 0 || adr.getAddressId() > result)
                                result = adr.getAddressId();
                        }
                    }
                }
                currentAddressId  = result;
                //====== поиск в коллекции индекса элемента по его addressId
                    int numAddr = 0;
                        for (Addresses adr : addresses) {
                            if (adr.getAddressId().equals(currentAddressId)) {
                                break;
                            }
                            if (numAddr < addresses.size() - 1)
                                ++numAddr;
                        }
                //======

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
                out.println("<input type='hidden' name='curAdrId' value='" + currentAddressId + "'>");

                out.println("<label>Наименование клиента: ");
                out.println("<input type='text' name='clientName' value='" + client.getClientName() + "' required></label><br><br>");
                out.println("<input type='hidden' name='added' value='" + client.getAdded() + "' required></label><br><br>");

                out.println("<label>Тип клиента:");
                out.println("<select name='type' required>");
                out.println("<option value='Corporate'" + ("Corporate".equals(client.getType()) ? " selected" : "") + ">Corporate</option>");
                out.println("<option value='Individual'" + ("Individual".equals(client.getType()) ? " selected" : "") + ">Individual</option>");
                out.println("</select></label><br><br>");



                out.println("<h3>Информация об адресе клиента (адрес " + (currentAddressIndex + 1) + " из " + addresses.size() + ")</h3>");
                out.println("<label>IP-адрес: ");
                out.println("<input type='text' name='ip' value='" + addresses.get(numAddr).getIp() + "' placeholder='192.168.0.1' required></label><br><br>");
                out.println("<label>MAC-адрес: ");
                out.println("<input type='text' name='mac' value='" + addresses.get(numAddr).getMac() + "' placeholder='00:1A:2B:3C:4D:5E' required></label><br><br>");
                out.println("<label>Модель устройства: ");
                out.println("<input type='text' name='model' value='" + addresses.get(numAddr).getModel() + "' required></label><br><br>");
                out.println("<label>Адрес места нахождения: ");
                out.println("<input type='text' name='address' value='" + addresses.get(numAddr).getAddress() + "' required></label><br><br>");
                out.println("<input type='hidden' name='addressId' value='" + addresses.get(numAddr).getAddressId() + "' required></label><br><br>");

                out.println("<input type='submit' name ='action' value='UPDATE'/>");
                out.println("<input type='submit' name ='action' value='ADD ADDRESS'/>");
                out.println("<input type='submit' name ='action' value='DELETE ADDRESS'/>");
                out.println("</form><br>");

                out.println("<form method='get' action='update_servlet' style='margin: 20px 0;'>");
                out.println("<input type='hidden' name='clientId' value='" + client.getClientId() + "'>");
                out.println("<input type='hidden' name='addressIndex' value='" + currentAddressIndex + "'>");
                out.println("<input type='hidden' name='curAdrId' value='" + currentAddressId + "'>");
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

    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        List<String> errors = new ArrayList<>();

        String action = request.getParameter("action");
        if ("UPDATE".equals(action)) {
            Integer clientId = Integer.parseInt(request.getParameter("clientId"));
            String clientName = request.getParameter("clientName");
            String type = request.getParameter("type");
            LocalDate added = LocalDate.parse(request.getParameter("added"));
            String ip = request.getParameter("ip");
            String mac = request.getParameter("mac");
            String model = request.getParameter("model");
            String address = request.getParameter("address");
            Integer addressId = Integer.parseInt(request.getParameter("addressId"));
            int currentAddressIndexStr = Integer.parseInt(request.getParameter("addressIndex"));

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
                errors.add("Некорректный адрес");
            }

            if (errors.isEmpty() && clientId != 0) {
                Client client = new Client();//clientService.findById(clientId);           //убрать обращение к БД (просто создать клиента с адресами через new и передать в update)
                //  if (client != null) {                                                   //второе - правильное редактирование адреса (по id а не по счетчику)
                client.setClientId(clientId);
                client.setClientName(clientName);
                client.setType(type);
                client.setAdded(added);
                Addresses addresses = new Addresses();//client.getAddresses().get(currentAddressIndexStr);
                addresses.setAddressId(addressId);
                addresses.setIp(ip);
                addresses.setMac(mac);
                addresses.setModel(model);
                addresses.setAddress(address);
                addresses.setClient(client);
                client.getAddresses().add(addresses);
                // 1 этап
                //clientService.update(client);
                // 2 этап
                updateBean.update(client);
                response.sendRedirect("viewList");
                //  }
            } else {
                request.setAttribute("errors", errors);
                doGet(request, response);
            }
        }
        if ("DELETE ADDRESS".equals(action)) {
            Integer clientId = Integer.parseInt(request.getParameter("clientId"));
            Integer addressId = Integer.parseInt(request.getParameter("addressId"));
             if (updateBean.deleteAddr(clientId, addressId)){
               response.sendRedirect("viewList");
             } else {
                 errors.add("Нельзя удалить последний адрес клиента");
                 request.setAttribute("errors", errors);
                 doGet(request, response);
             }
        }
        if ("ADD ADDRESS".equals(action)) {

            Integer clientId = Integer.parseInt(request.getParameter("clientId"));
            String ip = request.getParameter("ip");
            String mac = request.getParameter("mac");
            String model = request.getParameter("model");
            String address = request.getParameter("address");
            Integer addressId = Integer.parseInt(request.getParameter("addressId"));

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
                errors.add("Некорректный адрес");
            }

            if (errors.isEmpty() && clientId != 0) {

                Addresses addresses = new Addresses();
                addresses.setIp(ip);
                addresses.setMac(mac);
                addresses.setModel(model);
                addresses.setAddress(address);
                updateBean.addAddr(clientId, addresses);
                response.sendRedirect("viewList");
            }else {
                request.setAttribute("errors", errors);
                doGet(request, response);
            }
        }

    }
}
