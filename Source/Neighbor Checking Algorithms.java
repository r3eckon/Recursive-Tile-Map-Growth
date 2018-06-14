	//Get immediate neighbors on same level
    public void getNeighbors(int x, int y , int l){
        currentNeighborhood = new TileNeighborhood();

        if(data==null){
            currentNeighborhood.north=TileType.ERROR;
            currentNeighborhood.south=TileType.ERROR;
            currentNeighborhood.east=TileType.ERROR;
            currentNeighborhood.west=TileType.ERROR;
            return;
        }
        try{
            currentNeighborhood.north=data[x][y+1][l];
        }catch (Exception ex){
            currentNeighborhood.north=TileType.ERROR;
        }

        try{
            currentNeighborhood.south=data[x][y-1][l];
        }catch (Exception ex){
            currentNeighborhood.south=TileType.ERROR;
        }

        try{
            currentNeighborhood.east=data[x+1][y][l];
        }catch (Exception ex){
            currentNeighborhood.east=TileType.ERROR;
        }

        try{
            currentNeighborhood.west=data[x-1][y][l];
        }catch (Exception ex){
            currentNeighborhood.west=TileType.ERROR;
        }
    }

    //Also get above & below
    public void getNeighbors3D(int x, int y , int l){
        currentNeighborhood = new TileNeighborhood();

        if(data==null){
            currentNeighborhood.north=TileType.ERROR;
            currentNeighborhood.south=TileType.ERROR;
            currentNeighborhood.east=TileType.ERROR;
            currentNeighborhood.west=TileType.ERROR;
            return;
        }
        try{
            currentNeighborhood.north=data[x][y+1][l];
        }catch (Exception ex){
            currentNeighborhood.north=TileType.ERROR;
        }

        try{
            currentNeighborhood.south=data[x][y-1][l];
        }catch (Exception ex){
            currentNeighborhood.south=TileType.ERROR;
        }

        try{
            currentNeighborhood.east=data[x+1][y][l];
        }catch (Exception ex){
            currentNeighborhood.east=TileType.ERROR;
        }

        try{
            currentNeighborhood.west=data[x-1][y][l];
        }catch (Exception ex){
            currentNeighborhood.west=TileType.ERROR;
        }

        try{
            currentNeighborhood.above=data[x][y][l-1];
        }catch (Exception ex){
            currentNeighborhood.above=TileType.ERROR;
        }
        try{
            currentNeighborhood.below=data[x][y][l+1];
        }catch (Exception ex){
            currentNeighborhood.below=TileType.ERROR;
        }
    }

    //Get full neighboorhood
    public void getNeighborsFull(int x, int y , int l){
        currentNeighborhood = new TileNeighborhood();

        if(data==null){
            currentNeighborhood.north=TileType.ERROR;
            currentNeighborhood.south=TileType.ERROR;
            currentNeighborhood.east=TileType.ERROR;
            currentNeighborhood.west=TileType.ERROR;
            return;
        }
        try{
            currentNeighborhood.north=data[x][y+1][l];
        }catch (Exception ex){
            currentNeighborhood.north=TileType.ERROR;
        }
        try{
            currentNeighborhood.northeast=data[x+1][y+1][l];
        }catch (Exception ex){
            currentNeighborhood.northeast=TileType.ERROR;
        }
        try{
            currentNeighborhood.northwest=data[x-1][y+1][l];
        }catch (Exception ex){
            currentNeighborhood.northwest=TileType.ERROR;
        }

        try{
            currentNeighborhood.south=data[x][y-1][l];
        }catch (Exception ex){
            currentNeighborhood.south=TileType.ERROR;
        }
        try{
            currentNeighborhood.southwest=data[x-1][y-1][l];
        }catch (Exception ex){
            currentNeighborhood.southwest=TileType.ERROR;
        }
        try{
            currentNeighborhood.southeast=data[x+1][y-1][l];
        }catch (Exception ex){
            currentNeighborhood.southeast=TileType.ERROR;
        }

        try{
            currentNeighborhood.east=data[x+1][y][l];
        }catch (Exception ex){
            currentNeighborhood.east=TileType.ERROR;
        }

        try{
            currentNeighborhood.west=data[x-1][y][l];
        }catch (Exception ex){
            currentNeighborhood.west=TileType.ERROR;
        }

        try{
            currentNeighborhood.above=data[x][y][l+1];
        }catch (Exception ex){
            currentNeighborhood.above=TileType.ERROR;
        }
        try{
            currentNeighborhood.below=data[x][y][l-1];
        }catch (Exception ex){
            currentNeighborhood.below=TileType.ERROR;
        }
    }

    public boolean checkRoomNeighbors(int x, int y, int l, TileType roomType, TileType entranceType){
        getNeighbors(x,y,l);

        if(!(currentNeighborhood.north == TileType.Empty || currentNeighborhood.north == roomType || currentNeighborhood.north == entranceType ) || (currentNeighborhood.north==TileType.Corridor))
            return false;

        if(!(currentNeighborhood.south == TileType.Empty || currentNeighborhood.south == roomType || currentNeighborhood.south == entranceType ) || (currentNeighborhood.south==TileType.Corridor))
            return false;

        if(!(currentNeighborhood.east == TileType.Empty || currentNeighborhood.east == roomType || currentNeighborhood.east == entranceType )|| (currentNeighborhood.east==TileType.Corridor))
            return false;

        if(!(currentNeighborhood.west == TileType.Empty || currentNeighborhood.west == roomType || currentNeighborhood.west == entranceType )|| (currentNeighborhood.west==TileType.Corridor))
            return false;

        return true;
    }

    public boolean checkRoomNeighbors3D(int x, int y, int l, TileType roomType, TileType entranceType){
        getNeighbors3D(x,y,l);

        if(!(currentNeighborhood.north == TileType.Empty || currentNeighborhood.north == roomType || currentNeighborhood.north == entranceType ) || (currentNeighborhood.north==TileType.Corridor))
            return false;

        if(!(currentNeighborhood.south == TileType.Empty || currentNeighborhood.south == roomType || currentNeighborhood.south == entranceType ) || (currentNeighborhood.south==TileType.Corridor))
            return false;

        if(!(currentNeighborhood.east == TileType.Empty || currentNeighborhood.east == roomType || currentNeighborhood.east == entranceType )|| (currentNeighborhood.east==TileType.Corridor))
            return false;

        if(!(currentNeighborhood.west == TileType.Empty || currentNeighborhood.west == roomType || currentNeighborhood.west == entranceType )|| (currentNeighborhood.west==TileType.Corridor))
            return false;

        if(!(currentNeighborhood.above == TileType.Empty || currentNeighborhood.above == roomType || currentNeighborhood.above == entranceType )|| (currentNeighborhood.above==TileType.Corridor))
            return false;

        if(!(currentNeighborhood.below == TileType.Empty || currentNeighborhood.below == roomType || currentNeighborhood.below == entranceType )|| (currentNeighborhood.below==TileType.Corridor))
            return false;

        return true;
    }

    public boolean checkIncompatibleTileNeighbors(int x,int y,int l, TileType[] incompatible,IgnoreNeighborhood ignore){
        getNeighbors(x,y,l);

        boolean ok = true;

        for(TileType t:incompatible){
            ok=!((currentNeighborhood.north==t&&!ignore.north)||(currentNeighborhood.south==t&&!ignore.south)||(currentNeighborhood.east==t&&!ignore.east)||(currentNeighborhood.west==t&&!ignore.west));
            if(!ok)
                return ok;
        }

        return ok;
    }

    public boolean checkIncompatibleTileNeighbors3D(int x,int y,int l, TileType[] incompatible,IgnoreNeighborhood ignore){
        getNeighbors3D(x,y,l);

        boolean ok = true;

        for(TileType t:incompatible){
            ok=!((currentNeighborhood.north==t&&!ignore.north)||(currentNeighborhood.south==t&&!ignore.south)||(currentNeighborhood.east==t&&!ignore.east)||(currentNeighborhood.west==t&&!ignore.west)||(currentNeighborhood.above==t&&!ignore.above)||(currentNeighborhood.below==t&&!ignore.below));
            if(!ok)
                return ok;
        }

        return ok;
    }

    public boolean checkCompatibleNeighbors(int x, int y, int l , TileType[] compatible,IgnoreNeighborhood ignore){

        boolean northok = false,southok = false,eastok = false,westok = false;

        getNeighbors(x,y,l);

        for(TileType t : compatible){

            if(!northok)
                northok = (currentNeighborhood.north == t || ignore.north);

            if(!southok)
                southok = (currentNeighborhood.south == t|| ignore.south);

            if(!eastok)
                eastok = (currentNeighborhood.east == t|| ignore.east);

            if(!westok)
                westok = (currentNeighborhood.west == t|| ignore.west);

        }


        return (northok&&southok&&westok&&eastok);

    }

    public boolean areNeighborsEmpty(int x, int y, int l){
        getNeighbors(x,y,l);
        return (currentNeighborhood.north==TileType.Empty && currentNeighborhood.south==TileType.Empty && currentNeighborhood.east == TileType.Empty && currentNeighborhood.west == TileType.Empty);
    }