public class Task implements Comparable<Task> {
    public int ID;
    public int start;
    public int deadline;
    public int duration;
    public int compNum;
    public int printCount;
    public Task() {
        this.ID = 0;
        this.start = 0;
        this.deadline = 0;
        this.duration = 0;
        this.compNum = 1;
        this.printCount = 0;
    }

    /**
     * create and initialize Task and its components
     */
    public Task(int ID, int start, int deadline, int duration, int compNum) {
        this.ID = ID;
        this.start = start;
        this.deadline = deadline;
        this.duration = duration;
        this.compNum = compNum;
        this.printCount = duration;
    }

    public String toString() {
        return "Task " + ID;
    }

    public String toStringL() {
        return "Task " + ID + "[" + start + "-" + deadline + "] " + duration;
    }


    /**
     * determine the priority to be used and sort tasks accordingly
     * @param t2 Task being used for comparison
     * @return the difference between the specified elements of the two tasks
     */
    public int compareTo(Task t2) {
        if (compNum == 1) {
            // priority deadline
            return deadline-t2.deadline;
        } else if (compNum == 2) {
            // priority start time, deadline if tied
            return (deadline-t2.deadline) + ((start-t2.start) * 100);
        } else {
            // priority duration, deadline if tied
            return (deadline-t2.deadline) + ((duration-t2.duration) * 100);
        }
    }
}
