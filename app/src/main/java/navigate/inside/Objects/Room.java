package navigate.inside.Objects;


public class Room {

    String roomNum;
    String roomName;


    public Room(String roomNum,String roomName){
        this.roomNum=roomNum;
        this.roomName=roomName;
    }

    public String GetRoomNum(){
        return roomNum;
    }
    public String GetRoomName(){
        return roomName;
    }



}
