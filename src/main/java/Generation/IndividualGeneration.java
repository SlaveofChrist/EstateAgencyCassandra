package Generation;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import entity.Accommodation;
import entity.Individual;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

public class IndividualGeneration {

    private final String individualsFilePath = "data/individual.csv";

    public IndividualGeneration() {
        try {
            Reader reader = new BufferedReader(new FileReader(individualsFilePath));
            CsvToBean<Individual> csvReader = new CsvToBeanBuilder<Individual>(reader)
                    .withType(Individual.class)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .build();
            List<Individual> individuals = csvReader.parse();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + individualsFilePath);
        }
    }
}
