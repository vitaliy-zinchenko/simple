package zinchenko;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import org.jcrom.Jcrom;
import org.jcrom.dao.AbstractJcrDAO;

import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jws.soap.SOAPBinding;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Hello world!
 *
 */
public class UserDao extends AbstractJcrDAO<User> {

    private static final SimpleCredentials SIMPLE_CREDENTIALS = new SimpleCredentials("admin", "admin".toCharArray());

    private static final String ROOT_PATH = "/users";

    private Repository repository;

    public UserDao() {
        super(new Jcrom().map(User.class));
    }

    @Override
    protected Session getSession() {
        try {
            return repository.login(SIMPLE_CREDENTIALS);
        } catch (RepositoryException e) {
            throw new PersistentException("Error while creating Session.", e);
        }
    }

    public void findUsers() throws RepositoryException {
        Session session = repository.login(SIMPLE_CREDENTIALS);
        try {
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            String query = "//users/*";
            QueryResult queryResult = queryManager.createQuery(query, Query.XPATH).execute();
            NodeIterator nodeIterator = queryResult.getNodes();
            while (nodeIterator.hasNext()) {
                Node node = nodeIterator.nextNode();
                String name = JcrUtils.getStringProperty(node, "name", null);
                String email = JcrUtils.getStringProperty(node, "email", null);
                System.out.println(node.getPath() + " - > " + name + "  " + email);
            }
        } finally {
            if(session != null) {
                session.logout();
            }
        }
    }


    public void saveUser(User user) throws RepositoryException {
        Session session = repository.login(SIMPLE_CREDENTIALS);
        try {
            Node root = session.getRootNode();
            Node usersNode = JcrUtils.getOrAddNode(root, "users");

            Node userNode = usersNode.addNode("user-" + user.getEmail());
            userNode.setProperty("name", user.getName());
            userNode.setProperty("email", user.getEmail());

            session.save();
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    public List<User> findAllUsers() {
        return findAll(ROOT_PATH);
    }


    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}
