class IgnoreNeighborhood{

    public boolean north;
    public boolean northeast;
    public boolean northwest;
    public boolean south;
    public boolean southeast;
    public boolean southwest;
    public boolean east;
    public boolean west;
    public boolean above;
    public boolean below;

    public IgnoreNeighborhood(){
        north=false;
        northeast=false;
        northwest=false;
        south=false;
        southeast=false;
        southwest=false;
        east=false;
        west=false;
        above=false;
        below=false;
    }

    //Ignores the previous tile so that corridor self avoidance can be implemented
    static IgnoreNeighborhood CorridorIgnore(Orientation currentDirection){
        IgnoreNeighborhood toret = new IgnoreNeighborhood();
        switch (currentDirection){
            case Northbound:toret.south=true;break;
            case Southbound:toret.north=true;break;
            case Westbound:toret.east=true;break;
            case Eastbound:toret.west=true;break;
        }

        return toret;
    }
}