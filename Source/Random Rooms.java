//Algorithm for random room placement, supply your own tile types
    //and the method will grow a room in the available space.
    //Includes a way to make a room of fixed size
    public void placeRandomRoom(int x, int y,int l, int dh, int dv, TileType roomType, TileType entranceType, int minsize, int maxsize , boolean messyMode, boolean showoffmode){

        boolean reachedDoor=false;
        lastAddedRooms = new ArrayList<>();

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

        if(checkRoomNeighbors(currentRoomX+dh,currentRoomY+dv,l,roomType,entranceType)) {
            data[currentEntranceX][currentEntranceY][l] = entranceType;
            lastAddedRooms.add(new Vector3f(currentEntranceX, currentEntranceY, l));
        }else{
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

                    //Check if room reached door
                    if(zy&&currentRoomX==currentEntranceX && currentRoomY==currentEntranceY+dv) {
                        reachedDoor = true;
                    }

                    else if(!zy&&currentRoomY==currentEntranceY && currentRoomX==currentEntranceX+dh) {
                        reachedDoor = true;
                    }



                    lastAddedRooms.add(new Vector3f(currentRoomX,currentRoomY,l));

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


                    if(!reachedDoor)
                        clearInvalidRoom();

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

    //Algorithm to remove room that has not reached door
    public void clearInvalidRoom(){
        for(Vector3f v : lastAddedRooms){
            data[(int)v.x][(int)v.y][(int)v.z] = TileType.Empty;
        }
    }