import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Example {
    public static String Src;
    public static String Dest;
}
public class BidirectionalBFS {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int Row = scanner.nextInt();
        int Col = scanner.nextInt();

        //table matrix
        String[][] matrix = new String[Row][Col];
        String[][] tempMatrix = new String[Row][Col];

        String Robot = new String();
        ArrayList<String> Butters = new ArrayList<>();
        ArrayList<String> Plates = new ArrayList<>();
        //getting input
        for (int i = 0; i < Row; i++)
            for (int j = 0; j < Col; j++) {
                matrix[i][j] = scanner.next();
                tempMatrix[i][j] = matrix[i][j];
                if (matrix[i][j].contains("r"))
                    Robot = i + "," + j;
                if (matrix[i][j].contains("b"))
                    Butters.add(i + "," + j);
                if (matrix[i][j].contains("p"))
                    Plates.add(i + "," + j);
            }

        ArrayList<ArrayList<String>> temp = FindButterToPlate(Butters,Plates,tempMatrix,Row,Col);
        ArrayList<ArrayList<String>> butterToPlate = new ArrayList<>();
        for(int i = 0 ; i < temp.size(); i++) {
            if(temp.get(i).size() != 0)
                butterToPlate.add(makeWay(temp.get(i), matrix, Row, Col));
        }
        ArrayList<ArrayList<String>> finalPath = FindRobotToButter(Robot,butterToPlate,matrix,Row,Col);
        for(int i = 0 ; i < finalPath.size(); i++) {
            System.out.println("\n\ngoing for the butter : " + (i + 1) + "\n\n");
            terminalShow(finalPath.get(i), matrix, Row, Col);
            System.out.println("\nmax depth from robot to plate" + (i + 1) + " : " + finalPath.get(i).size());
            System.out.println();
            printDirection(finalPath.get(i));
        }
        for(int i = 0; i < Butters.size() - finalPath.size() ; i++)
            System.out.println("\033[1;31m" + "can not pass the butter!!!" + "\033[0m");
    }

    public static ArrayList<String> FindNeighborsForward(String location, String[][] matrix, int Row, int Col, boolean special) {
        int row = Integer.parseInt(location.split(",")[0]);
        int col = Integer.parseInt(location.split(",")[1]);
        ArrayList<String> neighbors = new ArrayList<>();

        //top of src
        if (row - 1 >= 0)
            if (!matrix[row - 1][col].contains("x") && !matrix[row - 1][col].contains("b"))
                if (special) {
                    if (row + 1 < Row && (!matrix[row + 1][col].contains("x") && !matrix[row + 1][col].contains("b")))
                        neighbors.add((row - 1) + "," + col);
                } else
                    neighbors.add((row - 1) + "," + col);


        //down of src
        if (row + 1 < Row)
            if (!matrix[row + 1][col].contains("x") && !matrix[row + 1][col].contains("b"))
                if (special) {
                    if (row - 1 >= 0 && (!matrix[row - 1][col].contains("x") && !matrix[row - 1][col].contains("b")))
                        neighbors.add((row + 1) + "," + col);
                } else
                    neighbors.add((row + 1) + "," + col);


        //left of src
        if (col - 1 >= 0)
            if (!matrix[row][col - 1].contains("x") && !matrix[row][col - 1].contains("b"))
                if (special) {
                    if (col + 1 < Col && (!matrix[row][col + 1].contains("x") && !matrix[row][col + 1].contains("b")))
                        neighbors.add(row + "," + (col - 1));
                } else
                    neighbors.add(row + "," + (col - 1));


        //right of src
        if (col + 1 < Col)
            if (!matrix[row][col + 1].contains("x") && !matrix[row][col + 1].contains("b"))
                if (special) {
                    if (col - 1 >= 0 && (!matrix[row][col - 1].contains("x") && !matrix[row][col - 1].contains("b")))
                        neighbors.add(row + "," + (col + 1));
                } else
                    neighbors.add(row + "," + (col + 1));

        return neighbors;
    }
    public static ArrayList<String> FindNeighborsBackWard(String location, String[][] matrix, int Row, int Col, boolean special) {
        int row = Integer.parseInt(location.split(",")[0]);
        int col = Integer.parseInt(location.split(",")[1]);
        ArrayList<String> neighbors = new ArrayList<>();


        //top of src
        if (row - 1 >= 0)
            if (!matrix[row - 1][col].contains("x") && !matrix[row - 1][col].contains("b"))
                if (special) {
                    if (row - 2 >= 0 && (!matrix[row - 2][col].contains("x") && !matrix[row - 2][col].contains("b")))
                        neighbors.add((row - 1) + "," + col);
                } else
                    neighbors.add((row - 1) + "," + col);


        //down of src
        if (row + 1 < Row)
            if (!matrix[row + 1][col].contains("x") && !matrix[row + 1][col].contains("b"))
                if (special) {
                    if (row + 2 < Row && (!matrix[row + 2][col].contains("x") && !matrix[row + 2][col].contains("b")))
                        neighbors.add((row + 1) + "," + col);
                } else
                    neighbors.add((row + 1) + "," + col);


        //left of src
        if (col - 1 >= 0)
            if (!matrix[row][col - 1].contains("x") && !matrix[row][col - 1].contains("b"))
                if (special) {
                    if (col - 2 >= 0 && (!matrix[row][col - 2].contains("x") && !matrix[row][col - 2].contains("b")))
                        neighbors.add(row + "," + (col - 1));
                } else
                    neighbors.add(row + "," + (col - 1));


        //right of src
        if (col + 1 < Col)
            if (!matrix[row][col + 1].contains("x") && !matrix[row][col + 1].contains("b"))
                if (special) {
                    if (col + 2 < Col && (!matrix[row][col + 2].contains("x") && !matrix[row][col + 2].contains("b")))
                        neighbors.add(row + "," + (col + 1));
                } else
                    neighbors.add(row + "," + (col + 1));

        return neighbors;
    }

    public static String FindThirdLocation(String first, String second) {

        int FirstRow = Integer.parseInt(first.split(",")[0]);
        int FirstCol = Integer.parseInt(first.split(",")[1]);

        int SecondRow = Integer.parseInt(second.split(",")[0]);
        int SecondCol = Integer.parseInt(second.split(",")[1]);

        if (FirstRow == SecondRow && SecondCol > FirstCol) {
            return FirstRow + "," + (FirstCol - 1);
        }

        if (FirstRow == SecondRow && SecondCol < FirstCol) {
            return FirstRow + "," + (FirstCol + 1);
        }

        if (FirstCol == SecondCol && FirstRow > SecondRow) {
            return (FirstRow + 1) + "," + FirstCol;
        } else
            return (FirstRow - 1) + "," + FirstCol;

    }

    public static String FindDirection(String first, String second) {

        int FirstRow = Integer.parseInt(first.split(",")[0]);
        int FirstCol = Integer.parseInt(first.split(",")[1]);

        int SecondRow = Integer.parseInt(second.split(",")[0]);
        int SecondCol = Integer.parseInt(second.split(",")[1]);

        if (FirstRow == SecondRow && SecondCol > FirstCol) {

            return "R";
        }

        if (FirstRow == SecondRow && SecondCol < FirstCol) {

            return "L";
        }

        if (FirstCol == SecondCol && FirstRow > SecondRow) {

            return "U";
        } else
            return "D";
    }
    public static int PositionToInt(String location,int Row,int Col){
        int row = Integer.parseInt(location.split(",")[0]);
        int col = Integer.parseInt(location.split(",")[1]);
        int counter = 0;
        for(int i = 0; i < Row; i++)
            for(int j = 0 ; j < Col; j++){
                if(i == row && j == col)
                    return counter;
                counter++;
            }
        return 0;
    }
    public static String PositionToString(int location,int Row,int Col){
        int counter = 0;
        for(int i = 0 ; i < Row; i++)
            for(int j = 0; j < Col; j++){
                if(location == counter)
                    return i + "," + j;
                counter ++;
            }
        return "";
    }

    public static ArrayList<String> BIBFS(String location, String target, String[][] matrix, int Row, int Col, boolean special) {
        if(location.equals(target))
            return null;
        ArrayList<String> path = new ArrayList<>();
        addButter(location,matrix);
        Graph G = new Graph(Row, Col, special,matrix);
        int src = PositionToInt(location,Row,Col);
        int dest = PositionToInt(target,Row,Col);
        boolean[] Visited1 = new boolean[Row * Col];
        boolean[] Visited2 = new boolean[Row * Col];
        int[] parent1 = new int[Row * Col];
        int[] parent2 = new int[Row * Col];
        for (int i = 0; i < Row * Col; i++) {
            Visited1[i] = false;
            Visited2[i] = false;
            parent1[i] = -1;
            parent2[i] = -1;
        }
        ArrayList<Integer> queue1 = new ArrayList<>();
        ArrayList<Integer> queue2 = new ArrayList<>();
        queue1.add(src);
        Visited1[src] = true;
        queue2.add(dest);
        Visited2[dest] = true;
        int intersection = -1;
        while (queue1.size() > 0 && queue2.size() > 0 && intersection == -1) {
            G.BFS(queue1, Visited1, parent1,false);
            G.BFS(queue2, Visited2, parent2,true);

            for (int i = 0; i < Row * Col; i++) {
                if (Visited1[i] && Visited2[i]) {  //checking intersection
                    intersection = i;
                    break;
                }
            }
        }

        if (intersection == -1)
            return null;

        ArrayList<String> path1 = new ArrayList<>();
        int j = intersection;
        while (j != -1) {
            path1.add(PositionToString(j,Row,Col));
            j = parent1[j];
        }
        ArrayList<String> path2 = new ArrayList<>();
        j = parent2[intersection];
        while (j != -1) {
            path2.add(PositionToString(j,Row,Col));
            j = parent2[j];
        }
        for (int i = path1.size() - 1 ; i >= 0; i--) {
            path.add(path1.get(i));
        }
        for (String string : path2) {
            path.add(string);
        }
        removeButter(location,matrix);
        return path;
    }

    static class Graph {
        String[][] matrix;
        int row;
        int col;
        boolean special;
        ArrayList<Integer>[] Adj; // adjacency list

        Graph(int row, int col, boolean special,String[][] Matrix) {
            this.row = row;
            this.col = col;
            this.special = special;
            this.matrix = Matrix;
            Adj = new ArrayList[row * col];
            for (int i = 0; i < row * col; i++) {
                Adj[i] = new ArrayList<>();
            }
        }

        void add_edge(int src,boolean back) {
                ArrayList<String> temp;
                if(back){
                    temp = FindNeighborsBackWard(PositionToString(src,row,col), matrix, row, col, special);
                }
                else{
                    temp = FindNeighborsForward(PositionToString(src,row,col), matrix, row, col, special);
                }
                for (String string : temp) {
                    Adj[src].add(PositionToInt(string,row,col));
                }
        }

        void BFS(ArrayList<Integer> queue, boolean[] visited, int[] parent,boolean back) {
            int current = queue.remove(0);
            add_edge(current,back);
            for (int i = 0; i < Adj[current].size(); i++) {
                int x = Adj[current].get(i);
                if (!visited[x]) {
                    queue.add(x);
                    visited[x] = true;
                    parent[x] = current;
                }
            }
        }
    }
    public static int calculateManhatan(String src,String goal){
        int goalRow = Integer.parseInt(goal.split(",")[0]);
        int goalCol = Integer.parseInt(goal.split(",")[1]);

        int srcRow = Integer.parseInt(src.split(",")[0]);
        int srcCol = Integer.parseInt(src.split(",")[1]);

        return Math.abs(goalCol-srcCol)+Math.abs(goalRow-srcRow);

    }
    private static void terminalShow(ArrayList<String> finalPath, String[][] matrix,int row ,int col) {
        for (String location : finalPath){
            for(int i = 0 ; i < row ; i++){
                for (int j = 0 ; j < col ; j++)
                    if(i == Integer.parseInt(location.split(",")[0]) && j == Integer.parseInt(location.split(",")[1]))
                        System.out.printf("\u001B[33m%5s\u001B[0m",matrix[i][j]);
                    else
                        System.out.printf("%5s",matrix[i][j]);
                System.out.println("");}
            System.out.println("\n\n\n");

        }

        for (int i = 0 ; i < row ; i++) {
            for (int j = 0; j < col; j++) {
                String location = i + "," + j;
                if(finalPath.contains(location))
                    System.out.printf("\u001B[33m%5s\u001B[0m",matrix[i][j]);
                else
                    System.out.printf("%5s",matrix[i][j]);

            }
            System.out.println();
        }
    }

    public static String findDirection(String first, String second) {

        String nexLocation ;

        int firstRow = Integer.parseInt(first.split(",")[0]);
        int firstCol = Integer.parseInt(first.split(",")[1]);

        int secRow = Integer.parseInt(second.split(",")[0]);
        int secCol = Integer.parseInt(second.split(",")[1]);

        if(firstRow == secRow && secCol > firstCol){
            nexLocation = firstRow + "," + (firstCol - 1);
            return nexLocation;}

        if(firstRow == secRow && secCol < firstCol){
            nexLocation = firstRow + "," + (firstCol + 1);
            return nexLocation;}

        if(firstCol == secCol && firstRow > secRow){
            nexLocation = (firstRow + 1) + "," + firstCol;
            return nexLocation; }

        else
            return (firstRow - 1) + "," + firstCol;
    }
    public static ArrayList<ArrayList<String>> FindButterToPlate(ArrayList<String> butters, ArrayList<String> plates, String[][] matrix, int row, int col){
        HashMap<String,ArrayList<ArrayList<String>>> paths = new HashMap<>();
        ArrayList<ArrayList<String>> finalPaths = new ArrayList<>();

        for(int i = 0; i < butters.size() ; i++){
             paths.put(butters.get(i),new ArrayList<>());
             finalPaths.add(new ArrayList<>());
        }

        for(int i = 0 ;i < butters.size(); i++){
            for(int j = 0 ; j < plates.size(); j++) {
                ArrayList<String> temp = BIBFS(butters.get(i), plates.get(j), matrix, row, col, true);
                if(temp != null) {
                    paths.get(butters.get(i)).add(temp);
                }
            }
        }
        boolean [] butterIndexes = new boolean[butters.size()];
        boolean [] platesIndexes = new boolean[plates.size()];
        for(int i = 0 ; i < butterIndexes.length; i++) {
            butterIndexes[i] = false;
            platesIndexes[i] = false;
        }

        for(int i = 0; i < butters.size() ; i ++){
            int min = plates.size() + 1;
            int minIndex = -1;
            for(int j = 0 ; j < butters.size() ; j++){
                if(paths.get(butters.get(j)).size() < min && paths.get(butters.get(j)).size() != 0 && !butterIndexes[j]){
                    min = paths.get(butters.get(j)).size();
                    minIndex = j;
                }
            }

            int minPath = row * col + 1;
            int minPathIndex = -1;
            if(minIndex == -1)
                break;
            for(int k = 0 ; k < paths.get(butters.get(minIndex)).size() ; k++){
                if(paths.get(butters.get(minIndex)).get(k).size() < minPath && !platesIndexes[checkPlate(plates,paths.get(butters.get(minIndex)).get(k))]){
                    minPath = paths.get(butters.get(minIndex)).get(k).size();
                    minPathIndex = k;
                }
            }
            setPlate(butters,minIndex,plates,checkPlate(plates,paths.get(butters.get(minIndex)).get(minPathIndex)),butterIndexes,platesIndexes,matrix);
            finalPaths.add(paths.get(butters.get(minIndex)).get(minPathIndex));
        }
        return finalPaths;
    }
    public static int checkPlate(ArrayList<String> plates,ArrayList<String> path) {
        for (int i = 0; i < plates.size(); i++)
            if (plates.get(i).equals(path.get(path.size() - 1)))
                    return i;
        return -1;
    }
    public static void setPlate(ArrayList<String> butters,int butterIndex,ArrayList<String> plates,int plateIndex,boolean[] buttersIndexes,boolean[] plateIndexes,String[][] matrix){
        buttersIndexes[butterIndex] = true;
        String butter = butters.get(butterIndex);
        replaceButter(butter,matrix);

        plateIndexes[plateIndex] = true;
        String plate = plates.get(plateIndex);
        placeButter(plate,matrix);
    }
    public static void removeButter(String first,String[][] matrix) {
        int firstRow = Integer.parseInt(first.split(",")[0]);
        int firstCol = Integer.parseInt(first.split(",")[1]);

        matrix[firstRow][firstCol] = matrix[firstRow][firstCol].replace("B","b");
    }

    /**
     * this method get location and add a butter to it
     * @param first
     */
    public static void addButter(String first,String[][] matrix) {
        int firstRow = Integer.parseInt(first.split(",")[0]);
        int firstCol = Integer.parseInt(first.split(",")[1]);

        matrix[firstRow][firstCol] = matrix[firstRow][firstCol].replace("b","B");
    }
    public static ArrayList<ArrayList<String>> FindRobotToButter(String Robot,ArrayList<ArrayList<String>> butterToPlates, String[][] matrix, int row, int col){
        ArrayList<ArrayList<String>> path = new ArrayList<>();
        boolean[] butterIndexes = new boolean[butterToPlates.size()];
        for(int i = 0 ; i < butterToPlates.size(); i++) {
            path.add(new ArrayList<>());
            butterIndexes[i] = false;
        }
        int min;
        int minIndex ;
        for(int i = 0 ; i < butterToPlates.size() ; i++){
            min = row * col + 1;
            minIndex = -1;
            for(int j = 0 ; j < butterToPlates.size(); j++)
                if(calculateManhatan(Robot,butterToPlates.get(j).get(0)) < min && !butterIndexes[j] && BIBFS(Robot,butterToPlates.get(j).get(0),matrix,row,col,false) != null){
                    min = calculateManhatan(Robot,butterToPlates.get(j).get(0));
                    minIndex = j;
                }
            if(minIndex == -1)
                break;
            butterIndexes[minIndex] = true;
            String butter = butterToPlates.get(minIndex).get(1);
            addButter(butter,matrix);
            ArrayList<String> temp = BIBFS(Robot,butterToPlates.get(minIndex).get(0),matrix,row,col,false);

            for(int j = 0 ; j < temp.size() ; j++)
                path.get(i).add(temp.get(j));

            for(int j = 1 ; j < butterToPlates.get(minIndex).size() ; j++) {
                path.get(i).add(butterToPlates.get(minIndex).get(j));
            }
            Robot = updateLocation(butterToPlates.get(minIndex).get(butterToPlates.get(minIndex).size() - 1));
        }
        return path;
    }
    public static String updateLocation(String newLocation){
        int goalRow = Integer.parseInt(newLocation.split(",")[0]);
        int goalCol = Integer.parseInt(newLocation.split(",")[1]);
        return goalRow + "," + goalCol;
    }
    public static ArrayList<String> makeWay(ArrayList<String> path,String[][] matrix,int row,int col){
        ArrayList<String> temp = new ArrayList<>();
        String butter = path.get(0);

        for(int i = 0; i < path.size() - 1 ; i++){
            if(!path.contains(FindThirdLocation(path.get(i),path.get(i + 1))))
                temp.add(FindThirdLocation(path.get(i),path.get(i + 1)));
            temp.add(path.get(i));
        }

        ArrayList<String> finalPath = new ArrayList<>();
        replaceButter(butter,matrix);
        finalPath.add(temp.get(0));
        for(int i = 0; i < temp.size() - 1 ; i ++){
            if(sameRow(temp.get(i),temp.get(i + 1)))
                placeButter(temp.get(i + 2),matrix);
            ArrayList<String> bbfs = BIBFS(temp.get(i),temp.get(i + 1),matrix,row,col,false);
            if(sameRow(temp.get(i),temp.get(i + 1)))
                replaceButter(temp.get(i + 2),matrix);
            for(int j = 1; j < bbfs.size() ; j++)
                finalPath.add(bbfs.get(j));
        }
        placeButter(butter,matrix);
        return finalPath;
    }
    public static boolean sameRow(String first,String second){
        int goalRow = Integer.parseInt(first.split(",")[0]);
        int goalCol = Integer.parseInt(first.split(",")[1]);
        int srcRow = Integer.parseInt(second.split(",")[0]);
        int srcCol = Integer.parseInt(second.split(",")[1]);
        if(goalRow == srcRow || goalCol == srcCol)
            return false;
        return true;
    }
    public static void placeButter(String location,String[][] matrix){
        int goalRow = Integer.parseInt(location.split(",")[0]);
        int goalCol = Integer.parseInt(location.split(",")[1]);
        matrix[goalRow][goalCol] = matrix[goalRow][goalCol] + "b";
    }
    public static void replaceButter(String location,String[][] matrix){
        int goalRow = Integer.parseInt(location.split(",")[0]);
        int goalCol = Integer.parseInt(location.split(",")[1]);
        matrix[goalRow][goalCol] = matrix[goalRow][goalCol].replace("b","");
    }
    public static void printDirection(ArrayList<String> path){
        for(int i = 0 ; i < path.size() - 1 ; i++)
            System.out.print("\u001B[32m" + FindDirection(path.get(i),path.get(i + 1)) + "\u001B[0m" + " ");
        System.out.println();
    }
}
