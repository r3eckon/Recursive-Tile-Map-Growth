//Recursive Algorithm for corridor generation
    //X and Y are origin points of the corridor
    //dH and dV are horizontal and vertical directions
    //min and max length to set those ( 0 to ignore )
    //branch, end, room and stairs floats for RNG rolls
    //Priority to overwrite any existing tile type otherwise return
    public void addCorridor(int x, int y, int dh, int dv, int maxlenght , int currentCount, float branch, float turn, float end, float room, float stairs , boolean priority, boolean justBranched,boolean showoffmode , boolean singleBranchMode, boolean singleBranchChanceMode){


        //Out of bounds
        if(x < 0 || x >= width || y < 0 || y >= height )
            return;

        //Tile not empty and cannot overwrite
        if( data[x][y] != TileType.Empty && !priority){
            return;
        }

        //Place corridor tile

        if(checkIncompatibleTileNeighbors(x,y,corridorAvoidanceList)){
            data[x][y] = TileType.Corridor;
            if(showoffmode){
                renderNow(Main.drawsize);
                try {
                    Thread.sleep(showoffwait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            currentCount++;

        }else{
            return;
        }





        //Exceeding max length
        if(currentCount >= maxlenght){
            return;
        }

        //RNG module

        //End the corridor randomly
        if(random.nextFloat() < end)
            return;

        //Branch into a perpendicular corridor
        if(random.nextFloat() < branch && !justBranched){
            //Currently vertical
            if(dh==0){

                //Special condition
                if(random.nextBoolean()&&random.nextBoolean()){
                    addCorridor(x+1,y, 1, 0,maxlenght,currentCount,branch, turn, end,room,stairs , priority, true , showoffmode , singleBranchMode, singleBranchChanceMode);
                    addCorridor(x-1,y, -1, 0,maxlenght,currentCount,branch, turn, end,room,stairs, priority, true, showoffmode, singleBranchMode, singleBranchChanceMode);
                }else {

                    //Branch X positive
                    if (random.nextBoolean())
                        addCorridor(x + 1, y, 1, 0, maxlenght, currentCount, branch, turn, end, room, stairs, priority, true, showoffmode, singleBranchMode, singleBranchChanceMode);
                    else
                        addCorridor(x - 1, y, -1, 0, maxlenght, currentCount, branch, turn, end, room, stairs, priority, true, showoffmode, singleBranchMode, singleBranchChanceMode);
                }

            }//Currently Horizontal
            else{

                //Special condition
                if(random.nextBoolean()&&random.nextBoolean()){
                    addCorridor(x,y+1, 0, 1,maxlenght,currentCount,branch, turn, end,room,stairs, priority, true, showoffmode , singleBranchMode, singleBranchChanceMode);
                    addCorridor(x,y-1, 0, -1,maxlenght,currentCount,branch, turn, end,room,stairs , priority, true, showoffmode , singleBranchMode, singleBranchChanceMode);
                }else {

                    //Branch Y positive
                    if (random.nextBoolean())
                        addCorridor(x, y + 1, 0, 1, maxlenght, currentCount, branch, turn, end, room, stairs, priority, true, showoffmode, singleBranchMode, singleBranchChanceMode);
                    else
                        addCorridor(x, y - 1, 0, -1, maxlenght, currentCount, branch, turn, end, room, stairs, priority, true, showoffmode, singleBranchMode, singleBranchChanceMode);
                }
            }


            if(singleBranchMode && !singleBranchChanceMode )
                return;

            if(singleBranchChanceMode && singleBranchMode && random.nextFloat() < turn)
                return;


        }

        //Place entrance
        if(random.nextFloat() < room){

            //Currently vertical
            if(dh==0){
                //Room Horizontal Positive
                if(random.nextBoolean())
                    placeRandomRoom(x,y,1,0,  TileType.Room, TileType.Entrance,2,6,false, showoffmode);
                else{
                    placeRandomRoom(x,y,-1,0, TileType.Room, TileType.Entrance,2,6,false, showoffmode);
                }
            //Currently horizontal
            }else{
                //Room Vertical Positive
                if(random.nextBoolean())
                    placeRandomRoom(x,y,0,1,  TileType.Room2, TileType.Entrance2,2,5,true, showoffmode);
                else{
                    placeRandomRoom(x,y,0,-1, TileType.Room2, TileType.Entrance2,2,5,true, showoffmode);
                }

            }

        }

        //Place staircase
        if(random.nextFloat() < stairs){
            //TODO:Place stairs
        }

        //Step to the next corridor
        addCorridor(x+dh,y+dv,dh,dv,maxlenght,currentCount,branch, turn, end,room,stairs,priority, false, showoffmode , singleBranchMode ,singleBranchChanceMode);



    }