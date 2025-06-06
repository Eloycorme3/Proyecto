/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reports;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import vista.GestorAdministrador;

/**
 *
 * @author eloy.castro
 */
public class ReportService {

    private final String url;
    private final String user;
    private final String password;

    public ReportService(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Report get(String reportPath) throws Exception {
        return get(reportPath, null);
    }

    public Report get(String reportPath, Map<String, Object> parameters) throws Exception {
        try (InputStream is = GestorAdministrador.class.getResourceAsStream(reportPath)) {
            try (Connection con = DriverManager.getConnection(url, user, password)) {
                JasperReport jr = JasperCompileManager.compileReport(is);
                JasperPrint jp = JasperFillManager.fillReport(jr, parameters, con);
                return new Report(jp);
            }
        }
    }
}
