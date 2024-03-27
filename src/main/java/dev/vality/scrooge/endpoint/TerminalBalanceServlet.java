package dev.vality.scrooge.endpoint;

import dev.vality.scrooge.terminal.balance.TerminalServiceSrv;
import dev.vality.woody.thrift.impl.http.THServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet(urlPatterns = {"/terminal/balance"})
public class TerminalBalanceServlet extends GenericServlet {

    private Servlet thriftServlet;

    @Autowired
    private TerminalServiceSrv.Iface requestHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        thriftServlet = new THServiceBuilder().build(TerminalServiceSrv.Iface.class, requestHandler);
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        thriftServlet.service(request, response);
    }
}
