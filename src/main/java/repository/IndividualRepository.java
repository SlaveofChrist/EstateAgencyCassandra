package repository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import entity.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IndividualRepository extends Repository {

    protected static final String TABLE_NAME = "individual";

    public IndividualRepository(CqlSession session) {
        super();
        this.session = session;
    }

    public void createTable() {
        createTable(null);
    }

    public void createTable(String keyspace) {
        CreateTable createTable = SchemaBuilder.createTable(TABLE_NAME).ifNotExists()
                .withPartitionKey("birthday", DataTypes.TIMESTAMP)
                .withColumn("name", DataTypes.TEXT)
                .withColumn("surname", DataTypes.TEXT)
                .withColumn("code", DataTypes.TEXT)
                // Address
                .withColumn("address_street_number", DataTypes.INT)
                .withColumn("address_street", DataTypes.TEXT)
                .withColumn("address_city", DataTypes.TEXT)
                .withColumn("address_postal_code", DataTypes.INT)
                .withColumn("address_country", DataTypes.TEXT);

        executeStatement(createTable.build(), keyspace);

        String secondaryIndexQuery = "CREATE INDEX individual_code_index ON " +
                TABLE_NAME +
                " (code);";
        session.execute(secondaryIndexQuery);
    }

    public String insertOrUpdateAccommodation(Individual individual) {
        return insertOrUpdateAccommodation(individual, null);
    }

    public String insertOrUpdateAccommodation(Individual individual, String keyspace) {

        if (individual.getCode().isEmpty()){
            Random rand = new Random();
            long time = new Timestamp(System.currentTimeMillis()).getTime();
            String accommodationCode = "ind-" +
                    time +
                    "-" +
                    rand.nextInt(1000);
            individual.setCode(accommodationCode);
        }

        RegularInsert insertInto = QueryBuilder.insertInto(TABLE_NAME)
                .value("birthday", QueryBuilder.bindMarker())
                .value("name", QueryBuilder.bindMarker())
                .value("surname", QueryBuilder.bindMarker())
                .value("code", QueryBuilder.bindMarker())
                .value("address_street_number", QueryBuilder.bindMarker())
                .value("address_street", QueryBuilder.bindMarker())
                .value("address_city", QueryBuilder.bindMarker())
                .value("address_postal_code", QueryBuilder.bindMarker())
                .value("address_country", QueryBuilder.bindMarker());

        SimpleStatement insertStatement = insertInto.build();

        if (keyspace != null) {
            insertStatement = insertStatement.setKeyspace(keyspace);
        }

        PreparedStatement preparedStatement = session.prepare(insertStatement);

        BoundStatement statement = preparedStatement.bind()
                .setInstant(0, individual.getBirthday())
                .setString(1, individual.getName())
                .setString(2, individual.getSurname())
                .setString(3, individual.getCode())
                .setInt(4, individual.getAddress().getStreetNumber())
                .setString(5, individual.getAddress().getStreet())
                .setString(6, individual.getAddress().getCity())
                .setString(7, individual.getAddress().getPostalCode())
                .setString(8, individual.getAddress().getCountry());

        session.execute(statement);

        return individual.getCode();
    }

    public void deleteIndividualByCode(String keyspace, String code){
        Delete delete = QueryBuilder.deleteFrom(TABLE_NAME)
                .whereColumn("code").isEqualTo(QueryBuilder.bindMarker());
        SimpleStatement deleteStatement = delete.build();
        if (keyspace != null) {
            deleteStatement = deleteStatement.setKeyspace(keyspace);
        }
        PreparedStatement preparedStatement = session.prepare(deleteStatement);

        BoundStatement statement = preparedStatement.bind().setString(0, code);
        ResultSet result = session.execute(statement);

    }

    public Individual selectByCode(String code){
        return selectByCode(null, code);
    }

    public Individual selectByCode(String keyspace, String code){
        Select select = QueryBuilder.selectFrom(TABLE_NAME).all()
                .whereColumn("code").isEqualTo(QueryBuilder.bindMarker());

        SimpleStatement selectStatement = select.build();
        if (keyspace != null) {
            selectStatement = selectStatement.setKeyspace(keyspace);
        }
        PreparedStatement preparedStatement = session.prepare(selectStatement);

        BoundStatement statement = preparedStatement.bind().setString(0, code);

        ResultSet resultSet = session.execute(statement);

        List<Individual> result = new ArrayList<>();

        resultSet.forEach(x -> result.add(
                new Individual(
                        x.getString("code"),
                        x.getString("name"),
                        x.getString("surname"),
                        x.getInstant("birthday"),
                        x.getInt("address_street_number"),
                        x.getString("address_street"),
                        x.getString("address_city"),
                        x.getString("address_postal_code"),
                        x.getString("address_country")
                )
        ));
        return result.get(0);
    }

    public List<Individual> selectAll() {
        return selectAll(null);
    }

    public List<Individual> selectAll(String keyspace) {
        Select select = QueryBuilder.selectFrom(TABLE_NAME).all();

        ResultSet resultSet = executeStatement(select.build(), keyspace);

        List<Individual> result = new ArrayList<>();

        resultSet.forEach(x -> result.add(
                new Individual(
                        x.getString("code"),
                        x.getString("name"),
                        x.getString("surname"),
                        x.getInstant("birthday"),
                        x.getInt("address_street_number"),
                        x.getString("address_street"),
                        x.getString("address_city"),
                        x.getString("address_postal_code"),
                        x.getString("address_country")
                )
        ));

        return result;
    }

}
