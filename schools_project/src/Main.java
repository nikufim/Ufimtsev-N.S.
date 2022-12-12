import data.School;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {

        try {
            // establishing connection..
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir") + "/src/data/database.db");
            //conn.setAutoCommit(false);

            System.out.println("Connection established!");

            // adding DB if not exists
            // creating table if not exists
            PreparedStatement create = conn.prepareStatement("""
                    -- creating table if not exists
                    CREATE TABLE if not exists schools (
                        id TEXT,
                        district TEXT,
                    	school TEXT,
                    	county TEXT,
                    	grades TEXT,
                    	students REAL,
                    	teachers REAL,
                    	calworks REAL,
                        lunch REAL,
                    	computer REAL,
                    	expenditure REAL,
                    	income REAL,
                    	english REAL,
                    	read REAL,
                    	math REAL
                    );
                    """);

            var sycc = create.execute();
            System.out.println("Created database? " + sycc);

            // updates for all except SELECT, for SELECT - query
            // create.executeUpdate();
            // create.close();

            // open the CSV file
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/data/Школы.csv"));
            System.out.println(".csv opened to read!");

            // reading header and columns
            // pretty useless : we know them beforehand
            String header = reader.readLine();
            String[] columns = header.split(",");
            columns[0] = "id";

            // create a prepared statement for inserting the data
            String placeholders = String.join(",", Collections.nCopies(columns.length, "?"));
            String sql = String.format("INSERT INTO schools (%s) VALUES (%s)", String.join(",", columns), placeholders);
            PreparedStatement ps = conn.prepareStatement(sql);

            // creating school object and passing all strings to be parsed:
            // read the remaining lines of the CSV file and insert the data into the database
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                // parsing array to school obj
                School s = new School(values);

                Object[] arr = s.getArray();

                System.out.println("Got: " + s.getData());

                for (int i = 0; i < values.length; i++) {
                    if (i < 5) {
                        ps.setString(i + 1, (String) arr[i]);
                    } else {
                        ps.setDouble(i + 1, (Double) arr[i]);
                    }
                }

                // executing addition of new element
                var a = ps.executeUpdate();
                System.out.println("Result of adding new element, status: " + a);
            }
            System.out.println("Data from .csv added to .db successfully!");

            /*
            Задание №1
            Постройте график по среднему количеству студентов, в 10 различных странах, взять на свой выбор.

            Поправка: нет колонки "страна" (country), есть "область" (county), её и будем визуализировать.
             */
            PreparedStatement task1 = conn.prepareStatement("""
                    select county, avg(students)
                    from schools\s
                    group by county
                    limit 10
                    """);

            ResultSet r1 = task1.executeQuery();
            System.out.println("Задание №1");

            // getting data
            while (r1.next()) {
                String county = r1.getString("county");
                double avgStudents = r1.getDouble("avg(students)");
                System.out.println(county + ": " + String.format("%.2f", avgStudents));
            }

            /*
            Задание №2
            Выведите в консоль среднее количество расходов(expenditure) в
            Fresno, Contra Costa, El Dorado и Glenn, у которых расход больше 10
             */
            PreparedStatement task2 = conn.prepareStatement("""
                    select county, avg(expenditure)
                            from schools
                            where county in ('"Fresno"', '"Contra Costa"', '"El Dorado"', '"Glenn"')
                            and expenditure > 10
                            group by county
                    """);

            ResultSet r2 = task2.executeQuery();
            System.out.println("Задание №2");

            // getting data
            while (r2.next()) {
                String county = r2.getString("county");
                double avgStudents = r2.getDouble("avg(expenditure)");
                System.out.println(county + ": " + String.format("%.2f", avgStudents));
            }

            /*
            Задание №3
            Выведите в консоль учебное заведение,
            с количеством студентов равному от 5000 до 7500 и с 10000 до 11000,
            с самым высоким показателем по математике (math)
             */
            PreparedStatement task3 = conn.prepareStatement("""
                    select school
                    from schools
                    where (students BETWEEN 5000.0 and 7500.0) or (students BETWEEN 10000.0 and 11000.0)
                    order by math desc
                    limit 1
                    """);

            ResultSet r3 = task3.executeQuery();
            System.out.println("Задание №3");

            // getting data
            while (r3.next()) {
                String sch = r3.getString("school");
                System.out.println(sch);
            }


        } catch (SQLException e) {
            System.out.println("Error: " + e.getSQLState());
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("Can't open .csv file!");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Can't read line from .csv file!");
            System.out.println(e.getMessage());
        }

    }
}