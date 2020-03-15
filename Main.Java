
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static final int TYPE_BIN=0;
    public static final int TYPE_STR=1;
    public static final int TYPE_HEX=2;

    public static String Str2Bin(String Str) {
        String ret="";
        char[] chars = Str.toCharArray();
        for(int i=0;i<chars.length;i++){
            String temp=Integer.toBinaryString(chars[i]);
            while(temp.length()!=8){
                temp='0'+temp;
            }
            ret+=temp;
        }
        return ret;
    }
    public static String Bin2Str(String Bin){
        String ret="";
        for(int i=0;i<Bin.length();i+=8){
            String temp=Bin.substring(i,i+8);//获取第一个字符的binary
            ret+=String.valueOf((char)Integer.parseInt(temp, 2));
        }
        return ret;
    }
    public static String Str2Hex(String Str) {
        String ret = "";
        for (int i = 0; i < Str.length(); i++) {
            int ch = Str.charAt(i);
            String temp = Integer.toHexString(ch);
            ret += temp;
        }
        return ret;
    }
    public static String Hex2Str(String Hex){
        String ret="";
        for(int i=0;i<Hex.length();i+=2){
            String temp=Hex.substring(i,i+2);
            ret+=String.valueOf((char)Integer.parseInt(temp, 16));
        }
        return ret;
    }
    public static String Hex2Bin(String Hex){
        String ret="";
        for(int i=0;i<Hex.length();i+=2){
            String temp=Hex.substring(i,i+2);
            temp=Integer.toBinaryString(Integer.parseInt(temp, 16));
            while(temp.length()!=8) temp='0'+temp;
            ret+=temp;
        }
        return ret;
    }
    public static String Bin2Hex(String Bin){
        String ret="";
        for(int i=0;i<Bin.length();i+=8){
            String temp=Bin.substring(i,i+8);
            temp=Integer.toHexString(Integer.parseInt(temp,2));
            while(temp.length()!=2) temp='0'+temp;
            ret+=temp;
        }
        return ret;
    }
    public static String Encrypt(String data){
        return Encrypt(data,TYPE_BIN);
    }
    public static String Encrypt(String data,int type){
        String ret="";
        int m_length,d_length;
        switch (type) {
            case TYPE_STR:
                data = Str2Bin(data);
                break;
            case TYPE_HEX:
                data = Hex2Bin(data);
                break;

            default:
                break;
        }
        d_length=data.length();
        //定义length
        m_length=Integer.toHexString(data.lastIndexOf('1')).length();
        List<String> plist =new ArrayList<>();
        for(int i=0;i<d_length;i++){
            if(data.substring(i,i+1).equals("1")){
                String temp=Integer.toHexString(i);
                while(temp.length()!=m_length) temp='0'+temp;
                plist.add(temp);
            }
        }
        Random rand=new Random();
        while(plist.size()!=0){
            int temp=rand.nextInt(plist.size());
            ret+=plist.get(temp);
            plist.remove(temp);
        }
        //格式化header(16-bit)
        String dlength,mlength;
        dlength=Integer.toHexString(d_length);
        while(dlength.length()<8) dlength='0'+dlength;
        mlength=Integer.toHexString(m_length);
        while(mlength.length()<8) mlength='0'+mlength;
        ret=dlength+mlength+ret;
        return ret;
    }
    public static String Decrypt(String data){
        String ret="";
        //获取Header
        String HeaderStr=data.substring(0,16);
        int d_length,m_length;
        d_length=Integer.parseInt(HeaderStr.substring(0,8),16);
        m_length=Integer.parseInt(HeaderStr.substring(9),16);
        data=data.substring(16);
        while(ret.length()!=d_length) ret+='0';
        for(int i=0;i<data.length();i+=2){
            String temp=data.substring(i,i+2);
            ret=UpdateStr(ret,"1",Integer.parseInt(temp,16));
        }
        return ret;
    }
    private static String UpdateStr(String data,String update){ return UpdateStr(data,update,0);}
    private static String UpdateStr(String data,String update,int position){
        String start,end;
        start=data.substring(0,position);
        if(position==0) start="";
        end=data.substring(position+update.length());
        return start+update+end;
    }

}
