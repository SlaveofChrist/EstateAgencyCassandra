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
import entity.Accommodation;
import entity.AccommodationGoal;
import entity.AccommodationState;
import entity.AccommodationType;

import javax.annotation.Nonnull;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccommodationRepository extends Repository {

    private static final String TABLE_NAME = "accommodation";

    public AccommodationRepository(CqlSession session) {
        super();
        this.session = session;
    }

    public void createTable() {
        createTable(null);
    }

    public void createTable(String keyspace) {
        CreateTable createTable = SchemaBuilder.createTable(TABLE_NAME).ifNotExists()
                .withPartitionKey("address_city", DataTypes.TEXT)
                .withClusteringColumn("goal", DataTypes.TEXT)
                .withClusteringColumn("type", DataTypes.TEXT)
                .withClusteringColumn("price", DataTypes.DOUBLE)
                .withClusteringColumn("availability_date", DataTypes.TIMESTAMP)
                .withClusteringColumn("roomNumber", DataTypes.INT)
                .withClusteringColumn("floorSpace", DataTypes.INT)
                .withColumn("code", DataTypes.TEXT)
                .withColumn("number", DataTypes.INT)
                .withColumn("state", DataTypes.TEXT)
                // Address
                .withColumn("address_street_number", DataTypes.INT)
                .withColumn("address_street", DataTypes.TEXT)
                .withColumn("address_postal_code", DataTypes.INT)
                .withColumn("address_country", DataTypes.TEXT);

        executeStatement(createTable.build(), keyspace);

        String secondaryIndexQuery = "CREATE INDEX accommodation_code_index ON " +
                TABLE_NAME +
                " (code);";
        session.execute(secondaryIndexQuery);
    }

    public String insertOrUpdateAccommodation(Accommodation accommodation) {
        return insertOrUpdateAccommodation(accommodation, null);
    }

    public String insertOrUpdateAccommodation(Accommodation accommodation, String keyspace) {

        if (accommodation.getCode().isEmpty()){
            Random rand = new Random();
            long time = new Timestamp(System.currentTimeMillis()).getTime();
            String accommodationCode = "acc-" +
                    time +
                    "-" +
                    rand.nextInt(1000);
            accommodation.setCode(accommodationCode);
        }

        RegularInsert insertInto = QueryBuilder.insertInto(TABLE_NAME)
                .value("address_city", QueryBuilder.bindMarker())
                .value("goal", QueryBuilder.bindMarker())
                .value("type", QueryBuilder.bindMarker())
                .value("price", QueryBuilder.bindMarker())
                .value("availability_date", QueryBuilder.bindMarker())
                .value("roomNumber", QueryBuilder.bindMarker())
                .value("floorSpace", QueryBuilder.bindMarker())
                .value("code", QueryBuilder.bindMarker())
                .value("number", QueryBuilder.bindMarker())
                .value("state", QueryBuilder.bindMarker())
                .value("address_street_number", QueryBuilder.bindMarker())
                .value("address_street", QueryBuilder.bindMarker())
                .value("address_postal_code", QueryBuilder.bindMarker())
                .value("address_country", QueryBuilder.bindMarker());

        SimpleStatement insertStatement = insertInto.build();

        if (keyspace != null) {
            insertStatement = insertStatement.setKeyspace(keyspace);
        }

        PreparedStatement preparedStatement = session.prepare(insertStatement);

        BoundStatement statement = preparedStatement.bind()
                .setString(0, accommodation.getAddress().getCity())
                .setString(1, accommodation.getGoal().name())
                .setString(2, accommodation.getType().name())
                .setDouble(3, accommodation.getPrice())
                .setInstant(4, accommodation.getAvailabilityDate())
                .setInt(5, accommodation.getRoomNumber())
                .setInt(6, accommodation.getFloorSpace())
                .setString(7, accommodation.getCode())
                .setInt(8, accommodation.getNumber())
                .setString(9, accommodation.getState().name())
                .setInt(10, accommodation.getAddress().getStreetNumber())
                .setString(11, accommodation.getAddress().getStreet())
                .setString(12, accommodation.getAddress().getPostalCode())
                .setString(13, accommodation.getAddress().getCountry());

        session.execute(statement);

        return accommodation.getCode();
    }

    public void deleteAccommodationByCode(String code){
        deleteAccommodationByCode(null, code);
    }

    public void deleteAccommodationByCode(String keyspace, String code){
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

    public List<Accommodation> selectBy(String keyspace, @Nonnull String address_city, String goal, String type, float price, Instant availability_date, int roomNumber, int floorSpace, Direction priceDirection, Direction roomNumberDirection, Direction floorSpaceDirection) {
        Select select = QueryBuilder.selectFrom(TABLE_NAME).all()
                .whereColumn("address_city").isEqualTo(QueryBuilder.bindMarker());
        if (!goal.isEmpty()){
            select = select.whereColumn("goal").isEqualTo(QueryBuilder.bindMarker());
            if (!type.isEmpty()){
                select = select.whereColumn("type").isEqualTo(QueryBuilder.bindMarker());
                if(price <= 0.0){
                    if (priceDirection.equals(Direction.LESS_THAN))
                        select = select.whereColumn("price").isLessThan(QueryBuilder.bindMarker());
                    else if (priceDirection.equals(Direction.LESS_THAN_OR_EQUAL))
                        select = select.whereColumn("price").isLessThanOrEqualTo(QueryBuilder.bindMarker());
                    else if (priceDirection.equals(Direction.EQUAL))
                        select = select.whereColumn("price").isEqualTo(QueryBuilder.bindMarker());
                    else if (priceDirection.equals(Direction.GREATER_THAN_OR_EQUAL))
                        select = select.whereColumn("price").isGreaterThanOrEqualTo(QueryBuilder.bindMarker());
                    else
                        select = select.whereColumn("price").isGreaterThan(QueryBuilder.bindMarker());
                    if (availability_date != null){
                        select = select.whereColumn("availability_date").isLessThan(QueryBuilder.bindMarker());
                        if (roomNumber <= 0){
                            if (priceDirection.equals(Direction.LESS_THAN))
                                select = select.whereColumn("roomNumber").isLessThan(QueryBuilder.bindMarker());
                            else if (priceDirection.equals(Direction.LESS_THAN_OR_EQUAL))
                                select = select.whereColumn("roomNumber").isLessThanOrEqualTo(QueryBuilder.bindMarker());
                            else if (priceDirection.equals(Direction.EQUAL))
                                select = select.whereColumn("roomNumber").isEqualTo(QueryBuilder.bindMarker());
                            else if (priceDirection.equals(Direction.GREATER_THAN_OR_EQUAL))
                                select = select.whereColumn("roomNumber").isGreaterThanOrEqualTo(QueryBuilder.bindMarker());
                            else
                                select = select.whereColumn("roomNumber").isGreaterThan(QueryBuilder.bindMarker());
                            if (floorSpace <= 0){
                                if (priceDirection.equals(Direction.LESS_THAN))
                                    select = select.whereColumn("floorSpace").isLessThan(QueryBuilder.bindMarker());
                                else if (priceDirection.equals(Direction.LESS_THAN_OR_EQUAL))
                                    select = select.whereColumn("floorSpace").isLessThanOrEqualTo(QueryBuilder.bindMarker());
                                else if (priceDirection.equals(Direction.EQUAL))
                                    select = select.whereColumn("floorSpace").isEqualTo(QueryBuilder.bindMarker());
                                else if (priceDirection.equals(Direction.GREATER_THAN_OR_EQUAL))
                                    select = select.whereColumn("floorSpace").isGreaterThanOrEqualTo(QueryBuilder.bindMarker());
                                else
                                    select = select.whereColumn("floorSpace").isGreaterThan(QueryBuilder.bindMarker());
                            }
                        }
                    }
                }
            }
        }

        SimpleStatement selectStatement = select.build();
        if (keyspace != null) {
            selectStatement = selectStatement.setKeyspace(keyspace);
        }
        PreparedStatement preparedStatement = session.prepare(selectStatement);
        BoundStatement statement = preparedStatement.bind().setString(0, address_city);
        if (!goal.isEmpty()){
            statement = statement.setString(1, goal);
            if (!type.isEmpty()){
                statement = statement.setString(2, type);
                if(price <= 0.0){
                    statement = statement.setFloat(3, price);
                    if (availability_date != null){
                        statement = statement.setInstant(4, availability_date);
                        if (roomNumber <= 0){
                            statement = statement.setInt(5, roomNumber);
                            if (floorSpace <= 0){
                                statement = statement.setInt(6, floorSpace);
                            }
                        }
                    }
                }
            }
        }
        ResultSet resultSet = session.execute(statement);

        List<Accommodation> result = new ArrayList<>();
        resultSet.forEach(x -> result.add(
                new Accommodation(
                        x.getString("code"),
                        x.getInt("number"),
                        AccommodationType.valueOf(x.getString("type")),
                        AccommodationGoal.valueOf(x.getString("goal")),
                        x.getInt("room_number"),
                        x.getInt("floor_space"),
                        AccommodationState.valueOf(x.getString("state")),
                        x.getDouble("price"),
                        x.getInstant("availability_date"),
                        x.getInt("address_street_number"),
                        x.getString("address_street"),
                        x.getString("address_city"),
                        x.getString("address_postal_code"),
                        x.getString("address_country")
                )
        ));

        return result;
    }

    public Accommodation selectByCode(String keyspace, String code) {
        Select select = QueryBuilder.selectFrom(TABLE_NAME).all()
                .whereColumn("code").isEqualTo(QueryBuilder.bindMarker());

        SimpleStatement selectStatement = select.build();
        if (keyspace != null) {
            selectStatement = selectStatement.setKeyspace(keyspace);
        }
        PreparedStatement preparedStatement = session.prepare(selectStatement);
        BoundStatement statement = preparedStatement.bind().setString(0, code);
        ResultSet resultSet = session.execute(statement);

        List<Accommodation> result = new ArrayList<>();

        resultSet.forEach(x -> result.add(
                new Accommodation(
                        x.getString("code"),
                        x.getInt("number"),
                        AccommodationType.valueOf(x.getString("type")),
                        AccommodationGoal.valueOf(x.getString("goal")),
                        x.getInt("room_number"),
                        x.getInt("floor_space"),
                        AccommodationState.valueOf(x.getString("state")),
                        x.getDouble("price"),
                        x.getInstant("availability_date"),
                        x.getInt("address_street_number"),
                        x.getString("address_street"),
                        x.getString("address_city"),
                        x.getString("address_postal_code"),
                        x.getString("address_country")
                )
        ));

        return result.get(0);
    }

    public List<Accommodation> selectAll() {
        return selectAll(null);
    }

    public List<Accommodation> selectAll(String keyspace) {
        Select select = QueryBuilder.selectFrom(TABLE_NAME).all();

        ResultSet resultSet = executeStatement(select.build(), keyspace);

        List<Accommodation> result = new ArrayList<>();

        resultSet.forEach(x -> result.add(
                new Accommodation(
                        x.getString("code"),
                        x.getInt("number"),
                        AccommodationType.valueOf(x.getString("type")),
                        AccommodationGoal.valueOf(x.getString("goal")),
                        x.getInt("room_number"),
                        x.getInt("floor_space"),
                        AccommodationState.valueOf(x.getString("state")),
                        x.getDouble("price"),
                        x.getInstant("availability_date"),
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
