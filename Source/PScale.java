/**
   Floor based scaling, works by using difference between current floor and origin floor
    then processes the value using the modified scaling factor. Can make levels change as
    they go down floors such as increase or decrease branching, rooms, stairs, models,
    connections or influence any other parameter of this kind.
 */
public class PScale {

    public static final byte SCALEMODE_NOSCALE = 0;//No scaling enabled
    public static final byte SCALEMODE_DOWN = 1;//Scale higher the lower you go
    public static final byte SCALEMODE_UP = 2;//Scale higher the higher you go
    public static final byte SCALEMODE_MIDOUT = 3;//Scale higher the further from the origin you go
    public static final byte SCALEMODE_OUTMID = 4;//Scale higher the closer from the origin you go

    float value;//Scaling factor per floor difference
    byte mode;

    public PScale(float v, byte m){
        this.value=v;
        this.mode=m;
    }

    public static float Process(PScale scaling, float val, int originFlr, int currentFlr){
        switch (scaling.mode){

            case PScale.SCALEMODE_NOSCALE:
                return val;

            case PScale.SCALEMODE_DOWN:
                return (float)Math.pow(scaling.value, Math.max(originFlr-currentFlr, 0))*val;

            case PScale.SCALEMODE_UP:
                return (float)Math.pow(scaling.value, Math.max(currentFlr-originFlr, 0))*val;

            case PScale.SCALEMODE_MIDOUT:
                return (float)Math.pow(scaling.value, Math.abs(originFlr-currentFlr))*val;

            case PScale.SCALEMODE_OUTMID:
                return (float)Math.pow(scaling.value, 2-Math.abs(originFlr-currentFlr))*val;

            default:
                return val;
        }
    }

}