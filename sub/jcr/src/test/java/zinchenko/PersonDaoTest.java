package zinchenko;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.junit.*;

import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zinchenko on 05.01.15.
 */
public class PersonDaoTest {

    static Repository repository;

    PersonDao dao;

    Mapper mapper;

    @BeforeClass
    public static void beforeClass() throws Exception {
        repository = TestUtils.createRepository();
    }

    @Before
    public void before() throws RepositoryException {
        prefillUsers();

        dao = new PersonDao();
        dao.setRepository(repository);

        List<Class> classes = new ArrayList<Class>();
        classes.add(Person.class);
        mapper = new AnnotationMapperImpl(classes);
        dao.setMapper(mapper);
    }

    @After
    public void after() throws RepositoryException {
        //TestUtils.remove(repository, "/users");
    }

    @AfterClass
    public static void afterClass() throws Exception {
        TestUtils.shutdownRepository(repository);
    }

    @Test
    public void test() throws RepositoryException {
        Person person = new Person();
        person.setPath("/persons/ppp");
        person.setFirstName("fn");
        person.setLastName("ln");

        dao.save(person);

        Collection<Person> persons = dao.findPersons();

        findUsers();
    }

    private void prefillUsers() throws RepositoryException {
        Session session = repository.login(TestUtils.TEST_CREDENTIALS);
        try {
            Node users = JcrUtils.getOrAddNode(session.getRootNode(), "persons");
            Node user = JcrUtils.getOrAddNode(users, "person-test");
            user.setProperty("firstName", "fn1");
            user.setProperty("lastName", "ln1");
            user.setProperty("ocm_classname", "zinchenko.Person");
            user = JcrUtils.getOrAddNode(users, "person-test2");
            user.setProperty("firstName", "fn2");
            user.setProperty("lastName", "ln2");
            user.setProperty("ocm_classname", "zinchenko.Person");

            session.save();
        } finally {
            if(session != null) {
                session.logout();
            }
        }
    }

    public void findUsers() throws RepositoryException {
        Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
        try {
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            String query = "//persons/*";
            QueryResult queryResult = queryManager.createQuery(query, Query.XPATH).execute();
            NodeIterator nodeIterator = queryResult.getNodes();
            while (nodeIterator.hasNext()) {
                Node node = nodeIterator.nextNode();
                PropertyIterator propertyIterator = node.getProperties();
                while (propertyIterator.hasNext()) {
                    Property property = propertyIterator.nextProperty();
                    System.out.println(node.getPath() + " - > " + property.getName() +" - "+ property.getString());
                }
            }
        } finally {
            if(session != null) {
                session.logout();
            }
        }
    }

}
