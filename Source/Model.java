//Class handling model rotation and creation
//Most methods are static, the important part is the 
//data held inside the model[][][] array as well as the
//Origin points. If those are valid, rotation will work as intended.
public class Model {

    public int ox,oy,ol;//Origin

    TileType[][][] model;//Model data

    TileType[] compatibleTypes;//Compatible type list for neighbor checking

    TileType[] typesToCheck;//Tile types needing neighbor check if they are on the model

    public int width,height,depth;//Bounds

    public Orientation orientation;//Current Orientation

    static int rix,riy;//Current rotation parameters
    static TileType[][][] buildData; //Current model data

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

    public Model(){};

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
        return newModel;
    }

    public static Model rotate( Model original, Orientation newOrientation){

        Model rotated = Model.copy(original);

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
                            riy = i;


                            rotated.width=original.height;
                            rotated.height=original.width;

                            if(i == original.ox && j == original.oy){
                                rotated.ox=rix;
                                rotated.oy =riy;
                            }

                            rotated.model[rix][riy][l]= original.model[i][j][l];
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
                            riy = (original.width-1)-i;


                            rotated.width=original.height;
                            rotated.height=original.width;

                            if(i == original.ox && j == original.oy){
                                rotated.ox=rix;
                                rotated.oy=riy;
                            }

                            rotated.model[rix][riy][l]= original.model[i][j][l];
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