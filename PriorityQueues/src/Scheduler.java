import java.util.ArrayList;

public class Scheduler {

    /**
     * create and print an orderly schedule of tasks based on various priorities
     * @param header summary of priority style and text file name
     * @param list list of tasks to be scheduled
     */
    public void makeSchedule(String header, ArrayList<Task> list){
        Heap<Task> heap = new Heap<>();
        for (int i = 0; i < list.size(); i++){
            // compare each new node to the root to find place
            heap.insert(list.get(i), heap.root);
        }

        // print and format schedule
        System.out.println(header);
        int time = 0;
        int i = 0;
        int timeOld;
        while (i <= heap.findMax(heap.root, 0)){
            timeOld = time;
            time = heap.search(i, heap.root, heap.root, time);
            if (time == timeOld){
                i++;
            }
        }
        //print summary of any lateness
        System.out.println("Tasks late: " + heap.lateCount + "   total late: " + heap.lateTime);
        System.out.println("");
    }
}
