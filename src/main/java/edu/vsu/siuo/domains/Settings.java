package edu.vsu.siuo.domains;

import lombok.Data;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@Data
public class Settings implements Serializable {
    int maxCountOfTasks = 100;
    String defaultPath = System.getProperty("user.home");
    Boolean openFile = false;

    public void save(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("settings.dat")))
        {
            oos.writeObject(this);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
