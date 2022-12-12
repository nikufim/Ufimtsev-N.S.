package data;

import java.util.ArrayList;
import java.util.List;

public class School {
    String id;
    String district;
    String school;
    String county;
    String grades;
    double students;
    double teachers;
    double calworks;
    double lunch;
    double computer;
    double expenditure;
    double income;
    double english;
    double read;
    double math;

    public double tryParseDouble(String value, int defaultVal) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
    public School(String[] data){
        this.id = data[0];
        this.district = data[1];
        this.school = data[2];
        this.county = data[3];
        this.grades = data[4];

        this.students = tryParseDouble(data[5], 0);
        this.teachers = tryParseDouble(data[6], 0);
        this.calworks = tryParseDouble(data[7], 0);

        this.lunch = tryParseDouble(data[8], 0);
        this.computer = tryParseDouble(data[9], 0);
        this.expenditure = tryParseDouble(data[10], 0);
        this.income = tryParseDouble(data[11], 0);

        this.english = tryParseDouble(data[12], 0);
        this.read = tryParseDouble(data[13], 0);
        this.math = tryParseDouble(data[14], 0);
    }

    public String getData(){
        String s = "";
        s = getString(s, Double.parseDouble(id.replace("\"", "")), district + ",", school + ",", county + ",", grades + ",", students, teachers);
        s = getString(s, calworks, lunch + ",", computer + ",", expenditure + ",", income + ",", english, read);
        s += math;
        return s;
    }

    private String getString(String s, double id, String s2, String s3, String s4, String s5, double students, double teachers) {
        s += id + ",";
        s += s2;
        s += s3;
        s += s4;
        s += s5;
        s += students + ",";
        s += teachers + ",";
        return s;
    }

    public Object[] getArray(){
        List<Object> resList = new ArrayList<>();
        resList.add(id);
        resList.add(district);
        resList.add(school);
        resList.add(county);
        resList.add(grades);
        resList.add(students);
        resList.add(teachers);
        resList.add(calworks);
        resList.add(lunch);
        resList.add(computer);
        resList.add(expenditure);
        resList.add(income);
        resList.add(english);
        resList.add(read);
        resList.add(math);

        return resList.toArray();
    }
}
