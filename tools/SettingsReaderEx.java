package ceeport.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author t10000
 */
public class SettingsReaderEx {

    private final JAXBContext context;
    private final Marshaller marshaller;
    private final Unmarshaller uMarshaller;
    
    
    public SettingsReaderEx() throws JAXBException {
        context = JAXBContext.newInstance(Settings.class);
        marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        uMarshaller = context.createUnmarshaller();
    }
    
    
    public Settings read(String fileName) throws FileNotFoundException, JAXBException {
        Settings settigs = (Settings) uMarshaller.unmarshal(new FileReader(fileName));
        
        return settigs;
    }
    
    
    public void write(String fileName, Settings settings) throws JAXBException {
        marshaller.marshal(settings, new File(fileName));
    }
    
    
    public void writeConsole(Settings settings) throws JAXBException {
        marshaller.marshal(settings, System.out);
    }
    
}
