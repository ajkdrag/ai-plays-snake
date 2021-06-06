package src.learner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QTable {
    private int numStates, numActions;
    private List<List<Double>> qTable;
    private static final String CSV_DELIM = ",";

    public QTable(int numStates, int numActions) {
        this.numStates = numStates;
        this.numActions = numActions;
        this.qTable = new ArrayList<>();
    }

    public void initQTable() {
        for (int i = 0; i < this.numStates; ++i) {
            List<Double> actions = new ArrayList<>();
            for (int j = 0; j < this.numActions; ++j) {
                actions.add(0.0);
            }
            this.qTable.add(actions);
        }
    }

    private String toCSVRow(List<Double> actionQVals) {
        return actionQVals.stream().map(String::valueOf).collect(Collectors.joining(CSV_DELIM));
    }

    public void saveQTableToCSV(String path) throws IOException {
        try (PrintWriter writer = new PrintWriter(path)) {
            this.qTable.stream().map(this::toCSVRow).forEach(writer::println);
        }
        ;
    }

    private List<Double> fromCSVRow(String row) {
        return Arrays.asList(row.split(CSV_DELIM)).stream().map(s -> Double.parseDouble(s))
                .collect(Collectors.toList());
    }

    public void loadQTableFromCSV(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            this.qTable = br.lines().map(this::fromCSVRow).collect(Collectors.toList());
        }
        ;
    }

    public void resetQTable() {
        for (int i = 0; i < this.numStates; ++i) {
            this.qTable.get(i).clear();
        }
    }

    public double getQValue(int idxState, int idxAction) {
        return this.qTable.get(idxState).get(idxAction);
    }
}