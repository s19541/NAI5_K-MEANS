import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class KMeans {
    static void groupFromFile(String filePath,int k){
        List<Iris>data=loadSetFromFile(filePath);
        List<List<Iris>>groupes=createStartingGroups(data,k);
        List<List<Iris>>modifiedGroupes;
        while(true) {
            modifiedGroupes = group(groupes, data);
            if(compareGroups(groupes,modifiedGroupes))
                break;
            groupes=modifiedGroupes;
        }
        groupes=modifiedGroupes;
        showGroupes(groupes);

    }
    private static List<Iris> loadSetFromFile(String filePath){
        List<Iris> set=new ArrayList<>();
        try{
            BufferedReader bf=new BufferedReader(new FileReader(filePath));
            String line;
            while((line=bf.readLine())!=null){
                StringTokenizer tokenizer=new StringTokenizer(line,",");
                String token;
                List<Double>attr=new ArrayList<>();
                while(tokenizer.hasMoreTokens()){
                    token=tokenizer.nextToken();
                    if(!token.startsWith("Iris")) {
                        attr.add(Double.parseDouble(token));
                    }
                    else
                        set.add(new Iris(attr,token));
                }
            }
            bf.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return set;
    }
    private static List<List<Iris>>createStartingGroups(List<Iris>data,int k){
        Collections.shuffle(data) ;
        List<List<Iris>>groupes=new ArrayList<>();
        int groupSize=data.size()/k;
        int tmp=0;
        int tmp2=0;
        for(Iris iris:data){
            if(tmp<k){
                if(groupes.size()-1<tmp)
                    groupes.add(new ArrayList<>());
                if(groupes.get(tmp).size()!=groupSize)
                    groupes.get(tmp).add(iris);
                else
                    tmp++;
            }
            if(tmp==k){
                groupes.get(tmp2).add(iris);
                tmp2++;
            }
        }
        return groupes;
    }
    private static void showGroupes(List<List<Iris>> groupes){
        for (int i = 0; i < groupes.size(); i++) {
            System.out.println("Group:"+(i+1));
            for (int j = 0; j < groupes.get(i).size(); j++) {
                System.out.println((j+1)+"."+groupes.get(i).get(j));
            }
        }
    }
    private static List<List<Iris>> group(List<List<Iris>> groupes,List<Iris>data){
       List<List<Double>> middlePoints = new ArrayList<>();
       for(List<Iris>group:groupes){
           List<Double>middlePoint=new ArrayList<>();
           for (int i = 0; i <group.get(0).attributes.size() ; i++) {
               middlePoint.add(0.0);
           }
           for (int i = 0; i < group.size(); i++)
               for (int j = 0; j < group.get(i).attributes.size(); j++)
                   middlePoint.set(j,middlePoint.get(j)+group.get(i).attributes.get(j));
           for (int j = 0; j < group.get(0).attributes.size(); j++)
               middlePoint.set(j,middlePoint.get(j)/group.size());
               middlePoints.add(middlePoint);
       }
        List<List<Iris>> retGroups=new ArrayList<>();
        for (int i = 0; i < groupes.size(); i++) {
            retGroups.add(new ArrayList<>());
        }
        for (Iris iris:data) {
            int group=0;
            double minDistance=calcDistance(iris.attributes,middlePoints.get(0));
            for (int i = 1; i <middlePoints.size() ; i++) {
                if(calcDistance(iris.attributes,middlePoints.get(i))<minDistance){
                    group=i;
                    minDistance=calcDistance(iris.attributes,middlePoints.get(i));
                }
            }
                retGroups.get(group).add(iris);

        }
        return retGroups;
    }
    private static double calcDistance(List<Double>iris,List<Double>midPoint){
        double distance=0;
        for (int i = 0; i < midPoint.size(); i++) {
            distance+=Math.pow(iris.get(i)-midPoint.get(i),2);
        }
        return distance;
    }
    private static boolean compareGroups(List<List<Iris>> groupes,List<List<Iris>> groupes2){
        if(groupes.size()!=groupes2.size())
            return false;
        for (int i = 0; i < groupes.size(); i++) {
            for (int j = 0; j < groupes.get(i).size(); j++) {
                for (int k = 0; k < groupes.get(i).get(j).attributes.size(); k++) {
                    if(groupes.get(i).get(j).attributes.get(k)!=groupes2.get(i).get(j).attributes.get(k))
                        return false;
                }
            }
        }
        return true;
    }
}
