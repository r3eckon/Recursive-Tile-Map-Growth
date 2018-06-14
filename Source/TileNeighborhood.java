public class TileNeighborhood {

    public TileType north;
    public TileType northeast;
    public TileType northwest;

    public TileType south;
    public TileType southeast;
    public TileType southwest;

    public TileType center;
    public TileType east;
    public TileType west;

    public TileType above;
    public TileType below;

    public TileNeighborhood(){
        north=TileType.Empty;
        northeast=TileType.Empty;
        northwest=TileType.Empty;

        south=TileType.Empty;
        southeast=TileType.Empty;
        southwest=TileType.Empty;

        center=TileType.Empty;
        east=TileType.Empty;
        west=TileType.Empty;

        above=TileType.Empty;
        below=TileType.Empty;
    }
}