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
import dao.TransactionDao;
import entity.AccommodationGoal;
import entity.Transaction;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransactionRepository extends Repository {

    private static final String TABLE_NAME = "transaction";
    private final IndividualRepository individualRepository;
    private final AccommodationRepository accommodationRepository;

    public TransactionRepository(CqlSession session) {
        super();
        this.session = session;
        this.individualRepository = new IndividualRepository(session);
        this.accommodationRepository = new AccommodationRepository(session);
    }

    public void createTable() {
        createTable(null);
    }

    public void createTable(String keyspace) {
        CreateTable createTable = SchemaBuilder.createTable(TABLE_NAME).ifNotExists()
                .withPartitionKey("date", DataTypes.TEXT)
                .withClusteringColumn("type", DataTypes.TEXT)
                .withColumn("commission", DataTypes.FLOAT)
                .withColumn("price", DataTypes.FLOAT)
                .withColumn("code", DataTypes.TEXT)
                .withColumn("owner_code", DataTypes.TEXT)
                .withColumn("estate_agent_code", DataTypes.TEXT)
                .withColumn("customer_code", DataTypes.TEXT)
                .withColumn("accommodation_code", DataTypes.TEXT);

        executeStatement(createTable.build(), keyspace);

        String secondaryIndexQuery = "CREATE INDEX transaction_code_index ON " +
                TABLE_NAME +
                " (code);";
        session.execute(secondaryIndexQuery);
    }

    public String insertOrUpdateTransaction(Transaction transaction) {
        return insertOrUpdateTransaction(transaction, null);
    }

    public String insertOrUpdateTransaction(Transaction transaction, String keyspace) {

        TransactionDao transactionDao = transaction.to_dao();

        if (transactionDao.getCode().isEmpty()){
            Random rand = new Random();
            long time = new Timestamp(System.currentTimeMillis()).getTime();
            String accommodationCode = "trans-" +
                    time +
                    "-" +
                    rand.nextInt(1000);
            transactionDao.setCode(accommodationCode);
        }

        RegularInsert insertInto = QueryBuilder.insertInto(TABLE_NAME)
                .value("date", QueryBuilder.bindMarker())
                .value("type", QueryBuilder.bindMarker())
                .value("commission", QueryBuilder.bindMarker())
                .value("price", QueryBuilder.bindMarker())
                .value("owner_code", QueryBuilder.bindMarker())
                .value("estate_agent_code", QueryBuilder.bindMarker())
                .value("customer_code", QueryBuilder.bindMarker())
                .value("accommodation_code", QueryBuilder.bindMarker());

        SimpleStatement insertStatement = insertInto.build();

        if (keyspace != null) {
            insertStatement = insertStatement.setKeyspace(keyspace);
        }

        PreparedStatement preparedStatement = session.prepare(insertStatement);

        BoundStatement statement = preparedStatement.bind()
                .setInstant(0, transactionDao.getDate())
                .setString(1, transactionDao.getType().name())
                .setFloat(2, transactionDao.getCommission())
                .setFloat(3, transactionDao.getPrice())
                .setString(4, transactionDao.getOwner_code())
                .setString(5, transactionDao.getEstateAgent_code())
                .setString(6, transactionDao.getCustomer_code())
                .setString(7, transactionDao.getAccommodation_code());

        session.execute(statement);

        return transactionDao.getCode();
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

    public List<Transaction> selectByDateAndType(String keyspace, Instant date, AccommodationGoal type){

        Select select = QueryBuilder.selectFrom(TABLE_NAME).all()
                .whereColumn("date").isEqualTo(QueryBuilder.bindMarker());
        if (type != null){
            select = select.whereColumn("type").isEqualTo(QueryBuilder.bindMarker());
        }
        SimpleStatement selectStatement = select.build();

        if (keyspace != null) {
            selectStatement = selectStatement.setKeyspace(keyspace);
        }
        PreparedStatement preparedStatement = session.prepare(selectStatement);
        BoundStatement statement = preparedStatement.bind().setInstant(0, date);
        if (type != null){
            statement = statement.setString(1, type.name());
        }

        ResultSet resultSet = session.execute(statement);
        List<Transaction> result = new ArrayList<>();

        resultSet.forEach(x -> result.add(
                new Transaction(
                        x.getString("code"),
                        x.getInstant("date"),
                        x.getFloat("commission"),
                        x.getFloat("price"),
                        AccommodationGoal.valueOf(x.getString("type")),
                        individualRepository.selectByCode(keyspace, x.getString("customer_code")),
                        individualRepository.selectByCode(keyspace, x.getString("estate_agent_code")),
                        individualRepository.selectByCode(keyspace, x.getString("owner_code")),
                        accommodationRepository.selectByCode(keyspace, x.getString("accommodation_code"))
                )
        ));
        return result;
    }

    public Transaction selectByCode(String code){
        return selectByCode(null, code);
    }

    public Transaction selectByCode(String keyspace, String code){
        Select select = QueryBuilder.selectFrom(TABLE_NAME).all()
                .whereColumn("code").isEqualTo(QueryBuilder.bindMarker());

        SimpleStatement selectStatement = select.build();
        if (keyspace != null) {
            selectStatement = selectStatement.setKeyspace(keyspace);
        }
        PreparedStatement preparedStatement = session.prepare(selectStatement);

        BoundStatement statement = preparedStatement.bind().setString(0, code);

        ResultSet resultSet = session.execute(statement);

        List<Transaction> result = new ArrayList<>();

        resultSet.forEach(x -> result.add(
                new Transaction(
                        x.getString("code"),
                        x.getInstant("date"),
                        x.getFloat("commission"),
                        x.getFloat("price"),
                        AccommodationGoal.valueOf(x.getString("type")),
                        individualRepository.selectByCode(keyspace, x.getString("customer_code")),
                        individualRepository.selectByCode(keyspace, x.getString("estate_agent_code")),
                        individualRepository.selectByCode(keyspace, x.getString("owner_code")),
                        accommodationRepository.selectByCode(keyspace, x.getString("accommodation_code"))
                )
        ));
        return result.get(0);
    }

    public List<Transaction> selectAll() {
        return selectAll(null);
    }

    public List<Transaction> selectAll(String keyspace) {

        Select select = QueryBuilder.selectFrom(TABLE_NAME).all();
        ResultSet resultSet = executeStatement(select.build(), keyspace);
        List<Transaction> result = new ArrayList<>();

        resultSet.forEach(x -> result.add(
                new Transaction(
                        x.getString("code"),
                        x.getInstant("date"),
                        x.getFloat("commission"),
                        x.getFloat("price"),
                        AccommodationGoal.valueOf(x.getString("type")),
                        individualRepository.selectByCode(keyspace, x.getString("customer_code")),
                        individualRepository.selectByCode(keyspace, x.getString("estate_agent_code")),
                        individualRepository.selectByCode(keyspace, x.getString("owner_code")),
                        accommodationRepository.selectByCode(keyspace, x.getString("accommodation_code"))
                )
        ));

        return result;
    }

}
