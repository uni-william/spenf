package br.com.sis.report;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.jdbc.Work;

import br.com.sis.util.jsf.FacesUtil;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class ExecutorRelatorio implements Work {

	private String caminhoRelatorio;
	private HttpServletResponse response;
	private Map<String, Object> parametros;
	private String nomeArquivoSaida;
	private boolean relatorioGerado;
	private int tipoRetorno;
	private boolean arquivoGerado;

	public ExecutorRelatorio(String caminhoRelatorio, HttpServletResponse response, Map<String, Object> parametros,
			String nomeArquivoSaida, int tipoRetorno) {
		this.caminhoRelatorio = caminhoRelatorio;
		this.response = response;
		this.parametros = parametros;
		this.nomeArquivoSaida = nomeArquivoSaida;
		this.tipoRetorno = tipoRetorno;

		this.parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
	}

	public void execute(Connection connection) throws SQLException {
		try {
			InputStream relatorioStream = this.getClass().getResourceAsStream(this.caminhoRelatorio);

			JasperPrint print = JasperFillManager.fillReport(relatorioStream, this.parametros, connection);
			this.relatorioGerado = print.getPages().size() > 0;

			if (this.relatorioGerado) {
				Exporter<ExporterInput, PdfReportConfiguration, PdfExporterConfiguration, OutputStreamExporterOutput> exportador = new JRPdfExporter();
				exportador.setExporterInput(new SimpleExporterInput(print));
				if (this.tipoRetorno == 0) {
					exportador.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "attachment; filename=\"" + this.nomeArquivoSaida + "\"");
					exportador.exportReport();
				} else {
					JasperExportManager.exportReportToPdfFile(print, FacesUtil.localArquivos() + this.nomeArquivoSaida);
					java.io.File file = new java.io.File(FacesUtil.localArquivos() + this.nomeArquivoSaida);
					arquivoGerado = file.exists();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new SQLException("Erro ao executar relatório " + this.caminhoRelatorio, e);
		}
	}

	public boolean isRelatorioGerado() {
		return relatorioGerado;
	}
	
	public boolean isArquivoGerado() {
		return arquivoGerado;
	}

}
