package org.example.dataipclient.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dataipclient.bean.SelectBean;
import org.example.dataipclient.models.Addresses;
import org.example.dataipclient.models.Client;
import org.example.dataipclient.service.ClientService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "view", value = "/")
public class ViewList extends HttpServlet {
    public static final String APP_URI = "DataIpClient-1.0-SNAPSHOT";

    @EJB
    private ClientService clientService;

    @EJB
    private SelectBean selectBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        getBody(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getBody(request, response);
    }

    public void destroy() {
    }

    private void getBody(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String filterType = Optional.ofNullable( request.getParameter("filterType")).orElse("All");
        //if (filterType == null) { filterType =  "All";}
        String searchText = request.getParameter("searchText");


        //================ 1 этап
//        List<Client> clients = clientService.findAll().stream()  //1 этап
//                .sorted((o1, o2) -> {
//                    int res = o1.getClientId().compareTo(o2.getClientId());
//                    return res;
//                })
//                .collect(Collectors.toList());
//
//        if (filterType != null && !filterType.isEmpty() && !"All".equals(filterType)) {
//            clients = clients.stream()
//                    .filter(client -> filterType.equals(client.getType()))
//                    .collect(Collectors.toList());
//        }
//
//        if (searchText != null && !searchText.isEmpty()) {
//            String searchTextLow = searchText.toLowerCase();
//            clients = clients.stream()
//                    .filter(client ->
//                            client.getClientName().toLowerCase().contains(searchTextLow) ||
//                                    client.getAddresses().stream()
//                                            .anyMatch(addr -> addr.getAddress().toLowerCase().contains(searchTextLow)))
//                    .collect(Collectors.toList());
//        }
        //============= 2 этап
        List<Client> clients = new ArrayList<>();
        if (filterType != null && !filterType.isEmpty() && "All".equals(filterType) && searchText == null) {
            clients = selectBean.findAll(). stream()
                .sorted((o1, o2) -> {
                    int res = o1.getClientId().compareTo(o2.getClientId());
                    return res;
                })
                .collect(Collectors.toList());
        } else {
            clients = selectBean.findClientsWithFilter(filterType, searchText).stream().toList();
        }
        //============
        PrintWriter out = response.getWriter();
        out.println("<head><meta charset=\"UTF-8\"><title>Title</title></head>");
        out.println("<html><body>");

        out.println("<form action=\"create_servlet\" method=\"get\">\n" +
                "    <input type=\"submit\" value=\"CREATE CLIENT\">\n" +
                "</form><br>");

        out.println("<form method='get' action='viewList'>");
        out.println("<label>Тип клиента:");
        out.println("<select name='filterType'>");
        out.println("<option value='All'" + ("All".equals(filterType) ? " selected" : "") + ">All</option>");
        out.println("<option value='Corporate'" + ("Corporate".equals(filterType) ? " selected" : "") + ">Corporate</option>");
        out.println("<option value='Individual'" + ("Individual".equals(filterType) ? " selected" : "") + ">Individual</option>");
        out.println("</select></label>");
        out.println("<label>Поиск: <input type='text' name='searchText' value='" + (searchText != null ? searchText : "") + "' placeholder='Введите текст для поиска'></label>");
        out.println("<input type='submit' value='Применить фильтр'>");
        out.println("</form><br>");

        out.println("<table border=\"2\" cellpadding=\"2\" cellspacing=\"2\">");
        out.println("<tr><th>ID</th><th>Наименование</th><th>Тип</th><th>Дата добавления</th>" +
                "<th>IP-адрес</th><th>MAC-адрес</th><th>Модель</th><th>Адрес</th><th>Действия</th></tr>");
        for(Client client : clients){
            out.println("<tr>");
            out.println("<td>" + (client.getClientId()!=-1L ? client.getClientId() : "") + "</td>");
            out.println("<td>" + (client.getClientName()!=null ? client.getClientName() : "") + "</td>");
            out.println("<td>" + (client.getType()!=null ? client.getType() : "") + "</td>");
            out.println("<td>" + (client.getAdded()!=null ? client.getAdded(): "") + "</td>");
            out.println("<td>" + (client.getAddresses()!=null ? client.getAddresses().stream().map(Addresses::getIp).collect(Collectors.joining("<hr>")): "") + "</td>");
            out.println("<td>" + (client.getAddresses()!=null ? client.getAddresses().stream().map(Addresses::getMac).collect(Collectors.joining("<hr>")): "") + "</td>");
            out.println("<td>" + (client.getAddresses()!=null ? client.getAddresses().stream().map(Addresses::getModel).collect(Collectors.joining("<hr>")): "") + "</td>");
            out.println("<td>" + (client.getAddresses()!=null ? client.getAddresses().stream().map(Addresses::getAddress).collect(Collectors.joining("<hr>")): "") + "</td>");
            out.println("<td><a href=\"/" + APP_URI + "/update_servlet?clientId=" + client.getClientId() + "\" title=\"\">Update</a><br>"+
                    "<a href=\"/" + APP_URI + "/delete_servlet?clientId=" + client.getClientId() + "\" title=\"\">Delete</a>" +
                    "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body></html>");
    }
}
