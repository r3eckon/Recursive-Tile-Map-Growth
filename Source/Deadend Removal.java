	//Algorithms for deadend removal

    //Find deadends by looking for corridors with less than
    //two accessible neighbors, by definition being deadends
    public Vector3f[] findDeadends(){

        ArrayList<Vector3f> deadends = new ArrayList<Vector3f>();
        Vector3f[] toreturn = new Vector3f[0];
        TileType current;

        for(int i =0;i<width;i++){
            for(int j =0; j< height; j++){
                for(int l=0;l<levels;l++){

                    current = data[i][j][l];

                    if( !(current == TileType.Corridor || current == TileType.Room || current == TileType.Stairs || current== TileType.Staircase || current == TileType.Room2 || current == TileType.Entrance || current == TileType.Entrance2 || current == TileType.EntranceBoss || current == TileType.EntranceCloset ))
                        continue;

                    cnc=0;

                    if(current != TileType.Stairs && current != TileType.Staircase){
                        getNeighbors(i,j,l);

                        if(currentNeighborhood.north!=TileType.Empty && currentNeighborhood.north != TileType.ERROR)
                            cnc++;
                        if(currentNeighborhood.south!=TileType.Empty && currentNeighborhood.south != TileType.ERROR)
                            cnc++;
                        if(currentNeighborhood.east!=TileType.Empty && currentNeighborhood.east != TileType.ERROR)
                            cnc++;
                        if(currentNeighborhood.west!=TileType.Empty && currentNeighborhood.west != TileType.ERROR)
                            cnc++;

                        if(cnc < 2 && (current == TileType.Corridor ||current==TileType.EntranceBoss || current == TileType.EntranceCloset))
                            deadends.add(new Vector3f(i,j,l));
                        else if(cnc < 1 && (current == TileType.Room||current == TileType.Room2)){
                            deadends.add(new Vector3f(i,j,l));
                        }
                    }else{

                        getNeighbors3D(i,j,l);

                        if(current == TileType.Staircase){
                            if(currentNeighborhood.north!=TileType.Empty && currentNeighborhood.north != TileType.ERROR)
                                cnc++;
                            if(currentNeighborhood.south!=TileType.Empty && currentNeighborhood.south != TileType.ERROR)
                                cnc++;
                            if(currentNeighborhood.east!=TileType.Empty && currentNeighborhood.east != TileType.ERROR)
                                cnc++;
                            if(currentNeighborhood.west!=TileType.Empty && currentNeighborhood.west != TileType.ERROR)
                                cnc++;
                            if(currentNeighborhood.above!=TileType.Empty && currentNeighborhood.above != TileType.ERROR)
                                cnc++;
                            if(currentNeighborhood.below!=TileType.Empty && currentNeighborhood.below != TileType.ERROR)
                                cnc++;

                            if(cnc >= 3){
                                continue;
                            }else{

                                cnc = 0;

                                if(currentNeighborhood.north==TileType.Staircase)
                                    cnc++;
                                if(currentNeighborhood.south==TileType.Staircase)
                                    cnc++;
                                if(currentNeighborhood.east==TileType.Staircase)
                                    cnc++;
                                if(currentNeighborhood.west==TileType.Staircase)
                                    cnc++;
                                if(currentNeighborhood.above==TileType.Staircase)
                                    cnc++;
                                if(currentNeighborhood.below==TileType.Staircase)
                                    cnc++;

                                if(cnc >= 2)
                                    continue;
                                else
                                    deadends.add(new Vector3f(i,j,l));

                            }


                        }else if(current == TileType.Stairs){

                            if(currentNeighborhood.north==TileType.Staircase)
                                cnc++;
                            if(currentNeighborhood.south==TileType.Staircase)
                                cnc++;
                            if(currentNeighborhood.east==TileType.Staircase)
                                cnc++;
                            if(currentNeighborhood.west==TileType.Staircase)
                                cnc++;
                            if(currentNeighborhood.above==TileType.Staircase)
                                cnc++;
                            if(currentNeighborhood.below==TileType.Staircase)
                                cnc++;

                            if(cnc >= 2)
                                continue;
                            else
                                deadends.add(new Vector3f(i,j,l));

                        }


                    }




                }
            }
        }

        return deadends.toArray(toreturn);

    }

        ArrayList<Vector3f> deadends = new ArrayList<Vector3f>();
        Vector3f[] toreturn = new Vector3f[0];

        for(int i =0;i<width;i++){
            for(int j =0; j< height; j++){
                for(int l=0;l<levels;l++){


                    if(data[i][j][l] != TileType.Corridor)
                        continue;

                    cnc=0;

                    getNeighbors(i,j,l);

                    if(north!=TileType.Empty && north != TileType.ERROR)
                        cnc++;
                    if(south!=TileType.Empty && south != TileType.ERROR)
                        cnc++;
                    if(east!=TileType.Empty && east != TileType.ERROR)
                        cnc++;
                    if(west!=TileType.Empty && west != TileType.ERROR)
                        cnc++;


                    if(cnc < 2)
                        deadends.add(new Vector3f(i,j,l));


                }
            }
        }

        return deadends.toArray(toreturn);

    }

    //Removes deadends in the current list
    public void removeFoundDeadends(Vector3f[] deadends){
        for(Vector3f v : deadends){
            data[(int)v.x][(int)v.y][(int)v.z]=TileType.Empty;
        }
    }

    //Searches the map for deadends and removes them until the found count is 0
    public void removeAllDeadends(){

        Vector3f[] cd;

        while ((cd = findDeadends()).length > 0){
            removeFoundDeadends(cd);
        }

    }
