package servlets.fileupload;

import com.google.gson.Gson;
import dto.loadedmachine.LoadedMachineDTO;
import enigma.managers.BattleFieldManager;
import enigma.managers.FilesManager;
import enigma.managers.MachineManager;
import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.Collection;
import java.util.Scanner;

@WebServlet("/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Collection<Part> parts = request.getParts();
        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {
            fileContent.append(this.readFromInputStream(part.getInputStream()));
        }

        InputStream is = new ByteArrayInputStream(fileContent.toString().getBytes());
        try {
            this.processRequest(response, is);
        } catch (JAXBException e) {
            throw new RuntimeException();
        }
    }

    private void processRequest(HttpServletResponse response, InputStream is) throws IOException, JAXBException {
        PrintWriter out = response.getWriter();
        FilesManager xmlManager = new FilesManager();
        xmlManager.checkFile(is);
        if(xmlManager.getFileErrors().isEmpty()) {
            BattleFieldManager battleFieldManager =
                    ServletUtils.getBattleFieldManager(getServletContext());
            if(!battleFieldManager.isBattleExists(xmlManager.getCurrentBattleFieldName())) {
                response.setContentType("application/json");
                MachineManager machineManager = new MachineManager();
                LoadedMachineDTO loadedMachineDTO =
                        machineManager.configureMachine(
                                xmlManager.getCurrentLoadedMachine()
                        );
                battleFieldManager.addBattleField(loadedMachineDTO.getBattleFieldDTO().getBattleFieldName(), machineManager);
                response.setStatus(HttpServletResponse.SC_OK);
                Gson gson = new Gson();
                String json = gson.toJson(loadedMachineDTO);
                out.println(json);
            }
            else{
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Can't upload the same file more than once.");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            for(String error : xmlManager.getFileErrors()){
                out.println(error);
            }
        }
        out.flush();
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }


}