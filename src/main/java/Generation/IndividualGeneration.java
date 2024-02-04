package Generation;

import com.datastax.oss.driver.api.core.CqlSession;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import entity.Individual;
import repository.CassandraConnection;
import repository.IndividualRepository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class IndividualGeneration {

    private static final String individualsFilePath = "data/individual.csv";

    public static void generateAndSave() {
        List<Individual> individuals = new ArrayList<>();
        try {
            Reader reader = new BufferedReader(new FileReader(individualsFilePath));
            CsvToBean<Individual> csvReader = new CsvToBeanBuilder<Individual>(reader)
                    .withType(Individual.class)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .build();
            individuals = csvReader.parse();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + individualsFilePath);
        }
        CassandraConnection connection = new CassandraConnection();
        connection.connect("127.0.0.1", 9042, "cassandra");
        CqlSession session = connection.getSession();
        IndividualRepository individualRepository = new IndividualRepository(session);
        individualRepository.createTable();

        individualRepository.createTable();
        for (Individual individual : individuals){
            individualRepository.insertOrUpdateAccommodation(individual);
        }
        connection.close();
    }
}
