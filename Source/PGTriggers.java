public class PGTrigger {

    public static final short TYPE_NOP = 0;
    public static final short TYPE_ADDCORRIDOR = 1;
    public static final short TYPE_ADDRANDROOM = 2;
    public static final short TYPE_ADDRANDMODL = 3;

    Vector3f pos;
    Orientation orientation;
    short type;
    boolean iexec;

    public PGTrigger(Vector3f p, Orientation o, short t){
       pos=p;
       orientation=o;
       type=t;
       iexec=false;
    }

    public PGTrigger(Vector3f p, Orientation o, short t, boolean ie){
        pos=p;
        orientation=o;
        type=t;
        iexec=ie;
    }


}