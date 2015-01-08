package zinchenko;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.mapper.Mapper;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import java.util.Collection;

/**
 * Created by zinchenko on 05.01.15.
 */
@Deprecated
public class PersonDao extends AbstractOcmDao {

    public void save(final Person person) {
        execute(new DaoCommand<Void>() {
            @Override
            public Void execute(ObjectContentManager contentManager) {
                contentManager.insert(person);
                contentManager.save();
                return null;
            }
        });
    }

    public Collection<Person> findPersons() {
        return execute(new DaoCommand<Collection<Person>>() {
            @Override
            public Collection<Person> execute(ObjectContentManager contentManager) {
                return contentManager.getObjects("//persons/*", Query.XPATH);
            }
        });
    }

}
