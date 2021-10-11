package simple;
import org.testng.Assert;
import org.testng.annotations.*;

public class Test1 {

    @BeforeSuite(alwaysRun = true)
    public void setupSuite(){
        System.out.println("Before Suite Setup ...... ");
    }

    @BeforeTest(alwaysRun = true)
    public void setUpTest() throws Exception {
        System.out.println("Before Test Setup ...... ");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpMethod() {
        System.out.println("Before Method Setup ...... ");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownMethod(){
        System.out.println("After Method Setup ...... ");
    }

    @AfterTest(alwaysRun = true)
    public void tearDownSuite() throws Exception {
        System.out.println("After test Setup ...... ");
    }

    @Test(groups = {"sometest"}, enabled = true,priority = 1)
    public void someTest(){
        System.out.println("Some test ...... ");
        Assert.fail("Intended Failed");
    }

    @Test(groups = {"anothertest","sometest"},priority = 2)
    public void anothertest(){
        System.out.println("Another test ...... ");
    }
    
    @Test(groups = {"sometest"}, enabled = true,priority = 1)
    public void nextTest(){
        System.out.println("nextTest...... ");
       
    }
}