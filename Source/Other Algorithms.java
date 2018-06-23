	//Generates 4 corridors starting from a middle point
    public void generateMap( int x, int y , float b, float t, float e, float r, float s, float m , boolean p , boolean showoff, boolean singleBranchMode , boolean singleBranchChanceMode, boolean planarMode){

        int l = (levels == 1) ? 0 : (levels == 2 ) ? 1 : (levels >= 3) ? levels/2 : 0;

        if(l==0||levels==0)
            return;

        if(x < 0 || x>=width || y < 0 || y >= height)
            return;

        Main.drawlevel=l;

        pgtriggers=new ArrayList<PGTrigger>();
        currentlyProcessingTriggers=false;

        data[x][y][l]=TileType.Spawn;

        //The following code includes an example on how to use post generation triggers to generate the original layout.

        //pgtriggers.add(new PGTrigger(new Vector3f(x+1,y,l) , Orientation.Eastbound , PGTrigger.TYPE_ADDCORRIDOR ));
        //pgtriggers.add(new PGTrigger(new Vector3f(x-1,y,l) , Orientation.Westbound , PGTrigger.TYPE_ADDCORRIDOR ));
        //pgtriggers.add(new PGTrigger(new Vector3f(x,y+1,l) , Orientation.Northbound , PGTrigger.TYPE_ADDCORRIDOR ));
        //pgtriggers.add(new PGTrigger(new Vector3f(x,y-1,l) , Orientation.Southbound , PGTrigger.TYPE_ADDCORRIDOR ));

        //addCorridor(x,y + 1, l,0,1,height/2,0,b,t,e,r,s,m,p,true, showoff , singleBranchMode ,singleBranchChanceMode);//Upwards
        //addCorridor(x,y - 1, l,0,-1,height/2,0,b,t,e,r,s,m,p,true, showoff, singleBranchMode, singleBranchChanceMode);//Downwards
        addCorridor(x+1,y, l,1,0,width/2,0,b,t,e,r,s,m,p,true, showoff, singleBranchMode, singleBranchChanceMode,planarMode);//Right
        //addCorridor(x-1,y, l,-1,0,width/2,0,b,t,e,r,s,m,p,true, showoff,singleBranchMode, singleBranchChanceMode );//Left

        //Process post generation triggers


        if(pgtriggers!=null){

            int cx,cy,cl,dh,dv;

            currentlyProcessingTriggers=true;
            for(PGTrigger tg : pgtriggers){

                cx=(int)tg.pos.x;
                cy=(int)tg.pos.y;
                cl=(int)tg.pos.z;
                dh = (int)Orientation.toDxDy(tg.orientation).x;
                dv = (int)Orientation.toDxDy(tg.orientation).y;

                pushToShowoffQueue((int)tg.pos.x,(int)tg.pos.y);
                pushToShowoffQueue((int)tg.pos.x,(int)tg.pos.y);
                pushToShowoffQueue((int)tg.pos.x,(int)tg.pos.y);
                pushToShowoffQueue((int)tg.pos.x,(int)tg.pos.y);
                pushToShowoffQueue((int)tg.pos.x,(int)tg.pos.y);

                if(showoff){


                    if(Main.drawlevel!=tg.pos.z){
                        Main.drawlevel=(int)tg.pos.z;
                    }

                    renderNow(Main.drawsize);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }




                switch (tg.type){

                    case PGTrigger.TYPE_NOP:break;

                    case PGTrigger.TYPE_ADDCORRIDOR:

                        //Execute corridor growing if there is space
                        if(data[cx][cy][cl] == TileType.Empty){

                            //First process the data array on the model to add entrance
                            data[cx-dh][cy-dv][cl]  = TileType.EntranceBoss;

                            //Then grow a corridor
                            addCorridor((int)tg.pos.x,(int)tg.pos.y,(int)tg.pos.z,(int)Orientation.toDxDy(tg.orientation).x , (int)Orientation.toDxDy(tg.orientation).y , height/2,0,b,t,e,r,s,m,p,true, showoff , singleBranchMode ,singleBranchChanceMode,planarMode);

                            System.out.println("Corridor Added");
                        }
                        System.out.println("Corridor Trigger Processed");
                        break;

                    case PGTrigger.TYPE_ADDRANDROOM:

                        if(data[cx][cy][cl] == TileType.Empty){
                            data[cx-dh][cy-dv][cl] = TileType.Entrance;
                            data[cx][cy][cl] = TileType.Corridor;
                            placeRandomRoom((int)tg.pos.x,(int)tg.pos.y,(int)tg.pos.z,(int)Orientation.toDxDy(tg.orientation).x , (int)Orientation.toDxDy(tg.orientation).y , (tg.orientation==Orientation.Northbound || tg.orientation==Orientation.Southbound) ? TileType.Room2 : TileType.Room ,(tg.orientation==Orientation.Northbound || tg.orientation==Orientation.Southbound) ? TileType.Entrance2 : TileType.Entrance , 2,5, (tg.orientation==Orientation.Northbound || tg.orientation==Orientation.Southbound),showoff,planarMode  );
                            System.out.println("Room Added");
                        }
                        System.out.println("Room Trigger Processed");
                        break;

                    case PGTrigger.TYPE_ADDRANDMODL:

                        if(data[cx][cy][cl] == TileType.Empty){
                            data[cx-dh][cy-dv][cl] = TileType.Entrance;
                            data[cx][cy][cl] = TileType.Corridor;
                            placeModel((int)tg.pos.x,(int)tg.pos.y,(int)tg.pos.z,(int)Orientation.toDxDy(tg.orientation).x , (int)Orientation.toDxDy(tg.orientation).y, Model.pickRandom(new Model[]{Model.BigCloset(), Model.BossRoom(), Model.TallCloset(), Model.Closet()}),false);
                            System.out.println("Model Added");
                        }

                        System.out.println("Model Trigger Processed");

                        break;

                    default:break;
                }
            }
        }


    }


	//Algorithm for model placement
    //Models are arrays of any TileTypes including a certain point acting as the origin
    //The model is rotated prior to being sent to this algorithm
    //Indices are then calculated to make sure the model origin is placed at the current X,Y,L point
    public void placeModel(int x, int y, int l, int dh, int dv, Model model , boolean priority, float b,float t,float e,float r,float s,float m,boolean justBranched,boolean showoff , boolean singleBranchMode, boolean singleBranchChanceMode, boolean planeMode,float csavoid){

        int sx, sy, sl, cx=0, cy=0, cl=0, tx,ty,tl; //indices

        //SX,SY,SL are where the 0,0,0 point of the model are on the data
        //CX,CY,CL are where the current X,Y,Z of the model are on the data
        //TX,TY,TL are where the current X,Y,Z of a TRIGGER are on the data

        TileType current;

        Orientation modelOrientation = Orientation.toOrientation(dh,dv);
        Orientation triggerOrientation;


        model = Model.rotate(model , modelOrientation);



        boolean isBoss=false;

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
                    for(TileType tt : model.typesToCheck){
                        if(model.model[i][j][k]==tt){

                            if(!planeMode){
                                getNeighbors(cx,cy,cl);
                                if(!model.neighborCheck(currentNeighborhood.north,currentNeighborhood.south,currentNeighborhood.east,currentNeighborhood.west))
                                    return;
                            }else{
                                getNeighbors3D(cx,cy,cl);
                                if(!model.neighborCheck3D(currentNeighborhood.north,currentNeighborhood.south,currentNeighborhood.east,currentNeighborhood.west, currentNeighborhood.above,currentNeighborhood.below))
                                    return;
                            }


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

                    if(!isBoss &&model.model[i][j][k]==TileType.BossRoom )
                        isBoss=true;

                    data[cx][cy][cl] = model.model[i][j][k];


                    /*DEFUNCT MODEL PROCESSING CODE

                    //Detect and add post generation triggers from tile types

                    current = data[cx][cy][cl];

                    //Rotate the trigger's orientation relative to the model rotation
                    triggerOrientation = Orientation.rotate(trig.orientation , modelOrientation);

                    //Create a new PGTrigger object using the rotated orientation and the trigger type
                    //Must find a way to streamline this process for usage within a GUI...
                    //Maybe let user place the entrance instead of a trigger type
                    //and specify triggers as new data included with model class
                    //This seems like the optimal idea despite the need to entirely rework the PGTrigger system.


                    if(current == TileType.TriggerCorridorNORTH || current == TileType.TriggerCorridorSOUTH || current == TileType.TriggerCorridorEAST || current == TileType.TriggerCorridorWEST ){

                        if(!currentlyProcessingTriggers){
                            pgtriggers.add(new PGTrigger(new Vector3f(cx+dh,cy+dv,cl), triggerOrientation , PGTrigger.TYPE_ADDCORRIDOR ));
                            data[cx][cy][cl] = TileType.EntranceBoss;
                        }else {
                            data[cx][cy][cl] = TileType.Empty;
                        }

                    }else if(current == TileType.TriggerRoomNORTH || current == TileType.TriggerRoomSOUTH || current == TileType.TriggerRoomEAST || current == TileType.TriggerRoomWEST){

                        if(!currentlyProcessingTriggers){
                            pgtriggers.add(new PGTrigger(new Vector3f(cx+dh,cy+dv,cl), triggerOrientation , PGTrigger.TYPE_ADDRANDROOM ));
                            data[cx][cy][cl] = TileType.EntranceBoss;
                            data[cx+dh][cy+dv][cl] = TileType.Corridor;
                        }else{
                            data[cx][cy][cl] = TileType.Empty;
                        }

                    }


                    */


                }
            }
        }

        //New trigger detection process
        //Generalized for all trigger type, orientation and position

        if(model.triggers!=null && !currentlyProcessingTriggers){

            for(PGTrigger trig : model.triggers){

                //Initialize by setting trigger position in data
                tx = sx + (int)trig.pos.x;
                ty = sy + (int)trig.pos.y;
                tl = sl + (int)trig.pos.z;

                trig.iexec=true;//REMOVE ME



                //First calculate the correct orientation
                triggerOrientation = Orientation.rotate(trig.orientation , modelOrientation);

                //Create trigger obj
                trig = new PGTrigger(new Vector3f(tx+dh,ty+dv,tl), triggerOrientation , trig.type,true );

                //Then add the new trigger with it's data on the map if it's intended for Post Generation Execution
                if(!trig.iexec)
                    pgtriggers.add(trig);
                else{//Otherwise execute the trigger immediately
                     processTrigger(trig,b,t,e,r,s,m,priority,justBranched,showoff,singleBranchMode,singleBranchChanceMode,planeMode,csavoid);
                }



            }

        }




    }

	
    //Universal trigger processing code
    //Handle custom trigger types here
    public void processTrigger(PGTrigger trig, float b, float t,float e,float r,float s,float m,boolean priority,boolean justBranched,boolean showoff, boolean singleBranchMode,boolean singleBranchChanceMode,boolean planeMode,float csavoid){

        int dh = (int)Orientation.toDxDy(trig.orientation).x;
        int dv = (int)Orientation.toDxDy(trig.orientation).y;
        int cx=(int)trig.pos.x;
        int cy=(int)trig.pos.y;
        int cl=(int)trig.pos.z;

        pushToShowoffQueue((int)trig.pos.x-dh,(int)trig.pos.y+dv);
        pushToShowoffQueue((int)trig.pos.x-dh,(int)trig.pos.y+dv);
        pushToShowoffQueue((int)trig.pos.x-dh,(int)trig.pos.y+dv);
        pushToShowoffQueue((int)trig.pos.x,(int)trig.pos.y);
        pushToShowoffQueue((int)trig.pos.x,(int)trig.pos.y);

        if(showoff){


            if(Main.drawlevel!=trig.pos.z){
                Main.drawlevel=(int)trig.pos.z;
            }

            renderNow(Main.drawsize);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }

        switch (trig.type){

            case PGTrigger.TYPE_NOP:break;

            case PGTrigger.TYPE_ADDCORRIDOR:

                //Execute corridor growing if there is space
                if(data[cx][cy][cl] == TileType.Empty){

                    //First process the data array on the model to add entrance
                    data[cx-dh][cy-dv][cl]  = TileType.EntranceBoss;

                    //Then grow a corridor
                    addCorridor((int)trig.pos.x,(int)trig.pos.y,(int)trig.pos.z,(int)Orientation.toDxDy(trig.orientation).x , (int)Orientation.toDxDy(trig.orientation).y , height/2,0,b,t,e,r,s,m,priority,true, showoff , singleBranchMode ,singleBranchChanceMode,planeMode,csavoid);

                    System.out.println("Corridor Added");
                }
                System.out.println("Corridor Trigger Processed");
                break;

            case PGTrigger.TYPE_ADDRANDROOM:

                if(data[cx][cy][cl] == TileType.Empty){
                    data[cx-dh][cy-dv][cl] = TileType.Entrance;
                    data[cx][cy][cl] = TileType.Corridor;
                    placeRandomRoom((int)trig.pos.x,(int)trig.pos.y,(int)trig.pos.z,(int)Orientation.toDxDy(trig.orientation).x , (int)Orientation.toDxDy(trig.orientation).y , (trig.orientation==Orientation.Northbound || trig.orientation==Orientation.Southbound) ? TileType.Room2 : TileType.Room ,(trig.orientation==Orientation.Northbound || trig.orientation==Orientation.Southbound) ? TileType.Entrance2 : TileType.Entrance , 2,5, (trig.orientation==Orientation.Northbound || trig.orientation==Orientation.Southbound),showoff,planeMode  );
                    System.out.println("Room Added");
                }
                System.out.println("Room Trigger Processed");
                break;

            case PGTrigger.TYPE_ADDRANDMODL:

                if(data[cx][cy][cl] == TileType.Empty){
                    data[cx-dh][cy-dv][cl] = TileType.Entrance;
                    data[cx][cy][cl] = TileType.Corridor;
                    placeModel((int)trig.pos.x,(int)trig.pos.y,(int)trig.pos.z,(int)Orientation.toDxDy(trig.orientation).x , (int)Orientation.toDxDy(trig.orientation).y, Model.pickRandom(new Model[]{Model.BigCloset(), Model.BossRoom(), Model.TallCloset(), Model.Closet()}),priority,b,t,e,r,s,m,justBranched,showoff,singleBranchMode,singleBranchChanceMode,planeMode,csavoid);
                    System.out.println("Model Added");
                }

                System.out.println("Model Trigger Processed");

                break;

            default:break;
        }

    }
