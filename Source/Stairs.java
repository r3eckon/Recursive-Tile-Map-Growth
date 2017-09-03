    //These algorithms are all needed for staircase placement.
	
	//This is the main step of the process, calling sub-methods
    public boolean placeStaircase(int x, int y,int l, int dl, boolean showoff ){


        //Check if placing staircase is possible
        if(x + 1 > width || x - 1 < 0 || y+1 > height || y-1<0 )
            return false;

        if(((dl>0) &&(l>=levels-1)) || (dl<0&&(l<=0)) )
            return false;

        if(!canPlaceStaircase(x,y,l,dl))
            return false;

        applyStaircase(x,y,l,dl, showoff);
        return true;




    }

	//Apply a staircase at a particular point in the map, 
	//towards a particular level ( dl ).
    public void applyStaircase(int x, int y, int l, int dl , boolean showoff){
        data[x-1][y+1][l] = TileType.Staircase;
        data[x][y+1][l] = TileType.Staircase;
        data[x+1][y+1][l] = TileType.Staircase;
        data[x-1][y][l] = TileType.Staircase;
        data[x][y][l] = TileType.Stairs;
        data[x+1][y][l] = TileType.Staircase;
        data[x-1][y-1][l] = TileType.Staircase;
        data[x][y-1][l] = TileType.Staircase;
        data[x+1][y-1][l] = TileType.Staircase;


	
        if(showoff){

            pushToShowoffQueue(x-1,y+1);
            pushToShowoffQueue(x+1,y+1);
            pushToShowoffQueue(x-1,y-1);
            pushToShowoffQueue(x+1,y-1);
            pushToShowoffQueue(x,y);

            renderNow(Main.drawsize);
            Main.drawlevel = l+dl;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        data[x-1][y+1][l+dl] = TileType.Staircase;
        data[x][y+1][l+dl] = TileType.Staircase;
        data[x+1][y+1][l+dl] = TileType.Staircase;
        data[x-1][y][l+dl] = TileType.Staircase;
        data[x][y][l+dl] = TileType.Stairs;
        data[x+1][y][l+dl] = TileType.Staircase;
        data[x-1][y-1][l+dl] = TileType.Staircase;
        data[x][y-1][l+dl] = TileType.Staircase;
        data[x+1][y-1][l+dl] = TileType.Staircase;

        if(showoff){
            renderNow(Main.drawsize);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
	
	//Ugly function checking the entire space the stairs will take 
	//to make sure its possible to place a staircase.
	//Could be looped but still works as is.
	public boolean canPlaceStaircase(int x, int y, int l, int dl){

        if(!staircaseCheck(x-1,y+1,l))return false;
        if(!staircaseCheck(x,y+1,l))return false;
        if(!staircaseCheck(x+1,y+1,l))return false;
        if(!staircaseCheck(x-1,y,l))return false;
        if(!staircaseCheck(x,y,l))return false;
        if(!staircaseCheck(x+1,y,l))return false;
        if(!staircaseCheck(x-1,y-1,l))return false;
        if(!staircaseCheck(x,y-1,l))return false;
        if(!staircaseCheck(x+1,y-1,l))return false;

        if(!staircaseCheck(x-1,y+1,l+dl))return false;
        if(!staircaseCheck(x,y+1,l+dl))return false;
        if(!staircaseCheck(x+1,y+1,l+dl))return false;
        if(!staircaseCheck(x-1,y,l+dl))return false;
        if(!staircaseCheck(x,y,l+dl))return false;
        if(!staircaseCheck(x+1,y,l+dl))return false;
        if(!staircaseCheck(x-1,y-1,l+dl))return false;
        if(!staircaseCheck(x,y-1,l+dl))return false;
        if(!staircaseCheck(x+1,y-1,l+dl))return false;

        return true;

    }

	//Lowest level method, checking a particular tile to make sure 
	//the type is compatible with being overridden.
    public boolean staircaseCheck(int x, int y, int l){
        return( (data[x][y][l]==TileType.Corridor || data[x][y][l]==TileType.Empty));
    }