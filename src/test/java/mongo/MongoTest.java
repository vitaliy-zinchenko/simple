package mongo;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.BulkWriteOperation;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteResult;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;

import static com.mongodb.QueryOperators.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.flapdoodle.embed.mongo.distribution.Version;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by zinchenko on 08.11.14.
 */
public class MongoTest {

    public static final String MORPHIA_BEAN = "morphia_bean";
    private Datastore datastore;

    private String collectionName = "collection";
    private String dbName;

    private static MongodForTestsFactory testsFactory;

    private DB db;

    private MongoClient mongo;

    @BeforeClass
    public static void setMongoDB() throws IOException {
        System.setProperty("DEBUG.MONGO", "true");
        System.setProperty("DB.TRACE", "true");

        testsFactory = MongodForTestsFactory.with(Version.Main.PRODUCTION);
    }

    @AfterClass
    public static void tearDownMongoDB() throws Exception {
        testsFactory.shutdown();
    }

    @Before
    public void setUpMongoDB() throws Exception {
        mongo = testsFactory.newMongo();
        dbName = UUID.randomUUID().toString();
        db = mongo.getDB(dbName);
    }

    private void p(String s) {
        System.out.println(s);
    }

    private DBCollection getCollection() {
        return db.getCollection(collectionName);
    }

    private DBCollection getCollection(String name) {
        return db.getCollection(name);
    }

    private void prepare() {
        DBCollection c = getCollection();

        c.insert(new BasicDBObject().append("name", "n_1").append("age", 10));
        c.insert(new BasicDBObject().append("name", "n_2").append("age", 11));
        c.insert(new BasicDBObject().append("name", "n_3").append("age", 23));
        c.insert(new BasicDBObject().append("name", "n_4").append("age", 62));
        c.insert(new BasicDBObject().append("name", "n_5").append("age", 15));
        c.insert(new BasicDBObject().append("name", "n_6").append("age", 16));
        c.insert(new BasicDBObject().append("name", "n_7").append("age", 49));
        c.insert(new BasicDBObject().append("name", "n_8").append("age", 61));
        c.insert(new BasicDBObject().append("name", "n_9").append("age", 46));
        c.insert(new BasicDBObject().append("name", "n_10").append("age", 26));
    }

    @Test
    public void testInsert() {
        prepare();
        {
            DBCollection c = getCollection();

            DBObject dbObject = new BasicDBObject("name", "nameValue");
            p("inserting");
            WriteResult result = c.insert(dbObject);
            p("end inserting");
            p("count");
            assertEquals(11, c.count());
            p("end count");
        }

        {
            DBCollection c = getCollection();
            DBObject object = new BasicDBObject("name", "nameValue");
            p("find one");
            DBObject result = c.findOne(object);
            p("end find one");

            assertNotNull(result);
            assertEquals("nameValue", result.get("name"));
        }

        {
            DBCollection collection = getCollection();
            DBObject query = new BasicDBObject("name", "nameValue");
            try(Cursor cursor = collection.find(query)) {
                int i = 0;
                while (cursor.hasNext()) {
                    DBObject dbObject = cursor.next();
                    assertNotNull(dbObject);
                    i++;
                }
                assertEquals(1, i);
            }
        }
    }

    @Test
    public void restrictions() {
        prepare();
        {
            DBCollection c = getCollection();
            DBObject query = new BasicDBObject("age", new BasicDBObject(GT, 46));
            int count = c.find(query).count();
            assertEquals(3, count);
        }

        {
            DBCollection c = getCollection();
            DBObject query = new BasicDBObject("age", new BasicDBObject(GT, 46))
                    .append("name", new BasicDBObject(NE, "n_8"));
            int count = c.find(query).count();
            assertEquals(2, count);
        }

        {
            DBCollection c = getCollection();
            DBObject query = new BasicDBObject("age", new BasicDBObject(GT, 46).append(LT, 62))
                    .append("name", new BasicDBObject(NE, "n_8"));
            int count = c.find(query).count();
            assertEquals(1, count);
        }
    }

    @Test
    public void queryBuilder() {
        prepare();
        {
            DBCollection c = getCollection();
            DBObject query = QueryBuilder.start().put("age").greaterThan(46).get();
            int count = c.find(query).count();
            assertEquals(3, count);
        }

        {
            DBCollection c = getCollection();
            DBObject query = QueryBuilder.start().put("age").greaterThan(46)
                    .put("name").notEquals("n_8").get();
            int count = c.find(query).count();
            assertEquals(2, count);
        }

        {
            DBCollection c = getCollection();
            DBObject query = QueryBuilder.start().put("age").greaterThan(46).lessThan(62)
                    .put("name").notEquals("n_8").get();
            int count = c.find(query).count();
            assertEquals(1, count);
        }

    }

    @Test
    public void in() {
        prepare();
        {
            DBCollection collection = getCollection();
            DBObject query = new BasicDBObject("age", new BasicDBObject("$in", new Long[]{10L, 11L}));
            int count = collection.find(query).count();
            assertEquals(2, count);
        }

        {
            DBCollection collection = getCollection();
            DBObject query = QueryBuilder.start().put("age").in(new Long[]{10L, 11L}).get();
            int count = collection.find(query).count();
            assertEquals(2, count);
        }

        {
            DBCollection collection = getCollection();
            List list = new ArrayList();
            list.add(10L);
            list.add(11L);
            DBObject query = QueryBuilder.start().put("age").in(list).get();
            int count = collection.find(query).count();
            assertEquals(2, count);
        }
    }

    @Test
    public void and() {
        
    }

    @Test
    public void bulk() {
        prepare();
        {
            p("bulk start");
            DBCollection collection = getCollection();
            BulkWriteOperation builder = collection.initializeOrderedBulkOperation();
            builder.insert(new BasicDBObject("_id", 1));
            builder.insert(new BasicDBObject("_id", 2));
            builder.insert(new BasicDBObject("_id", 3));

            builder.find(new BasicDBObject("_id", 1)).updateOne(new BasicDBObject("$set", new BasicDBObject("x", 2)));
            builder.find(new BasicDBObject("_id", 2)).removeOne();
            builder.find(new BasicDBObject("_id", 3)).replaceOne(new BasicDBObject("_id", 3).append("x", 4));
            p("bulk end");

            p("execute");
            builder.execute();
            p("executed");
        }

    }

    @Test
    public void text() {
        {
            DBCollection c = getCollection();

            c.insert(BasicDBObjectBuilder.start("comment", "Qwer asd zxc").get());
            c.insert(BasicDBObjectBuilder.start("comment", "Fghj vbnm hjk").get());
            c.insert(BasicDBObjectBuilder.start("comment", "Qwer jkl bnm").get());
            c.insert(BasicDBObjectBuilder.start("comment", "Uiop asd sdf").get());
            c.insert(BasicDBObjectBuilder.start("comment", "Cvbnm asd sdf").get());

            c.createIndex(BasicDBObjectBuilder.start("comment", "text").get());

            DBCursor cursor = c.find(BasicDBObjectBuilder.start("$text",
                BasicDBObjectBuilder.start("$search", "asd").get()).get());

            assertEquals(3, cursor.count());

            List<DBObject> list = cursor.toArray();
            assertEquals("Qwer asd zxc", list.get(2).get("comment"));
            assertEquals("Uiop asd sdf", list.get(1).get("comment"));
            assertEquals("Cvbnm asd sdf", list.get(0).get("comment"));

            //~~~~~~~~~~~~~~

            cursor = c.find(BasicDBObjectBuilder.start("$text", BasicDBObjectBuilder.start("$search", "Qwer asd").get()).get());

            assertEquals(4, cursor.count());
        }
    }

    @Test
    public void index3() throws Exception {
        DBCollection c = getCollection();


        c.insert(BasicDBObjectBuilder.start("name", "name-1").append("age", 1).get());
        c.insert(BasicDBObjectBuilder.start("name", "name-2").append("age", 1).get());
        c.insert(BasicDBObjectBuilder.start("name", "name-3").append("age", 1).get());
        c.insert(BasicDBObjectBuilder.start("name", "name-4").append("age", 2).get());
        c.insert(BasicDBObjectBuilder.start("name", "name-5").append("age", 2).get());
        c.insert(BasicDBObjectBuilder.start("name", "name-6").append("age", 2).get());
        c.insert(BasicDBObjectBuilder.start("name", "name-7").append("age", 2).get());
        c.insert(BasicDBObjectBuilder.start("name", "name-8").append("age", 3).get());
        c.insert(BasicDBObjectBuilder.start("name", "name-9").append("age", 3).get());
        c.insert(BasicDBObjectBuilder.start("name", "name-10").append("age", 3).get());

        c.createIndex(BasicDBObjectBuilder.start("age", 1).append("name", 1).get());
//        c.createIndex(BasicDBObjectBuilder.start("age", 1).get());

        p(c.getIndexInfo());

//        DBObject explain = c.find(BasicDBObjectBuilder.start("name", "name-1").append("age", 1).get()).explain();
        DBCursor cursor = c.find(BasicDBObjectBuilder.start("age", 1).get(),
                BasicDBObjectBuilder.start("name", true).append("_id", false).get());

        p(cursor.toArray());
        p(cursor.explain());
    }

    private void p(DBObject dbObject) throws Exception {
        String test = dbObject.toString();
        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(test, Object.class);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
    }

    private void p(List<DBObject> dbObjects) throws Exception {
        for (DBObject dbObject: dbObjects) {
            String test = dbObject.toString();
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(test, Object.class);
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
        }
    }

    private void initMorphia() {
        Morphia morphia = new Morphia();
        datastore = morphia.createDatastore(mongo, dbName);
        morphia.map(MorphiaBean.class);
    }

    @Test
    public void morphia() throws Exception {
        initMorphia();

        MorphiaBean morphiaBean = new MorphiaBean();
        morphiaBean.setName("qqq");
        datastore.save(morphiaBean);

        assertEquals(1, getCollection(MORPHIA_BEAN).find(BasicDBObjectBuilder.start("name", "qqq").get()).count());

        List<MorphiaBean> l = datastore.find(MorphiaBean.class)
                .field("name").equal("qqq").asList();
        assertEquals(1, l.size());

    }

    @Test
    public void aggregate() throws Exception {
        DBCollection c = getCollection();

        c.insert(BasicDBObjectBuilder.start("name", "name-1")
                .append("type", "a").append("age", 1)
                .get());

        c.insert(BasicDBObjectBuilder.start("name", "name-1_1")
                .append("type", "a").append("age", 2)
                .get());

        c.insert(BasicDBObjectBuilder.start("name", "name-2")
                .append("type", "a").append("age", 4)
                .get());

        c.insert(BasicDBObjectBuilder.start("name", "name-3")
                .append("type", "b").append("age", 6)
                .get());

        c.insert(BasicDBObjectBuilder.start("name", "name-3_2")
                .append("type", "b").append("age", 1)
                .get());

        List<DBObject> pipeline = new ArrayList<>();

//        pipeline.add(BasicDBObjectBuilder.start("$match",
//                BasicDBObjectBuilder.start("age", new BasicDBObject("$gt", 1)).get()).get());

        pipeline.add(BasicDBObjectBuilder.start("$group",
                BasicDBObjectBuilder.start("_id", new BasicDBObject("type", "$type").append("age", "$age"))
                        .append("total", new BasicDBObject("$sum", 1)).get()).get());

        AggregationOutput output = c.aggregate(pipeline);

        for(DBObject o: output.results()) {
            p(o);
        }
    }

    @Test
    public void morphiaAggregate() throws Exception {
        initMorphia();

        DBCollection c = getCollection();

        datastore.save(new MorphiaBean("name-1", "a"));
        datastore.save(new MorphiaBean("name-2", "a"));
        datastore.save(new MorphiaBean("name-3", "b"));


//        List<MorphiaBean> l = datastore.createAggregation(MorphiaBean.class).group()

//        assertEquals(1, l.size());

    }

    @Test
    public void update_replace() {
        DBCollection c = getCollection();

        BasicDBObject toInsert = new BasicDBObject("name", "Qwc").append("email", "qwe@qwe.qwe");
        WriteResult wr = c.insert(toInsert);
        Object id = toInsert.get("_id");

        DBObject object = c.findOne(new BasicDBObject("_id", id));
        object.put("email", "new@qwe.qwe");

        c.update(new BasicDBObject("_id", id), object);

        DBObject result = c.findOne(new BasicDBObject("_id", id));

        assertEquals("new@qwe.qwe", result.get("email"));
    }

    @Test
    public void update_set() {
        DBCollection c = getCollection();

        BasicDBObject toInsert = new BasicDBObject("name", "Qwc").append("email", "qwe@qwe.qwe");
        WriteResult wr = c.insert(toInsert);
        Object id = toInsert.get("_id");

        c.update(new BasicDBObject("_id", id), new BasicDBObject("$set", new BasicDBObject("email", "new@qqq.qqq")));

        DBObject result = c.findOne(new BasicDBObject("_id", id));

        assertEquals("new@qqq.qqq", result.get("email"));
    }

    @Test
    public void update_addToArray() {
        DBCollection c = getCollection();

        BasicDBObject toInsert = new BasicDBObject("name", "Qwc").append("emails", new String[]{"q@q.q", "a@a.a"});
        WriteResult wr = c.insert(toInsert);
        Object id = toInsert.get("_id");

        c.update(new BasicDBObject("_id", id), new BasicDBObject("$push", new BasicDBObject("emails", "new@n.n")));

        DBObject result = c.findOne(new BasicDBObject("_id", id));

        assertEquals(3, ((List)result.get("emails")).size());
    }

}
