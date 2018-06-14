	//Recursive Algorithm for corridor generation
    //X and Y are origin points of the corridor
    //dH and dV are horizontal and vertical directions
    //min and max length to set those ( 0 to ignore )
    //branch, end, room and stairs floats for RNG rolls
    //Priority to overwrite any existing tile type otherwise return
    public void addCorridor(int x, int y, int l, int dh, int dv, int maxlenght , int currentCount, float branch, float turn, float end, float room, float stairs , float testModel , boolean priority, boolean justBranched,boolean showoffmode , boolean singleBranchMode, boolean singleBranchChanceMode, boolean planeMode){


        //Out of bounds
        if(x < 0 || x >= width || y < 0 || y >= height )
            return;

        //Tile not empty and cannot overwrite
        if( data[x][y][l] != TileType.Empty && !priority){

            if( !(data[x][y][l] == TileType.Staircase || data[x][y][l] == TileType.Stairs || data[x][y][l] == TileType.TriggerCorridorNORTH || data[x][y][l] == TileType.TriggerCorridorSOUTH || data[x][y][l] == TileType.TriggerCorridorEAST || data[x][y][l] == TileType.TriggerCorridorWEST)){
                return;
            }

        }

        //Place corridor tile

        pushToShowoffQueue(x,y);

        currentIgnore = IgnoreNeighborhood.CorridorIgnore(Orientation.toOrientation(dh,dv));

        if(!planeMode){
            if(checkIncompatibleTileNeighbors(x,y,l,corridorAvoidanceList,currentIgnore)){

                if(!(data[x][y][l] == TileType.Staircase || data[x][y][l] == TileType.Stairs))
                    data[x][y][l] = TileType.Corridor;


                if(showoffmode){
                    if(Main.drawlevel!=l)
                        Main.drawlevel=l;
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
        }
        else
        {
            if(checkIncompatibleTileNeighbors3D(x,y,l,corridorAvoidanceList,currentIgnore)){

                if(!(data[x][y][l] == TileType.Staircase || data[x][y][l] == TileType.Stairs))
                    data[x][y][l] = TileType.Corridor;


                if(showoffmode){
                    if(Main.drawlevel!=l)
                        Main.drawlevel=l;
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
        }






        //Exceeding max length
        if(currentCount >= maxlenght){
            return;
        }

        //RNG module...

        //End the corridor randomly
        if(random.nextFloat() < end)
            return;

        //Branch into a perpendicular corridor
        if(random.nextFloat() < branch && !justBranched){
            //Currently vertical
            if(dh==0){

                //Special condition
                if(random.nextBoolean()&&random.nextBoolean()){
                    addCorridor(x+1,y,l, 1, 0,maxlenght,currentCount,branch, turn, end,room,stairs , testModel , priority, true , showoffmode , singleBranchMode, singleBranchChanceMode,planeMode);
                    addCorridor(x-1,y,l, -1, 0,maxlenght,currentCount,branch, turn, end,room,stairs, testModel ,priority, true, showoffmode, singleBranchMode, singleBranchChanceMode,planeMode);
                }else {

                    //Branch X positive
                    if (random.nextBoolean())
                        addCorridor(x + 1, y,l, 1, 0, maxlenght, currentCount, branch, turn, end, room, stairs, testModel ,priority, true, showoffmode, singleBranchMode, singleBranchChanceMode,planeMode);
                    else
                        addCorridor(x - 1, y,l, -1, 0, maxlenght, currentCount, branch, turn, end, room, stairs, testModel ,priority, true, showoffmode, singleBranchMode, singleBranchChanceMode,planeMode);
                }

            }//Currently Horizontal
            else{

                //Special condition
                if(random.nextBoolean()&&random.nextBoolean()){
                    addCorridor(x,y+1,l, 0, 1,maxlenght,currentCount,branch, turn, end,room,stairs, testModel ,priority, true, showoffmode , singleBranchMode, singleBranchChanceMode,planeMode);
                    addCorridor(x,y-1,l, 0, -1,maxlenght,currentCount,branch, turn, end,room,stairs , testModel ,priority, true, showoffmode , singleBranchMode, singleBranchChanceMode,planeMode);
                }else {

                    //Branch Y positive
                    if (random.nextBoolean())
                        addCorridor(x, y + 1,l, 0, 1, maxlenght, currentCount, branch, turn, end, room, stairs, testModel ,priority, true, showoffmode, singleBranchMode, singleBranchChanceMode,planeMode);
                    else
                        addCorridor(x, y - 1,l, 0, -1, maxlenght, currentCount, branch, turn, end, room, stairs, testModel ,priority, true, showoffmode, singleBranchMode, singleBranchChanceMode,planeMode);
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
                    placeRandomRoom(x,y,l,1,0,  TileType.Room, TileType.Entrance,2,5,false, showoffmode,planeMode);
                else{
                    placeRandomRoom(x,y,l,-1,0, TileType.Room, TileType.Entrance,2,5,false, showoffmode,planeMode);
                }
            //Currently horizontal
            }else{
                //Room Vertical Positive
                if(random.nextBoolean())
                    placeRandomRoom(x,y,l,0,1,  TileType.Room2, TileType.Entrance2,2,5,true, showoffmode,planeMode);
                else{
                    placeRandomRoom(x,y,l,0,-1, TileType.Room2, TileType.Entrance2,2,5,true, showoffmode,planeMode);
                }



            }

            //Step to the next corridor
            addCorridor(x+dh,y+dv,l,dh,dv,maxlenght,currentCount,branch, turn, end,room,stairs,testModel ,priority, false, showoffmode , singleBranchMode ,singleBranchChanceMode,planeMode);


        }

        //Place staircase
        if(random.nextFloat() < stairs){

            int lld = 0;

            //Towards top since we are at bottom
            if(l == 0)lld=1;

            //Towards bottom since we are at top
            else if(l>=levels-1)lld=-1;
            //Randomly since we are in between
            else if(random.nextBoolean())lld=1;
            else lld = -1;

            if(placeStaircase(x,y,l,lld,showoffmode)){

                addCorridor(x,y,l+lld,1,0, maxlenght,currentCount,branch,turn,end,room,stairs,testModel ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode);
                addCorridor(x,y,l+lld,-1,0, maxlenght,currentCount,branch,turn,end,room,stairs,testModel ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode);
                addCorridor(x,y,l+lld,0,1, maxlenght,currentCount,branch,turn,end,room,stairs,testModel ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode);
                addCorridor(x,y,l+lld,0,-1, maxlenght,currentCount,branch,turn,end,room,stairs,testModel ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode);

                Main.drawlevel=l;
                //End corridor
                if(showoffmode){
                    pushToShowoffQueue(x-1,y+1);
                    pushToShowoffQueue(x+1,y+1);
                    pushToShowoffQueue(x-1,y-1);
                    pushToShowoffQueue(x+1,y-1);
                    pushToShowoffQueue(x,y);
                    renderNow(Main.drawsize);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }


            return;

        }

        //Test for model placement
        if(random.nextFloat() < testModel){


            //currently horizontal
            if(dv == 0){

                //Place northbound
                if(random.nextBoolean()){
                    placeModel(x, y+1, l, 0 , 1, Model.pickRandom(new Model[]{Model.BigCloset(), Model.BossRoom(), Model.TallCloset(), Model.Closet()}) , false );
                }else{
                    placeModel(x, y-1, l, 0 , -1, Model.pickRandom(new Model[]{Model.BigCloset(), Model.BossRoom(), Model.TallCloset(), Model.Closet()}), false );
                }



            }else{


                //Place eastbound
                if(random.nextBoolean()){
                    placeModel(x+1 , y , l , 1 , 0, Model.pickRandom(new Model[]{Model.BigCloset(), Model.BossRoom(), Model.TallCloset(), Model.Closet()}) , false);
                }else{
                    placeModel(x-1 , y , l , -1 , 0, Model.pickRandom(new Model[]{Model.BigCloset(), Model.BossRoom(), Model.TallCloset(), Model.Closet()}) , false);
                }


            }

            if(showoffmode){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



        }



        //Step to the next corridor
        addCorridor(x+dh,y+dv,l,dh,dv,maxlenght,currentCount,branch, turn, end,room,stairs,testModel ,priority, false, showoffmode , singleBranchMode ,singleBranchChanceMode,planeMode);



    }
