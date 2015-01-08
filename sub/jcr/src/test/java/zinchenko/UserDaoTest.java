package zinchenko;

import org.apache.jackrabbit.commons.JcrUtils;
import org.junit.*;

import javax.jcr.*;
import java.io.InputStream;
import java.util.List;

public class UserDaoTest {

    static Repository repository;

    UserDao app;

    @BeforeClass
    public static void beforeClass() throws Exception {
        repository = TestUtils.createRepository();
    }

    @Before
    public void before() throws Exception {
        prefillUsers();

        app = new UserDao();
        app.setRepository(repository);
    }

    @After
    public void after() throws Exception {
        TestUtils.remove(repository, "/users");
    }

    @AfterClass
    public static void afterClass() throws Exception {
        TestUtils.shutdownRepository(repository);
    }

    private void prefillUsers() throws Exception {
//        TestUtils.execute(repository, new TestUtils.Command<Object>() {
//            @Override
//            public void execute(Session session) throws Exception {
//                Node users = JcrUtils.getOrAddNode(session.getRootNode(), "users");
//                Node user = JcrUtils.getOrAddNode(users, "user-test");
//                user.setProperty("email", "test@email");
//                user.setProperty("name", "testName");
//                user = JcrUtils.getOrAddNode(users, "user-test2");
//                user.setProperty("email", "test@email2");
//                user.setProperty("name", "testName2");
//            }
//        });

        TestUtils.importXml(repository, "/zinchenko/test.xml");

    }

    @Test
    public void testApp() throws RepositoryException {
        List<User> users = app.findAllUsers();

        Assert.assertEquals(2, users.size());
    }
}
