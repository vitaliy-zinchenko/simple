package mongo;

import com.mongodb.*;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;

import static com.mongodb.BasicDBObjectBuilder.start;
import static com.mongodb.QueryOperators.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

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
//        System.setProperty("DEBUG.MONGO", "true");
//        System.setProperty("DB.TRACE", "true");
//        testsFactory = MongodForTestsFactory.with(Version.Main.PRODUCTION);
    }

    @AfterClass
    public static void tearDownMongoDB() throws Exception {
//        testsFactory.shutdown();
    }

    @Before
    public void setUpMongoDB() throws Exception {
//        mongo = testsFactory.newMongo();
//        dbName = UUID.randomUUID().toString();
//        db = mongo.getDB(dbName);


        MongoClient mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDB("mongoTest");

        getCollection().remove(start().get());
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

            c.insert(start("comment", "Qwer asd zxc").get());
            c.insert(start("comment", "Fghj vbnm hjk").get());
            c.insert(start("comment", "Qwer jkl bnm").get());
            c.insert(start("comment", "Uiop asd sdf").get());
            c.insert(start("comment", "Cvbnm asd sdf").get());

            c.createIndex(start("comment", "text").get());

            DBCursor cursor = c.find(start("$text",
                    start("$search", "asd").get()).get());

            assertEquals(3, cursor.count());

            List<DBObject> list = cursor.toArray();
            assertEquals("Qwer asd zxc", list.get(2).get("comment"));
            assertEquals("Uiop asd sdf", list.get(1).get("comment"));
            assertEquals("Cvbnm asd sdf", list.get(0).get("comment"));

            //~~~~~~~~~~~~~~

            cursor = c.find(start("$text", start("$search", "Qwer asd").get()).get());

            assertEquals(4, cursor.count());
        }
    }

    @Test
    public void index3() throws Exception {
        DBCollection c = getCollection();


        c.insert(start("name", "name-1").append("age", 1).get());
        c.insert(start("name", "name-2").append("age", 1).get());
        c.insert(start("name", "name-3").append("age", 1).get());
        c.insert(start("name", "name-4").append("age", 2).get());
        c.insert(start("name", "name-5").append("age", 2).get());
        c.insert(start("name", "name-6").append("age", 2).get());
        c.insert(start("name", "name-7").append("age", 2).get());
        c.insert(start("name", "name-8").append("age", 3).get());
        c.insert(start("name", "name-9").append("age", 3).get());
        c.insert(start("name", "name-10").append("age", 3).get());

        c.createIndex(start("age", 1).append("name", 1).get());
//        c.createIndex(BasicDBObjectBuilder.start("age", 1).get());

        p(c.getIndexInfo());

//        DBObject explain = c.find(BasicDBObjectBuilder.start("name", "name-1").append("age", 1).get()).explain();
        DBCursor cursor = c.find(start("age", 1).get(),
                start("name", true).append("_id", false).get());

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

        assertEquals(1, getCollection(MORPHIA_BEAN).find(start("name", "qqq").get()).count());

        List<MorphiaBean> l = datastore.find(MorphiaBean.class)
                .field("name").equal("qqq").asList();
        assertEquals(1, l.size());

    }

    @Test
    public void aggregate() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "name-1")
                .append("type", "a").append("age", 1)
                .get());

        c.insert(start("name", "name-1_1")
                .append("type", "a").append("age", 2)
                .get());

        c.insert(start("name", "name-2")
                .append("type", "a").append("age", 4)
                .get());

        c.insert(start("name", "name-3")
                .append("type", "b").append("age", 6)
                .get());

        c.insert(start("name", "name-3_2")
                .append("type", "b").append("age", 1)
                .get());

        List<DBObject> pipeline = new ArrayList<>();

//        pipeline.add(BasicDBObjectBuilder.start("$match",
//                BasicDBObjectBuilder.start("age", new BasicDBObject("$gt", 1)).get()).get());

        pipeline.add(start("$group",
                start("_id", new BasicDBObject("type", "$type").append("age", "$age"))
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
<<<<<<< HEAD
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
=======
    public void range_0() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "name-1").append("age", 1).get());
        c.insert(start("name", "name-2").append("age", 2).get());
        c.insert(start("name", "name-3").append("age", 3).get());
        c.insert(start("name", "name-4").append("age", 4).get());

        List<DBObject> list = c.find(start("age", start("$gte", 2).append("$lte", 3).get()).get()).toArray();

        assertEquals(2, list.size());
    }

    @Test
    public void in_0() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "name-1").append("age", 1).get());
        c.insert(start("name", "name-2").append("age", 2).get());
        c.insert(start("name", "name-3").append("age", 3).get());
        c.insert(start("name", "name-4").append("age", 4).get());

        List<DBObject> list = getCollection()
                .find(
                        start("name",
                                start("$in", new String[]{"name-2", "name-4"}).get()
                        ).get()
                ).toArray();

        assertEquals(2, list.get(0).get("age"));
        assertEquals(4, list.get(1).get("age"));

    }

    @Test
    public void in_1() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "name-1").append("age", 1).append("tags", new String[]{"t1", "t2"}).get());
        c.insert(start("name", "name-2").append("age", 2).append("tags", new String[]{"t1", "t3"}).get());
        c.insert(start("name", "name-3").append("age", 3).append("tags", new String[]{"t2", "t1"}).get());
        c.insert(start("name", "name-4").append("age", 4).append("tags", new String[]{"t4", "t3"}).get());

        List<DBObject> list = getCollection()
                .find(
                        start("tags",
                                start("$in", new String[]{"t1"}).get()
                        ).get()
                ).toArray();

        assertEquals(3, list.size());

        assertEquals(1, list.get(0).get("age"));
        assertEquals(2, list.get(1).get("age"));
        assertEquals(3, list.get(2).get("age"));

    }

    @Test
    public void in_2_$elemMatch() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "name-1").append("age", 1).append("tags",
                new Object[]{new BasicDBObject("name", "t1").append("id", 11), new BasicDBObject("name", "t2").append("id", 12)}).get());
        c.insert(start("name", "name-2").append("age", 2).append("tags",
                new Object[]{new BasicDBObject("name", "t2").append("id", 11), new BasicDBObject("name", "t3").append("id", 13)}).get());
        c.insert(start("name", "name-3").append("age", 3).append("tags",
                new Object[]{new BasicDBObject("name", "t1").append("id", 11), new BasicDBObject("name", "t3").append("id", 13)}).get());
        c.insert(start("name", "name-4").append("age", 4).append("tags",
                new Object[]{new BasicDBObject("name", "t4").append("id", 13)}).get());

        List<DBObject> list = getCollection()
                .find(
                        start("tags",
                                start("$elemMatch",
                                        start("name",
                                                start("$in", new String[]{"t1"}).get()
                                        ).get()
                                ).get()
                        ).get()
                ).toArray();

        assertEquals(2, list.size());

        assertEquals(1, list.get(0).get("age"));
        assertEquals(3, list.get(1).get("age"));

    }

    @Test
    public void nin_0() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "name-1").append("age", 1).get());
        c.insert(start("name", "name-2").append("age", 2).get());
        c.insert(start("name", "name-3").append("age", 3).get());
        c.insert(start("name", "name-4").append("age", 4).get());

        List<DBObject> list = getCollection()
                .find(
                        start("name",
                                start("$nin", new String[]{"name-2", "name-4"}).get()
                        ).get()
                ).toArray();

        assertEquals(1, list.get(0).get("age"));
        assertEquals(3, list.get(1).get("age"));

    }

    @Test
    public void all_0() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "name-1").append("age", 1).append("tags", new String[]{"t1", "t2"}).get());
        c.insert(start("name", "name-2").append("age", 2).append("tags", new String[]{"t1", "t3"}).get());
        c.insert(start("name", "name-3").append("age", 3).append("tags", new String[]{"t2", "t1"}).get());
        c.insert(start("name", "name-4").append("age", 4).append("tags", new String[]{"t4", "t3"}).get());

        List<DBObject> list = getCollection()
                .find(
                        start("tags",
                                start("$all", new String[]{"t1", "t2"}).get()
                        ).get()
                ).toArray();

        assertEquals(1, list.get(0).get("age"));
        assertEquals(3, list.get(1).get("age"));

    }

    @Test
    public void regex_0() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "Aqw-1").append("age", 1).get());
        c.insert(start("name", "Qwe-2").append("age", 2).get());
        c.insert(start("name", "Zxc-3").append("age", 3).get());
        c.insert(start("name", "Sdf-4").append("age", 4).get());

        List<DBObject> list = getCollection().find(start("name", Pattern.compile(".qw.")).get()).toArray();

        assertEquals(1, list.size());

        assertEquals(1, list.get(0).get("age"));

    }

    @Test
    public void regex_$not_0() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "Aqw-1").append("age", 1).get());
        c.insert(start("name", "Qwe-2").append("age", 2).get());
        c.insert(start("name", "Zxc-3").append("age", 3).get());
        c.insert(start("name", "Sdf-4").append("age", 4).get());

        List<DBObject> list = getCollection().find(start("name",
                start("$not", Pattern.compile(".qw.")).get()
        ).get()).toArray();

        assertEquals(3, list.size());
    }

    @Test
    public void $exists_0() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "Aqw-1").append("age", 1).get());
        c.insert(start("name", "Qwe-2").append("value", "vvv").get());
        c.insert(start("name", "Zxc-3").append("age", 3).get());
        c.insert(start("name", "Sdf-4").append("age", 4).get());

        List<DBObject> list = getCollection().find(start("value",
                start("$exists", true).get()
        ).get()).toArray();

        assertEquals(1, list.size());
    }

    @Test
    public void $exists_1() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "Aqw-1").append("age", 1).get());
        c.insert(start("name", "Qwe-2").append("value", "vvv").get());
        c.insert(start("name", "Zxc-3").append("age", 3).get());
        c.insert(start("name", "Sdf-4").append("age", 4).get());

        List<DBObject> list = getCollection().find(start("value", null).get()).toArray();

        assertEquals(3, list.size());
    }

    @Test
    public void $exists_2() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "Aqw-1").append("age", 1).get());
        c.insert(start("name", "Qwe-2").append("value", "vvv").get());
        c.insert(start("name", "Zxc-3").append("age", 3).get());
        c.insert(start("name", "Sdf-4").append("age", 4).get());

        List<DBObject> list = getCollection().find(start("value",
                start("$ne", null).get()
            ).get()).toArray();

        assertEquals(1, list.size());
    }

    @Test
    public void array_0() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "n-1").append("array", new String[]{"qw", "as"}).get());
        c.insert(start("name", "n-2").append("array", new String[]{"sd", "xc"}).get());

        List<DBObject> list = getCollection().find(start("array.0", "qw").get()).toArray();

        assertEquals(1, list.size());
    }

    @Test
    public void o_2() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "n-1").append("o", start("k", "v").get()).get());

        List<DBObject> list = getCollection().find(start("o.k", "v").get()).toArray();

        assertEquals(1, list.size());
    }

    @Test
    public void js_0() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "n-1").append("age", 1).get());
        c.insert(start("name", "n-2").append("age",5).get());

        List<DBObject> list = getCollection().find(start("$where", "function() {return this.age > 3;}").get()).toArray();

        assertEquals(1, list.size());
    }

    @Test
    public void js_1() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "n-1").append("age", 1).get());
        c.insert(start("name", "n-2").append("age",5).get());

        List<DBObject> list = getCollection().find(start("$where", "this.age > 3").get()).toArray();

        assertEquals(1, list.size());
    }

    @Test
    public void js_2() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("name", "n-1").append("age", 1).get());
        c.insert(start("name", "n-2").append("age",5).get());

        String fn = "function() {" +
                    "   print('message from js');" +
                    "   return true;" +
                    "}";

        List<DBObject> list = getCollection().find(start("$where", fn).get()).toArray();
        assertEquals(2, list.size());

        List<DBObject> log = getCollection("temp_log").find(new BasicDBObject()).toArray();
    }


    @Test
    public void map_reduce_0() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("type", "t-1").append("price", 10).get());
        c.insert(start("type", "t-1").append("price", 15).get());
        c.insert(start("type", "t-2").append("price", 5).get());
        c.insert(start("type", "t-2").append("price", 1).get());

        String map = "function() {" +
                "   print('m~~~~~~~~~~~~');" +
                "   var n = this.type;" +
                "   emit(n, {'pr': this.price});" +
                "}";

        String reduce = "function(key, values) {" +
                "   print('r~~~~~~~~~~~~');" +
                "   var sum = 0;" +
                "   values.forEach(function(doc) {" +
                "       print(doc);" +
                "       sum += doc.pr;" +
                "   });" +
                "   return {sum: sum};" +
                "}";

        MapReduceCommand command = new MapReduceCommand(c, map, reduce, null, MapReduceCommand.OutputType.INLINE, null);
        MapReduceOutput output = getCollection().mapReduce(command);

        for(DBObject dbObject: output.results()) {
            System.out.println(dbObject);
        }
    }

    @Test
    public void distinct_0() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("type", "t-1").append("price", 10).get());
        c.insert(start("type", "t-1").append("price", 15).get());
        c.insert(start("type", "t-2").append("price", 5).get());
        c.insert(start("type", "t-2").append("price", 1).get());

        List list = c.distinct("type");

        System.out.println(list);
    }

    @Test
    public void distinct_2() throws Exception {
        DBCollection c = getCollection();

        c.insert(start("type", "t-1").append("tag", new String[]{"t1", "t2"}).append("price", 10).get());
        c.insert(start("type", "t-1").append("tag", new String[]{"t1", "t3"}).append("price", 15).get());
        c.insert(start("type", "t-2").append("tag", new String[]{"t24", "t23"}).append("price", 5).get());
        c.insert(start("type", "t-2").append("tag", new String[]{"t44", "t43"}).append("price", 1).get());

        List list = c.distinct("tag", new BasicDBObject("type", "t-1"));

        System.out.println(list);
    }

    @Test
    public void ref_() throws Exception {
        DBCollection user = getCollection("user");
        DBCollection role = getCollection("role");

        user.insert(new BasicDBObject("name", "name-1").append("role_id", 1));
        role.insert(new BasicDBObject("_id", 1).append("type", "admin"));

        DBRef dbRef = new DBRef(db, "role", 1);

//        List list =

//        System.out.println(list);
    }

>>>>>>> c

}
