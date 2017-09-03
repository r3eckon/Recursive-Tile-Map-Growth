	//Method use to spawn corridors when 'G' is pressed.
	//Generates 4 corridors starting from a middle point
    public void generateMap( int x, int y , float b, float t, float e, float r, float s, float m , boolean p , boolean showoff, boolean singleBranchMode , boolean singleBranchChanceMode){

        int l = (levels == 1) ? 0 : (levels == 2 ) ? 1 : (levels >= 3) ? levels/2 : 0;

        if(l==0||levels==0)
            return;

        if(x < 0 || x>=width || y < 0 || y >= height)
            return;

        Main.drawlevel=l;



        data[x][y][l]=TileType.Spawn;

        addCorridor(x,y + 1, l,0,1,height/2,0,b,t,e,r,s,m,p,true, showoff , singleBranchMode ,singleBranchChanceMode);//Upwards
        addCorridor(x,y - 1, l,0,-1,height/2,0,b,t,e,r,s,m,p,true, showoff, singleBranchMode, singleBranchChanceMode);//Downwards
        addCorridor(x+1,y, l,1,0,width/2,0,b,t,e,r,s,m,p,true, showoff, singleBranchMode, singleBranchChanceMode);//Right
        addCorridor(x-1,y, l,-1,0,width/2,0,b,t,e,r,s,m,p,true, showoff,singleBranchMode, singleBranchChanceMode );//Left


	}
	

	//Algorithm for model placement
    //Models are arrays of any TileTypes including a certain point acting as the origin
    //The model is rotated prior to being sent to this algorithm
    //Indices are then calculated to make sure the model origin is placed at the current X,Y,L point
    public void placeModel(int x, int y, int l, int dh, int dv, Model model , boolean priority){

        int sx, sy, sl, cx, cy, cl; //indices

        model = Model.rotate(model , Orientation.toOrientation(dh,dv));


        sx = x - model.ox;
        sy = y - model.oy;
        sl = l - model.ol;

        //First loop to make sure the space is available in non priority mode

        for(int i = 0; i < model.width ; i++){
            for(int j = 0 ; j < model.height; j++){
                for(int k = 0; k < model.depth; k++ ) {



                    cx = sx + i;
                    cy = sy + j;
                    cl = sl + k;

                    //Return if out of bounds
                    if(cx < 0 || cx >= width || cy < 0 || cy >= height || cl < 0 || cl >= levels){
                        return;
                    }

                    //Return if cannot override non empty tile
                    if(data[cx][cy][cl] != TileType.Empty && !priority){
                        return;
                    }

                    //Finally check neighbors if required by model
                    for(TileType t : model.typesToCheck){
                        if(model.model[i][j][k]==t){
                            getNeighbors(cx,cy,cl);
                            if(!model.neighborCheck(north,south,east,west))
                                return;
                        }
                    }


                }
            }
        }

        //Second loop to place the model, if the space is fully available ( or if priority mode is on )

        for(int i = 0; i < model.width ; i++){
            for(int j = 0 ; j < model.height; j++){
                for(int k = 0; k < model.depth; k++ ) {

                    cx = sx + i;
                    cy = sy + j;
                    cl = sl + k;

                    data[cx][cy][cl] = model.model[i][j][k];

                }
            }
        }


    }
	
	
	//Algorithm for random room placement, supply your own tile types
    //and the method will grow a room in the available space.
    //Includes a way to make a room of fixed size
    public void placeRandomRoom(int x, int y,int l, int dh, int dv, TileType roomType, TileType entranceType, int minsize, int maxsize , boolean messyMode, boolean showoffmode){
        currentRoomTiles=0;
        entranceMet=false;

        //Out of bounds
        if(x < 0 || x >= width || y < 0 || y >= height )
            return;

        //Assign direction bound variables
        int d = (dh == 0) ? dv : dh;
        boolean zy = (d == dv);

        //Place initial entrance
        currentRoomX = x+dh;
        currentRoomY = y+dv;

        currentEntranceX = currentRoomX;
        currentEntranceY = currentRoomY;

        if(checkRoomNeighbors(currentRoomX+dh,currentRoomY+dv,l,roomType,entranceType))
            data[currentEntranceX][currentEntranceY][l] = entranceType;
        else{
            return;
        }

        currentRoomX +=dh;
        currentRoomY +=dv;

        int jn=-(random.nextInt(maxsize-minsize)+minsize);
        int jp=(random.nextInt(maxsize-minsize)+minsize);
        int in=-(random.nextInt(maxsize-minsize)+minsize);
        int ip=(random.nextInt(maxsize-minsize)+minsize);

        for(int j = jn; j < jp; j++){
            if(messyMode){
                jn=-(random.nextInt(maxsize-minsize)+minsize);
                jp=(random.nextInt(maxsize-minsize)+minsize);
            }

            //Generate room line
            for(int i=in; i < ip; i++){
                if(messyMode){
                    in=-(random.nextInt(maxsize-minsize)+minsize);
                    ip=(random.nextInt(maxsize-minsize)+minsize);
                }
                currentRoomX = (zy)? x + i*d : currentRoomX;
                currentRoomY = (!zy)? y + i*d : currentRoomY;


                if(checkRoomNeighbors(currentRoomX,currentRoomY,l,roomType,entranceType)){
                    data[currentRoomX][currentRoomY][l] = roomType;
                    currentRoomTiles++;

                    pushToShowoffQueue(currentRoomX,currentRoomY);

                    if(showoffmode){
                        renderNow(Main.drawsize);
                        try {
                            Thread.sleep(showoffwait);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }else{

                    if(currentRoomTiles==0 ){
                        data[currentEntranceX][currentEntranceY][l] = TileType.Empty;
                    }
                    return;


                }



            }
            currentRoomX +=dh;
            currentRoomY +=dv;

        }

        if(currentRoomTiles==0){
            data[currentEntranceX][currentEntranceY][l] = TileType.Empty;
        }

    }
	