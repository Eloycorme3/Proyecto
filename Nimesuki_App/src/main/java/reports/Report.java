/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reports;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author eloy.castro
 */
public class Report {

    private final JasperPrint report;

    protected Report(JasperPrint jp) {
        this.report = jp;
    }

    public Report view() {
        JasperViewer.viewReport(report, false);
        return this;
    }

    public Report toPdf(String destFile) throws IOException {
        return toPdf(destFile, true);
    }
    
    public JasperPrint getJP() throws Exception {
        return report;
    }

    public Report toPdf(String destFile, boolean open) throws IOException {
        if (!destFile.endsWith(".pdf") || !destFile.endsWith(".PDF")) {
            destFile = destFile + ".pdf";
        } 
        try {
            JasperExportManager.exportReportToPdfFile(report, destFile);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
        if (open) {
            Desktop.getDesktop().open(new File(destFile));
        }
        return this;
    }
}
