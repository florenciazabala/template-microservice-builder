package com.bancogalicia.templatemicroservicegenerator.config;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class MongoDbConfing extends AbstractMongoClientConfiguration {

    @Autowired
    Environment env;

    private static final String MONGO_DB_USERNAME = "mongodb.user";

    private static final String MONGO_DB_PASSWORD = "mongodb.pass";

    private static final String MONGO_DB_HOST = "mongodb.host";

    private static final String MONGO_DB_DATABASE = "mongodb.database";

    private static final String MONGO_DB_REPLICA = "mongodb.replica";

    private static final String MONGO_DB_AUTHSOURCE = "mongodb.authSource";

    @Override
    protected String getDatabaseName() {
        return env.getProperty(MONGO_DB_DATABASE);
    }

    @Bean
    public MongoClient mongoClient() {
        String options = "";
        String credentials = "";

        if(env.getProperty(MONGO_DB_USERNAME, "").length() > 0 &&
                env.getProperty(MONGO_DB_PASSWORD, "").length() > 0) {

            credentials = String.format("%s:%s@", env.getProperty(MONGO_DB_USERNAME), env.getProperty(MONGO_DB_PASSWORD));
        }
        if(env.getProperty(MONGO_DB_REPLICA, "").length() > 0) {
            options += String.format("replicaSet=%s&", env.getProperty(MONGO_DB_REPLICA));
        }
        if(env.getProperty(MONGO_DB_AUTHSOURCE, "").length() > 0) {
            options += String.format("authSource=%s&", env.getProperty(MONGO_DB_AUTHSOURCE));
        }

        /*final String uri = String.format("mongodb://%s%s/%s?%sssl=true",
                credentials,
                env.getProperty(MONGO_DB_HOST),
                env.getProperty(MONGO_DB_DATABASE),
                options);*/
        final String uri = String.format("mongodb+srv://%scluster0.e1yh1xw.mongodb.net/?retryWrites=true&w=majority",
                credentials);
        System.out.println(uri);

        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) {
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return super.mongoTemplate(databaseFactory, converter);
    }

    /*String connectionString = "mongodb+srv://l1004064:<password>@cluster0.e1yh1xw.mongodb.net/?retryWrites=true&w=majority";
    ServerApi serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build();
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .serverApi(serverApi)
            .build();
    // Create a new client and connect to the server
        try (
    MongoClient mongoClient = MongoClients.create(settings)) {
        try {
            // Send a ping to confirm a successful connection
            MongoDatabase database = mongoClient.getDatabase("admin");
            database.runCommand(new Document("ping", 1));
            System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }*/
}
