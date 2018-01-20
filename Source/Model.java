//Class handling model rotation and creation
//Most methods are static, the important part is the 
//data held inside the model[][][] array as well as the
//Origin points. If those are valid, rotation will work as intended.
//Updated to include some hand crafted model array as examples
public class Model {

    public int ox,oy,ol;//Origin

    TileType[][][] model;//Model data

    TileType[] compatibleTypes;//Compatible type list for neighbor checking

    TileType[] typesToCheck;//Tile types needing neighbor check if they are on the model

    public int width,height,depth;//Bounds

    public Orientation orientation;//Current Orientation

    public PGTrigger[] triggers;

    static int rix,riy;//Current rotation parameters
    static TileType[][][] buildData; //Current model data
    static ArrayList<PGTrigger> triggerList;

    //Constructors

    //Fully featured model with triggers
    public Model(int ox, int oy, int ol, TileType[][][] model , TileType[] compatibleTypes , TileType[] types, PGTrigger[] trigs ){

        this.ox=ox;
        this.oy=oy;
        this.ol=ol;
        this.model=model;
        this.compatibleTypes=compatibleTypes;
        this.typesToCheck=types;
        this.triggers = trigs;


        depth= model[0][0].length;
        height = model[0].length;
        width = model.length;
        orientation = Orientation.Northbound;

    }

    //Model without any triggers
    public Model(int ox, int oy, int ol, TileType[][][] model , TileType[] compatibleTypes , TileType[] types ){

        this.ox=ox;
        this.oy=oy;
        this.ol=ol;
        this.model=model;
        this.compatibleTypes=compatibleTypes;
        this.typesToCheck=types;

        depth= model[0][0].length;
        height = model[0].length;
        width = model.length;
        orientation = Orientation.Northbound;

    }

    //Model without compatible/incompatible types
    public Model(int ox, int oy, int ol, TileType[][][] model  ){

        this.ox=ox;
        this.oy=oy;
        this.ol=ol;
        this.model=model;

        depth= model[0][0].length;
        height = model[0].length;
        width = model.length;
        orientation = Orientation.Northbound;

    }

    //Empty model class
    public Model(){};


    //Helper methods

    public static Model copy(Model m){
        Model newModel = new Model();
        newModel.ox=m.ox;
        newModel.oy=m.oy;
        newModel.ol=m.ol;


        newModel.depth= m.model[0][0].length;
        newModel.height = m.model[0].length;
        newModel.width = m.model.length;

        newModel.model = new TileType[newModel.width][newModel.height][newModel.depth];

        for(int i = 0; i < m.width; i++){
            for(int j = 0; j < m.height; j++){
                for(int l = 0; l < m.depth; l++){
                    newModel.model[i][j][l] = m.model[i][j][l];
                }
            }
        }

        newModel.orientation = Orientation.Northbound;
        if(m.compatibleTypes!=null){
            newModel.compatibleTypes = new TileType[m.compatibleTypes.length];
            newModel.typesToCheck = new TileType[m.typesToCheck.length];
            for(int i=0;i<m.compatibleTypes.length;i++){
                newModel.compatibleTypes[i]=m.compatibleTypes[i];
            }

            for(int j = 0; j< m.typesToCheck.length; j++){
                newModel.typesToCheck[j]=m.typesToCheck[j];
            }
        }

        if(m.triggers != null){
            newModel.triggers = new PGTrigger[m.triggers.length];
            for(int i=0;i<m.triggers.length;i++){
                newModel.triggers[i] = m.triggers[i];
            }
        }

        return newModel;
    }

    //Model rotation
    //Updated to also rotate non tile type based PGTriggers
    public static Model rotate( Model original, Orientation newOrientation){

        Model rotated = Model.copy(original);
        ArrayList<PGTrigger> newTriggers = new ArrayList<>();

        switch (newOrientation){
            case Northbound:
                break;
            case Southbound:
                for(int j = 0; j<original.height;j++){
                    for(int i = 0; i<original.width;i++){
                        for(int l = 0; l<original.depth;l++){

                            rix = (original.width-1)-i;
                            riy = (original.height-1)-j;

                            if(rix == original.ox && riy == original.oy){
                                rotated.ox=i;
                                rotated.oy=j;
                            }

                            rotated.model[rix][riy][l]= original.model[i][j][l];

                            //Rotate the PGTriggers on the model
                            if(original.triggers!=null){
                                for(PGTrigger t : original.triggers){
                                    if(t.pos.x == i && t.pos.y == j && t.pos.z == l){
                                        newTriggers.add(new PGTrigger(new Vector3f(rix,riy,l) , t.orientation , t.type));
                                    }
                                }

                                rotated.triggers = newTriggers.toArray(new PGTrigger[]{});
                            }



                        }

                    }
                }


                rotated.orientation=Orientation.Southbound;
                break;

            case Eastbound:

                rotated.model = new TileType[original.height][original.width][original.depth];

                for(int j = 0; j < original.height; j++){
                    for(int i = 0; i < original.width; i++){
                        for(int l = 0; l < original.depth;l++){

                            rix = j;
                            riy = (original.width-1)-i;


                            rotated.width=original.height;
                            rotated.height=original.width;

                            if(i == original.ox && j == original.oy){
                                rotated.ox=rix;
                                rotated.oy =riy;
                            }

                            rotated.model[rix][riy][l]= original.model[i][j][l];

                            //Rotate the PGTriggers on the model
                            if(original.triggers!=null){
                                for(PGTrigger t : original.triggers){
                                    if(t.pos.x == i && t.pos.y == j && t.pos.z == l){
                                        newTriggers.add(new PGTrigger(new Vector3f(rix,riy,l) , t.orientation , t.type));
                                    }
                                }
                                rotated.triggers = newTriggers.toArray(new PGTrigger[]{});
                            }

                        }
                    }
                }
                rotated.orientation = Orientation.Eastbound;
                break;

            case Westbound:
                rotated.model = new TileType[original.height][original.width][original.depth];

                for(int i = 0; i < original.width; i++) {
                    for (int j = 0; j < original.height; j++) {
                        for (int l = 0; l < original.depth; l++) {

                            rix = (original.height-1)-j;
                            riy = i;


                            rotated.width=original.height;
                            rotated.height=original.width;

                            if(i == original.ox && j == original.oy){
                                rotated.ox=rix;
                                rotated.oy=riy;
                            }

                            rotated.model[rix][riy][l]= original.model[i][j][l];

                            //Rotate the PGTriggers on the model
                            if(original.triggers!=null){
                                for(PGTrigger t : original.triggers){
                                    if(t.pos.x == i && t.pos.y == j && t.pos.z == l){
                                        newTriggers.add(new PGTrigger(new Vector3f(rix,riy,l) , t.orientation , t.type));
                                    }
                                }
                                rotated.triggers = newTriggers.toArray(new PGTrigger[]{});
                            }

                        }
                    }
                }
                rotated.orientation = Orientation.Westbound;
                break;


        }

        return rotated;
    }

    public static Model pickRandom(Model[] models){
        return models[Main.rand.nextInt(models.length)];
    }

    public boolean neighborCheck(TileType n, TileType s, TileType e, TileType w){

        boolean northok = false, southok=false, eastok=false, westok=false;

        for(TileType t : compatibleTypes){
            if(!northok){
                northok=t==n;
            }
            if(!southok){
                southok=t==s;
            }
            if(!eastok){
                eastok=t==e;
            }
            if(!westok){
                westok=t==w;
            }
        }

        return (northok&&southok&&westok&&eastok);
    }

    //Below are some manually created model prefabs

    public static Model Closet(){

        buildData = new TileType[3][4][1];

        buildData[0][0][0] = TileType.Empty;         
        buildData[1][0][0] = TileType.EntranceCloset;
        buildData[2][0][0] = TileType.Empty;         

        buildData[0][1][0] = TileType.RoomCloset;    
        buildData[1][1][0] = TileType.RoomCloset;    
        buildData[2][1][0] = TileType.Empty;        

        buildData[0][2][0] = TileType.RoomCloset;   
        buildData[1][2][0] = TileType.RoomCloset;    
        buildData[2][2][0] = TileType.Empty;        

        buildData[0][3][0] = TileType.Empty;//ADDING NORTHBOUND TRIGGER HERE
        buildData[1][3][0] = TileType.Empty;         
        buildData[2][3][0] = TileType.Empty;         

        triggerList = new ArrayList<>();
        triggerList.add(new PGTrigger(new Vector3f(0,3,0) , Orientation.Northbound , PGTrigger.TYPE_ADDRANDROOM));

        return new Model(1,0,0 , buildData , new TileType[]{TileType.Empty }, new TileType[]{TileType.RoomCloset} , triggerList.toArray(new PGTrigger[]{}));
    }

    public static Model TallCloset(){

        buildData = new TileType[3][3][3];

        buildData[0][0][0] = TileType.Empty;
        buildData[1][0][0] = TileType.Empty;
        buildData[2][0][0] = TileType.Empty;

        buildData[0][1][0] = TileType.RoomCloset;
        buildData[1][1][0] = TileType.RoomCloset;
        buildData[2][1][0] = TileType.RoomCloset;

        buildData[0][2][0] = TileType.RoomCloset;    
        buildData[1][2][0] = TileType.RoomCloset;    
        buildData[2][2][0] = TileType.RoomCloset;    

        buildData[0][0][1] = TileType.Empty;         
        buildData[1][0][1] = TileType.EntranceCloset;
        buildData[2][0][1] = TileType.Empty;        

        buildData[0][1][1] = TileType.RoomCloset;    
        buildData[1][1][1] = TileType.RoomCloset;    
        buildData[2][1][1] = TileType.RoomCloset;    

        buildData[0][2][1] = TileType.RoomCloset;    
        buildData[1][2][1] = TileType.RoomCloset;    
        buildData[2][2][1] = TileType.RoomCloset;    

        buildData[0][0][2] = TileType.Empty;         
        buildData[1][0][2] = TileType.Empty;         
        buildData[2][0][2] = TileType.Empty;         

        buildData[0][1][2] = TileType.RoomCloset;    
        buildData[1][1][2] = TileType.RoomCloset;    
        buildData[2][1][2] = TileType.RoomCloset;    

        buildData[0][2][2] = TileType.RoomCloset;   
        buildData[1][2][2] = TileType.RoomCloset;    
        buildData[2][2][2] = TileType.RoomCloset;    


        return new Model(1,0,1 , buildData , new TileType[]{TileType.Empty }, new TileType[]{TileType.RoomCloset});
    }

    public static Model BigCloset(){

        buildData = new TileType[6][6][1];

        buildData[0][0][0] = TileType.Empty;         
        buildData[1][0][0] = TileType.Empty;         
        buildData[2][0][0] = TileType.EntranceCloset;
        buildData[3][0][0] = TileType.Empty;        
        buildData[4][0][0] = TileType.Empty;        
        buildData[5][0][0] = TileType.Empty;         

        buildData[0][1][0] = TileType.RoomCloset;   
        buildData[1][1][0] = TileType.RoomCloset;   
        buildData[2][1][0] = TileType.RoomCloset;  
        buildData[3][1][0] = TileType.RoomCloset;   
        buildData[4][1][0] = TileType.RoomCloset;    
        buildData[5][1][0] = TileType.RoomCloset;    

        buildData[0][2][0] = TileType.RoomCloset;        
        buildData[1][2][0] = TileType.EntranceCloset;   
        buildData[2][2][0] = TileType.EntranceCloset;   
        buildData[3][2][0] = TileType.EntranceCloset;   
        buildData[4][2][0] = TileType.EntranceCloset;   
        buildData[5][2][0] = TileType.RoomCloset;   

        buildData[0][3][0] = TileType.RoomCloset;  
        buildData[1][3][0] = TileType.EntranceCloset;  
        buildData[2][3][0] = TileType.RoomCloset;   
        buildData[3][3][0] = TileType.RoomCloset;   
        buildData[4][3][0] = TileType.EntranceCloset;   
        buildData[5][3][0] = TileType.RoomCloset;   

        buildData[0][4][0] = TileType.RoomCloset;   
        buildData[1][4][0] = TileType.EntranceCloset;   
        buildData[2][4][0] = TileType.EntranceCloset;    
        buildData[3][4][0] = TileType.EntranceCloset;  
        buildData[4][4][0] = TileType.EntranceCloset;   
        buildData[5][4][0] = TileType.RoomCloset;   

        buildData[0][5][0] = TileType.RoomCloset;   
        buildData[1][5][0] = TileType.RoomCloset;    
        buildData[2][5][0] = TileType.RoomCloset;    
        buildData[3][5][0] = TileType.RoomCloset;    
        buildData[4][5][0] = TileType.RoomCloset;   
        buildData[5][5][0] = TileType.RoomCloset;   


        return new Model(2,0,0 , buildData,new TileType[]{TileType.Empty }, new TileType[]{TileType.RoomCloset});
    }

    public static Model BossRoom(){

        buildData = new TileType[10][10][2];

        buildData[0][0][0] = TileType.Empty;      
        buildData[1][0][0] = TileType.Empty;         
        buildData[2][0][0] = TileType.Empty;  
        buildData[3][0][0] = TileType.Empty;  
        buildData[4][0][0] = TileType.EntranceBoss;         
        buildData[5][0][0] = TileType.EntranceBoss;         
        buildData[6][0][0] = TileType.Empty;        
        buildData[7][0][0] = TileType.Empty;       
        buildData[8][0][0] = TileType.Empty;       
        buildData[9][0][0] = TileType.Empty;        

        for(int i = 1; i < 10; i++){
            buildData[0][i][0] = TileType.BossRoom;    
            buildData[1][i][0] = TileType.BossRoom;        
            buildData[2][i][0] = TileType.BossRoom; 
            buildData[3][i][0] = TileType.BossRoom;  
            buildData[4][i][0] = TileType.BossRoom;         
            buildData[5][i][0] = TileType.BossRoom;        
            buildData[6][i][0] = TileType.BossRoom;         
            buildData[7][i][0] = TileType.BossRoom;        
            buildData[8][i][0] = TileType.BossRoom;        
            buildData[9][i][0] = TileType.BossRoom;         
        }

        for(int i = 0; i < 9; i++){
            buildData[0][i][1] = TileType.BossRoom;        
            buildData[1][i][1] = TileType.BossRoom;      
            buildData[2][i][1] = TileType.BossRoom; 
            buildData[3][i][1] = TileType.BossRoom; 
            buildData[4][i][1] = TileType.BossRoom;       
            buildData[5][i][1] = TileType.BossRoom;       
            buildData[6][i][1] = TileType.BossRoom;         
            buildData[7][i][1] = TileType.BossRoom;       
            buildData[8][i][1] = TileType.BossRoom;       
            buildData[9][i][1] = TileType.BossRoom;        
        }

        buildData[0][9][1] = TileType.Empty;        
        buildData[1][9][1] = TileType.Empty;         
        buildData[2][9][1] = TileType.Empty;       
        buildData[3][9][1] = TileType.Empty;        
        buildData[4][9][1] = TileType.Empty; //TRIGGER HERE
        buildData[5][9][1] = TileType.Empty; //TRIGGER HERE
        buildData[6][9][1] = TileType.Empty;        
        buildData[7][9][1] = TileType.Empty;         
        buildData[8][9][1] = TileType.Empty;         
        buildData[9][9][1] = TileType.Empty;         

        triggerList = new ArrayList<PGTrigger>();
        triggerList.add(new PGTrigger(new Vector3f(4,9,1) , Orientation.Northbound , PGTrigger.TYPE_ADDCORRIDOR));
        triggerList.add(new PGTrigger(new Vector3f(5,9,1) , Orientation.Northbound , PGTrigger.TYPE_ADDCORRIDOR));


        return new Model(5,0,0 , buildData,new TileType[]{TileType.Empty }, new TileType[]{TileType.BossRoom} , triggerList.toArray(new PGTrigger[]{}));
    }



}