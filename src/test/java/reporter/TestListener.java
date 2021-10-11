package reporter;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import com.sun.tools.xjc.generator.bean.ImplStructureStrategy.Result;

public class TestListener implements ITestListener {

	private static String getTestMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getConstructorOrMethod().getName();
	}
	@Override
	public void onStart(ITestContext iTestContext) {
		ExtentManager.createExtentReports();
	}
	@Override
	public void onFinish(ITestContext iTestContext) {
		
		
		ExtentManager.extentReports.flush();
		
	}
	@Override
	public void onTestStart(ITestResult iTestResult) {
		ExtentTestManager.startTest(iTestResult.getMethod().getMethodName(), iTestResult.getMethod().getDescription());
	}
	@Override
	public void onTestSuccess(ITestResult iTestResult) {
		
		ExtentTestManager.getTest().log(Status.PASS, "Test passed");
	}
	@Override
	public void onTestFailure(ITestResult iTestResult) {
		
//		ExtentTestManager.getTest().log(Status.FAIL, "Test Failed",
//				ExtentTestManager.getTest().addScreenCaptureFromBase64String(base64Screenshot).getModel().getMedia().get(0));
	}
	@Override
	public void onTestSkipped(ITestResult iTestResult) {

		ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
	}
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
		
	}

}
