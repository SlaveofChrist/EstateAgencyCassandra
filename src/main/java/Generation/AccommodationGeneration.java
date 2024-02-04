package Generation;

import com.datastax.oss.driver.api.core.CqlSession;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import entity.Accommodation;
import repository.AccommodationRepository;
import repository.CassandraConnection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class AccommodationGeneration {

    private final static String accommodationsFilePath = "data/accommodations.csv";

    public static void generateAndSave(){
        List<Accommodation> accommodations = new ArrayList<>();
        try{
            Reader reader = new BufferedReader(new FileReader(accommodationsFilePath));
            CsvToBean<Accommodation> csvReader = new CsvToBeanBuilder<Accommodation>(reader)
                    .withType(Accommodation.class)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .build();
            accommodations = csvReader.parse();
        }catch (FileNotFoundException e){
            System.out.println("File not found: " + accommodationsFilePath);
        }

        CassandraConnection connection = new CassandraConnection();
        connection.connect("127.0.0.1", 9042, "cassandra");
        CqlSession session = connection.getSession();
        AccommodationRepository accommodationRepository = new AccommodationRepository(session);
        accommodationRepository.createTable();

        accommodationRepository.createTable();
        for (Accommodation accommodation : accommodations){
            accommodationRepository.insertOrUpdateAccommodation(accommodation);
        }
        connection.close();
    }

}
