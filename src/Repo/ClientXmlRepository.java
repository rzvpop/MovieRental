package Repo;
import Domain.Client;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;
import Repo.InMemoryRepository;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ClientXmlRepository extends InMemoryRepository<Long, Client> {
    private String fileName;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document document;
    private Element root;

    public ClientXmlRepository(Validator<Client> validator, String fileName) {
        super(validator);
        this.fileName = fileName;

        try {
            checkFile();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void checkFile() throws ParserConfigurationException, IOException, SAXException {
        File inp = new File(fileName);
        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
        if(!inp.exists()) {
            document = dBuilder.newDocument();
            root = document.createElement("clients");
            document.appendChild(root);
        }
        else {
            loadData();
        }
    }

    private void loadData() throws IOException, SAXException {
        File inp = new File(fileName);
        document = dBuilder.parse(inp);
        root = document.getDocumentElement();
        root.normalize();
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        Stream<Node> nodeStream = IntStream.range(0, nodeList.getLength())
                .mapToObj(nodeList::item);
        nodeStream.forEach(item -> {
            Client cl = new Client(item.getFirstChild().getTextContent(), Integer.parseInt(item.getChildNodes().item(1).getTextContent()));
            cl.setId(Long.parseLong(item.getAttributes().getNamedItem("id").getNodeValue()));
            super.save(cl);
        });
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException {
        Optional<Client> optional = super.save(entity);
        if(optional.isPresent()) {
            return optional;
        }
        try {
            addClient(entity);
        } catch (TransformerException e) {
            throw new ValidatorException("Error writing XML");
        }
        return Optional.empty();
    }

    private void addClient(Client entity) throws TransformerException {
        Element cl = document.createElement("client");
        cl.setAttribute("id", entity.getId().toString());
        cl.setIdAttribute("id", true);
        root.appendChild(cl);

        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(entity.getName()));
        cl.appendChild(name);

        Element age = document.createElement("age");
        age.appendChild(document.createTextNode(Integer.toString(entity.getAge())));
        cl.appendChild(age);

        writeToFile();
    }

    @Override
    public Optional<Client> update(Client entity) {
        super.update(entity);
        try {
            updateClient(entity);
        } catch (TransformerException e) {
            throw new RuntimeException(e.getMessage());
        }

        return Optional.empty();
    }

    private void updateClient(Client entity) throws TransformerException {
        Element client = document.getElementById(entity.getId().toString());
        client.getElementsByTagName("name").item(0).setTextContent(entity.getName());
        client.getElementsByTagName("age").item(0).setTextContent(Integer.toString(entity.getAge()));
        client.removeChild(client.getElementsByTagName("movieList").item(0));
        writeToFile();
    }

    @Override
    public Optional<Client> delete(Long id) {
        super.delete(id);
        try {
            deleteClient(id);
        } catch (TransformerException e) {
            throw new RuntimeException(e.getMessage());
        }

        return Optional.empty();
    }

    private void deleteClient(Long id) throws TransformerException {
        Element cl = document.getElementById(id.toString());
        document.getElementsByTagName("clients").item(0).removeChild(cl);

        writeToFile();
    }

    public int size(){return 0;}

    private void writeToFile() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(fileName));

        DOMImplementation domImpl = document.getImplementation();
        DocumentType doctype = domImpl.createDocumentType("doctype",
                "clientsDB.dtd",
                "clientsDB.dtd");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());

        transformer.transform(source, result);
    }
}
