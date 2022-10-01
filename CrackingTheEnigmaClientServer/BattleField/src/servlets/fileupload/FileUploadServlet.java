package servlets.fileupload;

import enigma.xml.generated.CTEEnigma;
import enigma.xml.validator.CTEValidator;
import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

@WebServlet("/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    private static final String GENERATED_CLASSES_REF = "enigma.xml.generated";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        Collection<Part> parts = request.getParts();
        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {
            fileContent.append(this.readFromInputStream(part.getInputStream()));
        }

        InputStream is = new ByteArrayInputStream(fileContent.toString().getBytes());
        try {
            List<String> isValid = new CTEValidator().isValid(this.deserializeFrom(is));
            if(isValid.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_OK);
            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                for(String exception : isValid){
                    out.println(exception);
                }
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(GENERATED_CLASSES_REF);
        Unmarshaller u = jc.createUnmarshaller();

        return (CTEEnigma) u.unmarshal(in);
    }
}