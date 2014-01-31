import java.io.*;

/**
 * The test class CollapseConsoleTest.
 *
 * @author  Erik Owen
 * @version 1
 */
public class CollapseConsoleTest extends junit.framework.TestCase
{
    /**
     * Default constructor for test class CollapseConsoleTest
     */
    public CollapseConsoleTest()
    {

    }

    /**
     * Tests some of the CollapseConsole functionality.
     */
    public void test1()
    {
        try
        {
            CollapseConsole app = new CollapseConsole();
            app.setIOsources(new FileReader(new File("testData/testInput1.txt")),
                new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("testData/testOutput1.txt"), "utf-8")));
            app.run();
        }
        catch(Exception e)
        {
            System.out.print("");
        }
    }
    
    /**
     * Mainly tests the run method
     */
    public void test2()
    {
        try
        {
            CollapseConsole app = new CollapseConsole();
            app.setIOsources(new FileReader(new File("testData/testInput.txt")),
                new OutputStreamWriter(System.out));
            app.run();
        }
        catch(Exception e)
        {
            System.out.print("");
        }
    }

    /**
     * Tests some of the CollapseConsole functionality.
     */
    public void test3()
    {
        try
        {
            CollapseConsole app = new CollapseConsole();
            app.setIOsources(new FileReader(new File("testData/testInput2.txt")),
                new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("testData/testOutput2.txt"), "utf-8")));
            app.run();
        }
        catch(Exception e)
        {
            System.out.print("");
        }
    }

    /**
     * Tests some of the CollapseConsole functionality.
     */
    public void test4()
    {
        try
        {
            CollapseConsole app = new CollapseConsole();
            app.setIOsources(new FileReader(new File("testData/testInput3.txt")),
                new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("testData/testOutput3.txt"), "utf-8")));
            app.run();
        }
        catch(Exception e)
        {
            System.out.print("");
        }
    }
    
    /**
     * Tests some of the CollapseConsole functionality.
     */
    public void test5()
    {
        try
        {
            CollapseConsole app = new CollapseConsole();
            app.setIOsources(new FileReader(new File("testData/testInput4.txt")),
                new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("testData/testOutput4.txt"), "utf-8")));
            app.run();
        }
        catch(Exception e)
        {
            System.out.print(""); 
        }
    }

    /**
     * Tests some of the CollapseConsole functionality.
     */
    public void test6()
    {
        try
        {
            CollapseConsole app = new CollapseConsole();
            app.setIOsources(new FileReader(new File("testData/testInput5.txt")),
                new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("testData/testOutput5.txt"), "utf-8")));
            app.run();
        }
        catch(Exception e)
        {
            System.out.print("");
        }
    }
    
    /**
     * Tests some of the CollapseConsole functionality.
     */
    public void test7()
    {
        try
        {
            CollapseConsole app = new CollapseConsole();
            app.setIOsources(new FileReader(new File("testData/testInput6.txt")),
                new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("testData/testOutput6.txt"), "utf-8")));
            app.run();
        }
        catch(Exception e)
        {
            System.out.print("");   
        }
    }

    /**
     * Tests some of the CollapseConsole functionality.
     */
    public void test8()
    {
        try
        {
            CollapseConsole app = new CollapseConsole();
            app.setIOsources(new FileReader(new File("testData/testInput7.txt")),
                new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("testData/testOutput7.txt"), "utf-8")));
            app.run();
        }
        catch(Exception e)
        {
            System.out.print("");   
        }
    }
}









