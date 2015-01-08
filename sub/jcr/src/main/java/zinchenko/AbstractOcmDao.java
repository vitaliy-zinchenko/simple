package zinchenko;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;

import javax.jcr.*;
import javax.jnlp.PersistenceService;

/**
 * Created by zinchenko on 05.01.15.
 */
@Deprecated
public class AbstractOcmDao {

    protected Mapper mapper;

    protected Repository repository;

    protected ObjectContentManager getObjectContentManager(Session session) {
        return new ObjectContentManagerImpl(session, mapper);
    }

    protected Session login() throws RepositoryException {
        return repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
    }

    protected void logout(Session session) {
        if (session != null) {
            session.logout();
        }
    }

    protected <T> T execute(DaoCommand<T> daoCommand) throws PersistentException {
        Session session = null;
        try {
            session = login();
            return daoCommand.execute(getObjectContentManager(session));
        } catch (RepositoryException e) {
            throw new PersistentException("Failed during executing dao command", e);
        } finally {
            logout(session);

        }
    }

    public Mapper getMapper() {
        return mapper;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    interface DaoCommand<T> {
        T execute(ObjectContentManager contentManager);
    }

}
