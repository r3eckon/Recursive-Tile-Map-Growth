/**
   Floor based scaling, works by using difference between current floor and origin floor
    then processes the bval using the modified scaling factor. Can make levels change as
    they go down floors such as increase or decrease branching, rooms, stairs, models,
    connections or influence any other parameter of this kind.
 */
public class PScale {

    public static final byte SCALEMODE_NOSCALE = 0;//No scaling enabled
    public static final byte SCALEMODE_DOWN = 1;//Scale higher the lower you go
    public static final byte SCALEMODE_UP = 2;//Scale higher the higher you go
    public static final byte SCALEMODE_MIDOUT = 3;//Scale higher the further from the origin you go
    public static final byte SCALEMODE_OUTMID = 4;//Scale higher the closer from the origin you go

    float bval,tval,eval,rval,sval,mval,cval;//Scaling factor for each generation float setting, per floor difference.
    byte mode;

    public PScale(float bv,float tv,float ev,float rv,float sv,float mv,float cv, byte m){
        this.bval =bv;
        this.eval =ev;
        this.tval =tv;
        this.rval =rv;
        this.sval =sv;
        this.mval =mv;
        this.cval =cv;
        this.mode=m;
    }

    public static float Process(byte scalingMode, float toScale, float val, int originFlr, int currentFlr, int levelFloors){


        switch (scalingMode){

            case SCALEMODE_NOSCALE:
                return val;

            case SCALEMODE_DOWN:
                return (float)Math.pow(toScale, Math.max(originFlr-currentFlr, 0))*val;

            case SCALEMODE_UP:
                return (float)Math.pow(toScale, Math.max(currentFlr-originFlr, 0))*val;

            case SCALEMODE_MIDOUT:
                return (float)Math.pow(toScale, Math.abs(originFlr-currentFlr))*val;

            case SCALEMODE_OUTMID:
                return (float)Math.pow(toScale, (levelFloors/2)-Math.abs(originFlr-currentFlr))*val;

            default:
                return val;
        }
    }

}