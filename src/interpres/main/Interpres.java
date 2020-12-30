package interpres;

/* Java Web Application Requirements
 * */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.servlet.*;
import javax.servlet.http.*;

/* Nashorn Requirements
 * */
import java.io.StringWriter;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

public class Interpres extends HttpServlet {
    
    public void displayOutput(HttpServletRequest request, HttpServletResponse response, String jjs, String results)
        throws IOException
    {
        PrintWriter out = response.getWriter();

        try
        {
            ResourceBundle rb = ResourceBundle.getBundle("interpres", request.getLocale());
            String title = rb.getString("interpres.title");

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>" + title + "</title>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            out.println("<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css'>");
            out.println("<script src='https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js'></script>");
            out.println("</head>");
            out.println("<body>");

            out.println("<nav class='navbar navbar-expand-lg navbar-light bg-light'><div class='container'>" + title + "</div></nav>");

            out.println("<div class='container' style='padding: 40px 15px'>");

            out.println("<div class='row'>");
            out.println("<div class='col'>");
            out.println("<form method='post'>");
            out.println("  <div class='form-group'>");
            out.println("    <textarea rows='12' class='form-control' style='font-family: Consolas, Lucida Console, monospace;' id='jjs' name='jjs' placeholder='Code here ...'>" + jjs + "</textarea>");
            out.println("  </div>");
            out.println("  <button type='submit' class='btn btn-primary btn-block'>Run</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");

            out.println("<br /><div class='row'><div class='col'><h3>Results:</h3></div></div>");

            out.println("<div class='row'>");
            out.println("<div class='col'>");
            //out.println("<p style='font-family: Consolas, Lucida Console, monospace;'>" + results + "</p>");
            out.println("<pre style='font-family: Consolas, Lucida Console, monospace;'>" + results + "</pre>");
            out.println("</div>");
            out.println("</div>");

            out.println("</body>");
            out.println("</html>");
        }
        catch (Exception e)
        {
            System.err.println("displayOutput: " + e);
        }
        finally
        {
            out.close();
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=UTF-8");

        try
        {
            displayOutput(request, response, "", "No results yet ...");
        }
        catch (Exception e)
        {
            System.err.println("doGet: " + e);
        }
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=UTF-8");

        String[] arguments = new String[] {
            "-strict",
            "--dump-on-error",
            "--no-java",
            "--no-syntax-extensions"
        };

        String jjs = request.getParameter("jjs");
        StringWriter sw = new StringWriter();

        try
        {
            NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
            ScriptEngine engine = factory.getScriptEngine();

            ScriptContext context = engine.getContext();
            context.setWriter(sw);
            context.setErrorWriter(sw);
            
            engine.eval( jjs );
        }
        catch (ScriptException s)
        {
            sw.write( s.toString() );
        }
        finally
        {
            displayOutput(request, response, jjs, sw.toString());
        }
    }

}
