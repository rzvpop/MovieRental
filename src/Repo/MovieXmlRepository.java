package Repo;
import Domain.Movie;
import Domain.Validators.Validator;
import Domain.Validators.ValidatorException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MovieXmlRepository extends InMemoryRepository<Long, Movie> {
    private String fileName;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document document;
    private Element root;

    public MovieXmlRepository(Validator<Movie> validator, String fileName) {
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
            root = document.createElement("movies");
            document.appendChild(root);
        }
        else {
            loadData();
        }
    }

    private void loadData() throws IOException, SAXException {
        File inp = new File(fileName);
        document = dBuilder.parse(inp);
        document.getDocumentElement().normalize();
        root = document.getDocumentElement();
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        Stream<Node> nodeStream = IntStream.range(0, nodeList.getLength())
                .mapToObj(nodeList::item);
        nodeStream.forEach(item -> {
            Movie mv = new Movie(item.getFirstChild().getTextContent(), item.getChildNodes().item(1).getTextContent(), Integer.parseInt(item.getChildNodes().item(2).getTextContent()));
            mv.setId(Long.parseLong(item.getAttributes().getNamedItem("id").getNodeValue()));
            NodeList clients = item.getLastChild().getChildNodes();
            Stream<Node> clientStream = IntStream.range(0, clients.getLength()).mapToObj(clients::item);
            super.save(mv);
        });
    }

    @Override
    public Optional<Movie> save(Movie entity) throws ValidatorException {
        Optional<Movie> optional = super.save(entity);
        if(optional.isPresent())
            return optional;
        try {
            addMovie(entity);
        } catch (TransformerException e) {
            throw new RuntimeException(e.getMessage());
        }

        return Optional.empty();
    }

    private void addMovie(Movie entity) throws TransformerException {
        Element mv = document.createElement("movie");
        mv.setAttribute("id", entity.getId().toString());
        mv.setIdAttribute("id", true);
        root.appendChild(mv);

        Element title = document.createElement("title");
        title.appendChild(document.createTextNode(entity.getName()));
        mv.appendChild(title);

        Element date = document.createElement("date");
        date.appendChild(document.createTextNode(entity.getDate_time()));
        mv.appendChild(date);

        Element price = document.createElement("price");
        price.appendChild(document.createTextNode(entity.getPrice()+""));
        mv.appendChild(price);

        mv.appendChild(document.createElement("clientList"));

        writeToFile();
    }

    @Override
    public Optional<Movie> update(Movie entity) {
        super.update(entity);
        try {
            updateMovie(entity);
        } catch (TransformerException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Optional.empty();
    }

    private void updateMovie(Movie entity) throws TransformerException {
        Element movie = document.getElementById(entity.getId().toString());
        movie.getFirstChild().setTextContent(entity.getName());
        movie.getChildNodes().item(1).setTextContent(entity.getDate_time());
        movie.getChildNodes().item(2).setTextContent(entity.getPrice()+"");
        writeToFile();
    }

    @Override
    public Optional<Movie> delete(Long id) {
        super.delete(id);
        try {
            deleteMovie(id);
        } catch (TransformerException e) {
            throw new RuntimeException(e.getMessage());
        }

        return Optional.empty();
    }

    private void deleteMovie(Long id) throws TransformerException {
        Element mv = document.getElementById(id.toString());
        document.getElementsByTagName("movies").item(0).removeChild(mv);

        writeToFile();
    }

    private void writeToFile() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(fileName));
        DOMImplementation domImpl = document.getImplementation();
        DocumentType doctype = domImpl.createDocumentType("doctype",
                "moviesDB.dtd",
                "moviesDB.dtd");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());

        transformer.transform(source, result);
    }
}
