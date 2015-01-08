package zinchenko;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.*;

import javax.jcr.*;
import javax.jcr.Node;
import java.io.File;
import java.io.InputStream;

/**
 * Created by zinchenko on 04.01.15.
 */
public abstract class TestUtils {

    public static final Credentials TEST_CREDENTIALS = new SimpleCredentials("admin", "admin".toCharArray());

    public static Repository createRepository() throws Exception {
        InputStream xml = UserDao.class.getResourceAsStream("/repository.xml");
        String dir = new File(UserDaoTest.class.getResource("/").getFile()+"../jcr_repo").getCanonicalPath();
        RepositoryConfig config = RepositoryConfig.create(xml, dir);
        return RepositoryImpl.create(config);
    }


    public static void shutdownRepository(Repository repository) throws Exception {
        if(repository != null) {
            ((RepositoryImpl) repository).shutdown();
        }
    }

    public static void remove(Repository repository, final String path) throws Exception {
        execute(repository, new Command<Object>() {
            @Override
            public void execute(Session session) throws Exception {
                if(session.nodeExists(path)) {
                    session.getNode(path).remove();
                }
            }
        });
    }

    public static void importXml(Repository repository, final String fullClassPathName) throws Exception {
        TestUtils.execute(repository, new TestUtils.Command<Object>() {
            @Override
            public void execute(Session session) throws Exception {
                InputStream inputStream = UserDaoTest.class.getResourceAsStream(fullClassPathName);
                session.importXML(session.getRootNode().getPath(), inputStream, ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);
                inputStream.close();
            }
        });
    }

    public static Session login(Repository repository) throws RepositoryException {
        return repository.login(TestUtils.TEST_CREDENTIALS);
    }

    public static void logout(Session session) {
        if (session != null) {
            session.logout();
        }
    }

    public static <T> void execute(Repository repository, Command<T> daoCommand) throws Exception {
        Session session = null;
        try {
            session = login(repository);
            daoCommand.execute(session);
            session.save();
        } finally {
            logout(session);
        }
    }

    public static interface Command<T> {
        void execute(Session session) throws Exception;
    }

}
