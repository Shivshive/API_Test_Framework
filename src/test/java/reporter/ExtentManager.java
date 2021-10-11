package reporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
	
	public static final ExtentReports extentReports = new ExtentReports();
	
	public synchronized static ExtentReports createExtentReports() {
		
		ExtentSparkReporter reporter = new ExtentSparkReporter("./extent-reports/extent-report.html");
		
		reporter.config().setReportName("Sample Extent Report");
		reporter.config().setDocumentTitle("Test Report");
		reporter.config().setTheme(Theme.DARK);
		reporter.config().setTimelineEnabled(true);
		reporter.config().setProtocol(Protocol.HTTP);
		reporter.config().equals(true);

		extentReports.attachReporter(reporter);
		
		
		extentReports.setSystemInfo("Test Strategy", "Rest API Testing");
		extentReports.setSystemInfo("Author", "Shiv");
		
		return extentReports;
	}
}