package server;


import java.util.ArrayList;

public class Game {

    private int randomNumber;
    private ArrayList<GameData> data;

    public void AddData(String name, int number)
    {
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).Name.equals(name)){
                data.get(i).Number = number;
                return;
            }
        }
        data.add(new GameData(name,number));
    }

    public int GetRandom(){
       return randomNumber;
    }

    public String GetResult(){
        GameData min = data.get(0);
        for (int i = 1; i < data.size(); i++ ){
            if(Math.abs(randomNumber - data.get(i).Number) < Math.abs(randomNumber - min.Number))
                min = data.get(i);
        }
        data.clear();
        randomNumber = (int)(Math.random() * 1000);
        return "Матрос под номером - " + min.Name + " отправляется за палубу. Его число - " + min.Number + ". Поздравляем!!!";
    }

    public Game() {

        randomNumber = (int)(Math.random() * 1000);
        data = new ArrayList<GameData>();
    }

}
