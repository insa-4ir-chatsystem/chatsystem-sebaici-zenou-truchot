package objects;

import java.net.InetAddress;

public class User{
    
    private String nickname;
    private int port;
    private InetAddress ip;

    // 0 = offline, 1 = online, 2 = busy, 3 = away
    private int status = 1;

    public User(String nickname, InetAddress ip){
        this.nickname = nickname;
        this.ip = ip;

    }
    public User(InetAddress ip){ // used to give a first default name which will be the ip adress
        this.nickname = ip.toString();
        this.ip = ip;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    
    public void setPort(int port){
        this.port = port;
    }
    public void setStatus(int status){
        if (status < 0 || status > 3){
            throw new IllegalArgumentException("Status must be between 0 and 3");
        }
        this.status = status;
    }
    
    public int getPort() {
        return this.port;
    }
    public int getStatus(){
        return this.status;
    }
    public String getNickname(){
        return this.nickname;
    }
    public InetAddress getIp(){
        return this.ip;
    }
    
}