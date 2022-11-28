import java.io.File;
import java.util.*;
import java.util.Scanner;

public class TestSched {
    /**
     * insert node into the heap/tree and rebalance
     * @param filename file containing a list of tasks
     * @param task1 list to be populated with tasks from file, first priority style
     * @param task2 list to be populated with tasks from file, second priority style
     * @param task3 list to be populated with tasks from file, third priority style
     */
    public static void readTasks(String filename,
                          ArrayList<Task> task1, ArrayList<Task> task2,
                                 ArrayList<Task> task3) {
        try {
            // read in file and populate lists
            Scanner reader = new Scanner(new File(filename));
            int counter = 1;
            while (reader.hasNext()) {
                int startTime = reader.nextInt();
                int deadline = reader.nextInt();
                int duration = reader.nextInt();
                Task temp = new Task(counter, startTime, deadline, duration, 1 );
                task1.add(temp);
                Task temp2 = new Task(counter, startTime, deadline, duration, 2 );
                task2.add(temp2);
                Task temp3 = new Task(counter, startTime, deadline, duration, 3 );
                task3.add(temp3);
                counter ++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * manage the creation of three lists for each file, each
     * list with a different priority type
     */
    public static void main(String args[]) {
        String [] files = {"taskset1.txt","taskset2.txt","taskset3.txt","taskset4.txt","taskset5.txt" };
        for (String f : files) {
            Scheduler s = new Scheduler();
            Scheduler s1 = new Scheduler();
            Scheduler s2 = new Scheduler();
            ArrayList<Task> t1 = new ArrayList();    // elements are Task1
            ArrayList<Task> t2 = new ArrayList();    // elements are Task2
            ArrayList<Task> t3 = new ArrayList();    // elements are Task3
            readTasks(f, t1, t2, t3);
            s.makeSchedule("Deadline Priority "+ f, t1);
            s1.makeSchedule("StartTime Priority " + f, t2);
            s2.makeSchedule("Duration Priority " + f, t3);
       }
    }
}
