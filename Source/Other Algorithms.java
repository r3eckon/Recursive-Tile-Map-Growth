	//Method used to spawn corridors when 'G' is pressed.
	//Generates 4 corridors starting from a middle point
    public void generateMap( int x, int y , float b, float t, float e, float r, float s, float m , boolean p , boolean showoff, boolean singleBranchMode , boolean singleBranchChanceMode){

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

        addCorridor(x,y + 1, l,0,1,height/2,0,b,t,e,r,s,m,p,true, showoff , singleBranchMode ,singleBranchChanceMode);//Upwards
        addCorridor(x,y - 1, l,0,-1,height/2,0,b,t,e,r,s,m,p,true, showoff, singleBranchMode, singleBranchChanceMode);//Downwards
        addCorridor(x+1,y, l,1,0,width/2,0,b,t,e,r,s,m,p,true, showoff, singleBranchMode, singleBranchChanceMode);//Right
        addCorridor(x-1,y, l,-1,0,width/2,0,b,t,e,r,s,m,p,true, showoff,singleBranchMode, singleBranchChanceMode );//Left

        //Process post generation triggers


        if(pgtriggers!=null){

            currentlyProcessingTriggers=true;
            for(PGTrigger tg : pgtriggers){

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
                        addCorridor((int)tg.pos.x,(int)tg.pos.y,(int)tg.pos.z,(int)Orientation.toDxDy(tg.orientation).x , (int)Orientation.toDxDy(tg.orientation).y , height/2,0,b,t,e,r,s,m,p,true, showoff , singleBranchMode ,singleBranchChanceMode);
                        System.out.println("Corridor Trigger Processed");
                        break;

                    case PGTrigger.TYPE_ADDRANDROOM:
                        placeRandomRoom((int)tg.pos.x,(int)tg.pos.y,(int)tg.pos.z,(int)Orientation.toDxDy(tg.orientation).x , (int)Orientation.toDxDy(tg.orientation).y , (tg.orientation==Orientation.Northbound || tg.orientation==Orientation.Southbound) ? TileType.Room2 : TileType.Room ,(tg.orientation==Orientation.Northbound || tg.orientation==Orientation.Southbound) ? TileType.Entrance2 : TileType.Entrance , 2,5, (tg.orientation==Orientation.Northbound || tg.orientation==Orientation.Southbound),showoff  );
                        break;

                    case PGTrigger.TYPE_ADDRANDMODL:
                        placeModel((int)tg.pos.x,(int)tg.pos.y,(int)tg.pos.z,(int)Orientation.toDxDy(tg.orientation).x , (int)Orientation.toDxDy(tg.orientation).y, Model.pickRandom(new Model[]{Model.BigCloset(), Model.BossRoom(), Model.TallCloset(), Model.Closet()}),false);
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
    public void placeModel(int x, int y, int l, int dh, int dv, Model model , boolean priority){

        int sx, sy, sl, cx, cy, cl , tx,ty,tl; //indices

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

                    if(!isBoss &&model.model[i][j][k]==TileType.BossRoom )
                        isBoss=true;

                    data[cx][cy][cl] = model.model[i][j][k];

                }
            }
        }

        //New trigger detection process
        //Generalized for all trigger type, orientation and position

        if(model.triggers!=null){

            for(PGTrigger trig : model.triggers){

                //Initialize by setting trigger position in data
                tx = sx + (int)trig.pos.x;
                ty = sy + (int)trig.pos.y;
                tl = sl + (int)trig.pos.z;

                //First calculate the correct orientation
                triggerOrientation = Orientation.rotate(trig.orientation , modelOrientation);

                //Next, process based on trigger type
                //First check if were not already processing triggers
                //Then add the new trigger with it's data on the map
                //Finish by adding needed tile types on the map
                switch (trig.type){

                    case PGTrigger.TYPE_ADDCORRIDOR:
                        if(!currentlyProcessingTriggers){
                            pgtriggers.add(new PGTrigger(new Vector3f(tx+dh,ty+dv,tl), triggerOrientation , PGTrigger.TYPE_ADDCORRIDOR ));
                            data[tx][ty][tl] = TileType.EntranceBoss;
                        }
                        break;

                    case PGTrigger.TYPE_ADDRANDROOM:
                        if(!currentlyProcessingTriggers){
                            pgtriggers.add(new PGTrigger(new Vector3f(tx+dh,ty+dv,tl), triggerOrientation , PGTrigger.TYPE_ADDRANDROOM ));
                            data[tx][ty][tl] = TileType.EntranceBoss;
                            data[tx+dh][ty+dv][tl] = TileType.Corridor;
                        }

                }

            }

        }




    }
	
