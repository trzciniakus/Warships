import java.util.List;

public class Grid {

    private int size = 10;
    private List<Field> fields;




    class Field {
        private int x;
        private int y;
        private int shipOwner;
        private boolean revealed;

        public Field(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
