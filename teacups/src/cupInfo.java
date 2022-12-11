import static java.lang.Math.min;

public class cupInfo {
    int maxValue;
    String maxSetList;
    public cupInfo(int maxValue, String maxSetList){
        this.maxValue = maxValue;
        this.maxSetList = maxSetList;
    }
    public static void main(String[] args) {
        teacups helperNew = new teacups();
        helperNew.helper();
    }
}

