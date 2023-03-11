package losor.controller.util;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class ResponseUtil
{
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    public static void writeJsonResponse(HttpServletResponse res, String json) throws IOException
    {
        res.setContentType("application/json");
        PrintWriter writer = res.getWriter();
        writer.write(json);
    }

    public static void writeJsonResponse(HttpServletResponse res, Object toConvert) throws IOException
    {
        writeJsonResponse(res, gson.toJson(toConvert));
    }

    public static void writeJsonErrorResponse(HttpServletResponse res, String errorMessage) throws IOException
    {
        writeJsonResponse(res, "{\"errorMessage\":\"" + errorMessage + "\"}");
    }
}
