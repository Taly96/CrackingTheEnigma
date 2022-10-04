package enigma.engine.managers;

import enigma.xml.generated.CTEEnigma;
import enigma.xml.validator.CTEValidator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FilesManager {

    private static final String GENERATED_CLASSES_REF = "enigma.xml.generated";

    private List<String> fileErrors = null;

    private CTEEnigma currentLoadedMachine = null;

    public FilesManager(){
        this.fileErrors = new ArrayList<>();
    }

    public void checkFile(InputStream is) throws JAXBException {
        this.currentLoadedMachine = this.deserializeFrom(is);
        this.fileErrors = new CTEValidator().isValid(this.currentLoadedMachine);
    }

    private CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(GENERATED_CLASSES_REF);
        Unmarshaller u = jc.createUnmarshaller();

        return (CTEEnigma) u.unmarshal(in);
    }

    public List<String> getFileErrors() {
        return this.fileErrors;
    }

    public CTEEnigma getCurrentLoadedMachine() {
        return this.currentLoadedMachine;
    }
}
