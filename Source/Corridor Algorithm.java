	//Recursive Algorithm for corridor generation
    //X and Y are origin points of the corridor
    //dH and dV are horizontal and vertical directions
    //min and max length to set those ( 0 to ignore )
    //branch, end, room and stairs floats for RNG rolls
    //Priority to overwrite any existing tile type otherwise return
    public void addCorridor(int x, int y, int l, int dh, int dv, int maxlenght , int currentCount, float branch, float turn, float end, float room, float stairs , float model , boolean priority, boolean justBranched,boolean showoffmode , boolean singleBranchMode, boolean singleBranchChanceMode, boolean planeMode,float csavoid, int cto, int mlo, boolean useOffsets){


        //Out of bounds
        if(x < 0 || x >= width || y < 0 || y >= height )
            return;

        //Exceeding max length
        if(currentCount >= maxlenght){
            System.out.println("Current Corridor Exceeded Max Length ! Count : " + currentCount + " / " + maxlenght);
            return;
        }else {
            System.out.println("Current Count : " + currentCount + " / " + maxlenght);
        }

        //Tile not empty and cannot overwrite
        if( data[x][y][l] != TileType.Empty && !priority){

            if( !(data[x][y][l] == TileType.Staircase || data[x][y][l] == TileType.Stairs || data[x][y][l] == TileType.TriggerCorridorNORTH || data[x][y][l] == TileType.TriggerCorridorSOUTH || data[x][y][l] == TileType.TriggerCorridorEAST || data[x][y][l] == TileType.TriggerCorridorWEST)){
                return;
            }else if((data[x][y][l] == TileType.Staircase || data[x][y][l] == TileType.Stairs)&&planeMode){
                return;
            }

        }

        //Process parameter scales

        branch = branchs = PScale.Process(scaling.mode,scaling.bval,obranch,originFlr,l,levels);
        end = ends = PScale.Process(scaling.mode,scaling.eval,oend,originFlr,l,levels);
        turn = turns = PScale.Process(scaling.mode,scaling.tval,oturn,originFlr,l,levels);
        room = rooms = PScale.Process(scaling.mode,scaling.rval,oroom,originFlr,l,levels);
        stairs = PScale.Process(scaling.mode,scaling.sval,ostairs,originFlr,l,levels);
        model = models = PScale.Process(scaling.mode,scaling.mval,omodel,originFlr,l,levels);
        csavoid = PScale.Process(scaling.mode,scaling.cval,ocsavoid,originFlr,l,levels);

        maxlenght = maxlengths = (int)PScale.Process(scaling.mode,scaling.mlval,omaxlength,originFlr,l,levels)+((useOffsets) ? mlo:0);




        //Place corridor tile

        pushToShowoffQueue(x,y);

        currentIgnore = IgnoreNeighborhood.CorridorIgnore(Orientation.toOrientation(dh,dv));

        if(!planeMode){

            if(csavoid >= random.nextFloat()){
                if(checkIncompatibleTileNeighbors(x,y,l,corridorSelfAvoidanceList,currentIgnore)){

                    if(!(data[x][y][l] == TileType.Staircase || data[x][y][l] == TileType.Stairs))
                        data[x][y][l] = TileType.Corridor;currentCount++;

                }else{

                    return;
                }
            }else {
                if(checkIncompatibleTileNeighbors(x,y,l,corridorAvoidanceList,currentIgnore)){

                    if(!(data[x][y][l] == TileType.Staircase || data[x][y][l] == TileType.Stairs))
                        data[x][y][l] = TileType.Corridor;currentCount++;

                }else{

                    return;
                }
            }


        }
        else//If planar mode is active
        {

            if(csavoid >= random.nextFloat()){
                if(checkIncompatibleTileNeighbors3D(x,y,l,corridorSelfAvoidanceList,currentIgnore)){

                    if(!(data[x][y][l] == TileType.Staircase || data[x][y][l] == TileType.Stairs))
                        data[x][y][l] = TileType.Corridor;currentCount++;

                }else{
                    return;
                }
            }else {
                if(checkIncompatibleTileNeighbors3D(x,y,l,corridorAvoidanceList,currentIgnore)){

                    if(!(data[x][y][l] == TileType.Staircase || data[x][y][l] == TileType.Stairs))
                        data[x][y][l] = TileType.Corridor;currentCount++;

                }else{
                    return;
                }
            }


        }





        //Check if the corridor has been added
        if(data[x][y][l] == TileType.Corridor){
            //If the corridor is furthest from the spawn point, use it as current furthest path
            if(currentCount > longestPath){
                longestPath = currentCount;
                lastCorridor = new Vector3f(x,y,l);
            }

        }

        if(maxlengths>topLength)
            topLength=maxlengths;

        currentCounts = currentCount;


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
                    addCorridor(x+1,y,l, 1, 0,maxlenght,currentCount,branch, turn, end,room,stairs , model , priority, true , showoffmode , singleBranchMode, singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);
                    addCorridor(x-1,y,l, -1, 0,maxlenght,currentCount,branch, turn, end,room,stairs, model ,priority, true, showoffmode, singleBranchMode, singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);
                }else {

                    //Branch X positive
                    if (random.nextBoolean())
                        addCorridor(x + 1, y,l, 1, 0, maxlenght, currentCount, branch, turn, end, room, stairs, model ,priority, true, showoffmode, singleBranchMode, singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);
                    else
                        addCorridor(x - 1, y,l, -1, 0, maxlenght, currentCount, branch, turn, end, room, stairs, model ,priority, true, showoffmode, singleBranchMode, singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);
                }

            }//Currently Horizontal
            else{

                //Special condition
                if(random.nextBoolean()&&random.nextBoolean()){
                    addCorridor(x,y+1,l, 0, 1,maxlenght,currentCount,branch, turn, end,room,stairs, model ,priority, true, showoffmode , singleBranchMode, singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);
                    addCorridor(x,y-1,l, 0, -1,maxlenght,currentCount,branch, turn, end,room,stairs , model ,priority, true, showoffmode , singleBranchMode, singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);
                }else {

                    //Branch Y positive
                    if (random.nextBoolean())
                        addCorridor(x, y + 1,l, 0, 1, maxlenght, currentCount, branch, turn, end, room, stairs, model ,priority, true, showoffmode, singleBranchMode, singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);
                    else
                        addCorridor(x, y - 1,l, 0, -1, maxlenght, currentCount, branch, turn, end, room, stairs, model ,priority, true, showoffmode, singleBranchMode, singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);
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

            //Step to the next corridor right away
            //addCorridor(x+dh,y+dv,l,dh,dv,maxlenght,currentCount,branch, turn, end,room,stairs,model ,priority, false, showoffmode , singleBranchMode ,singleBranchChanceMode,planeMode,csavoid);


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



            if(!planeMode){
                if(placeStaircase(x,y,l,lld,showoffmode)){

                    addCorridor(x,y,l+lld,1,0, maxlenght,currentCount,branch,turn,end,room,stairs,model ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);
                    addCorridor(x,y,l+lld,-1,0, maxlenght,currentCount,branch,turn,end,room,stairs,model ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);
                    addCorridor(x,y,l+lld,0,1, maxlenght,currentCount,branch,turn,end,room,stairs,model ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);
                    addCorridor(x,y,l+lld,0,-1, maxlenght,currentCount,branch,turn,end,room,stairs,model ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);

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
            }else{//Adding planar style stairs
                //Special condition ( adds tri junction staircase )
                if(random.nextBoolean() && random.nextBoolean() && random.nextBoolean()){
                    System.out.println("TRI JUNCTION ADDED");

                    //Normal direction stairs
                    if(addStaircaseV2(x+dh,y+dv,l,dh,dv,lld)){


                        if(showoffmode){
                            Main.drawlevel = l+lld;
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            renderNow(Main.drawsize);
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }


                        addCorridor(x+dh*3,y+dv*3,l+lld,dh,dv,maxlenght,currentCount+2,branch,turn,end,room,stairs,model ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);


                        if(showoffmode){
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            renderNow(Main.drawsize);
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }

                    }

                    //First extra junction
                    odh = dh;
                    odv= dv;

                    dh = dv;
                    dv = odh;

                    if(addStaircaseV2(x+dh,y+dv,l,dh,dv,lld)){


                        if(showoffmode){
                            Main.drawlevel = l+lld;
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            renderNow(Main.drawsize);
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }


                        addCorridor(x+dh*3,y+dv*3,l+lld,dh,dv,maxlenght,currentCount+2,branch,turn,end,room,stairs,model ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);


                        if(showoffmode){
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            renderNow(Main.drawsize);
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }


                    }

                    dh = odh;
                    dv = odv;

                    //Second extra junction
                    odh = dh;
                    odv= dv;

                    dh = -dv;
                    dv = -odh;

                    if(addStaircaseV2(x+dh,y+dv,l,dh,dv,lld)){


                        if(showoffmode){
                            Main.drawlevel = l+lld;
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            renderNow(Main.drawsize);
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }


                        addCorridor(x+dh*3,y+dv*3,l+lld,dh,dv,maxlenght,currentCount+2,branch,turn,end,room,stairs,model ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);


                        if(showoffmode){
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            renderNow(Main.drawsize);
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }

                        return;

                    }

                    dh = odh;
                    dv = odv;



                }else if(random.nextBoolean() && random.nextBoolean() ){//T junction

                    System.out.println("T JUNCTION ADDED");

                    //First extra junction
                    odh = dh;
                    odv= dv;

                    dh = dv;
                    dv = odh;

                    if(addStaircaseV2(x+dh,y+dv,l,dh,dv,lld)){


                        if(showoffmode){
                            Main.drawlevel = l+lld;
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            renderNow(Main.drawsize);
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }


                        addCorridor(x+dh*3,y+dv*3,l+lld,dh,dv,maxlenght,currentCount+2,branch,turn,end,room,stairs,model ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);


                        if(showoffmode){
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            renderNow(Main.drawsize);
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }


                    }

                    dh = odh;
                    dv = odv;

                    //Second extra junction
                    odh = dh;
                    odv= dv;

                    dh = -dv;
                    dv = -odh;

                    if(addStaircaseV2(x+dh,y+dv,l,dh,dv,lld)){


                        if(showoffmode){
                            Main.drawlevel = l+lld;
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            renderNow(Main.drawsize);
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }


                        addCorridor(x+dh*3,y+dv*3,l+lld,dh,dv,maxlenght,currentCount+2,branch,turn,end,room,stairs,model ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);


                        if(showoffmode){
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            renderNow(Main.drawsize);
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }

                        return;

                    }

                    dh = odh;
                    dv = odv;
                }else{
                    if(addStaircaseV2(x+dh,y+dv,l,dh,dv,lld)){


                        if(showoffmode){
                            Main.drawlevel = l+lld;
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            pushToShowoffQueue(x+dh*2,y+dv*2);
                            renderNow(Main.drawsize);
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }


                        addCorridor(x+dh*3,y+dv*3,l+lld,dh,dv,maxlenght,currentCount+2,branch,turn,end,room,stairs,model ,priority,true,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);


                        if(showoffmode){
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            pushToShowoffQueue(x+dh,y+dv);
                            renderNow(Main.drawsize);
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }

                        return;

                    }
                }
            }





            return;

        }

        //Test for model placement
        if(random.nextFloat() < model){

            //Gets a random model
            modelChosen=false;
            currentModel=null;
            mn=0;
            do{
                ma = new Model[availableModels.size()];
                currentModel = Model.pickRandomChance(availableModels.toArray(ma), random.nextFloat());
                currentModelIndex = availableModels.indexOf(currentModel);
                currentModel = Model.copy(currentModel);//Dereference arrays

                //Return if no models are available
                if(currentModel == null)
                    return;

                if(currentModel.maxCount==-1){
                    modelChosen=true;
                }else if(currentModel.maxCount-1>=0){
                    modelChosen=true;
                }

                mn++;//Increment maximum try counter to avoid looping forever
            }while (!modelChosen && mn < 100);

            //Try adding the chosen model
            if(modelChosen){

                System.out.println("CHOSEN MODEL " + currentModel.type + " WITH " + currentModel.maxCount + " REMAINING USES");

                //currently horizontal
                if(dv == 0){

                    //Place northbound
                    if(random.nextBoolean()){
                        placeModel(x, y+1, l, 0 , 1, currentModel , false,branch,turn,end,room,stairs,model,justBranched,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid ,maxlenght,currentCount ,cto,mlo);
                    }else{
                        placeModel(x, y-1, l, 0 , -1, currentModel, false ,branch,turn,end,room,stairs,model,justBranched,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid,maxlenght ,currentCount ,cto,mlo);
                    }



                }else{


                    //Place eastbound
                    if(random.nextBoolean()){
                        placeModel(x+1 , y , l , 1 , 0, currentModel , false,branch,turn,end,room,stairs,model,justBranched,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid ,maxlenght,currentCount ,cto,mlo);
                    }else{
                        placeModel(x-1 , y , l , -1 , 0, currentModel , false,branch,turn,end,room,stairs,model,justBranched,showoffmode,singleBranchMode,singleBranchChanceMode,planeMode,csavoid,maxlenght ,currentCount,cto,mlo);
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

        }

        //Step to the next corridor
        addCorridor(x+dh,y+dv,l,dh,dv,maxlenght,currentCount,branch, turn, end,room,stairs,model ,priority, false, showoffmode , singleBranchMode ,singleBranchChanceMode,planeMode,csavoid,cto,mlo,useOffsets);



    }
