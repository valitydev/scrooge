package dev.vality.scrooge.endpoint;

import dev.vality.scrooge.account.AccountServiceSrv;
import dev.vality.woody.thrift.impl.http.THServiceBuilder;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@WebServlet(urlPatterns = {"/terminal/balance"})
public class TerminalBalanceServlet extends GenericServlet {

    private Servlet thriftServlet;

    @Autowired
    private AccountServiceSrv.Iface requestHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        thriftServlet = new THServiceBuilder().build(AccountServiceSrv.Iface.class, requestHandler);
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        thriftServlet.service(request, response);
    }
}
