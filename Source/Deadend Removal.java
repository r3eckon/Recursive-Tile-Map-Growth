	//Algorithms for deadend removal

    //Find deadends by looking for corridors with less than
    //two accessible neighbors, by definition being deadends
    public Vector3f[] findDeadends(){

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
