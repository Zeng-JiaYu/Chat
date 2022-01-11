import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * 数据库连接类SqlConnection
 * <p>包括数据库连接以及基于数据库操作的方法。
 * @author  鲫鱼
 * 
 */
public class SqlConnection {

    /** URL 获取协议、IP和端口等信息 */
    private static final String URL = "jdbc:mysql://localhost:3308/chat";
    
    /** Name 获取数据库用户名 */
    private static final String NAME = "root";  
    
    /** Name 获取数据库密码 */
    private static final String PASSWORD = "";  
    
    //定义Connection、Statement、ResultSet
    Connection connection = null;   
    Statement myStatement = null;
    ResultSet resultSet = null;
    
    
    public SqlConnection() {
        
        //指定MySQL驱动程序
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL,NAME,PASSWORD);
        } catch(ClassNotFoundException e) {
            
            //加载驱动程序错误，提示报错
            JOptionPane.showMessageDialog(null, "未能成功加载驱动程序，请检查是否导入驱动程序！", null, JOptionPane.ERROR_MESSAGE); 
            close();
            System.exit(0);
            //e.printStackTrace();
            
            
        } catch(SQLException e) {
            
            //获取数据库连接失败报错
            JOptionPane.showMessageDialog(null, "获取数据库连接失败！", null, JOptionPane.ERROR_MESSAGE); 
            close();
            System.exit(0);
            //e.printStackTrace();
        }
    }
    
    
    /**
     * 用户登录方法
     * <p>用户输入系统已有的账号和密码，基于数据库查询核对账号和密码。
     * @param id        用户ID
     * @param password  用户密码
     * @return          true表示存在用户且密码正确，false表示用户不存在或密码错误。
     */
    public boolean vertifyLogin(String id,String password){
        
        //正确的密码
        String corrPass = null;
        
        //获取正确的密码
        try {
            myStatement  = connection.createStatement();
            resultSet = myStatement.executeQuery("select password from user where id = '"+ id +"'");
            if(resultSet.next()) {
                corrPass = resultSet.getString("Password");
            }
        } catch (SQLException e) {
            
            //登录出错，退出程序
            JOptionPane.showMessageDialog(null, "登录出错", null, JOptionPane.ERROR_MESSAGE);
            close();
            System.exit(0);
            //e.printStackTrace();
        }
        
        //如果没有找到密码，即用户不存在，登录失败
        if(corrPass == null) {
            return false;
        }
        //如果密码正确，登录成功
        else if(corrPass.equals(password)) {
            return true;
        }
        
        //其他情况，登录失败
        return false;
    }
        
    
    /**
     * 获取用户名
     * <p>通过用户ID，基于数据库查询获得对应的用户名。
     * @param   id      用户ID
     * @return  name    用户名字
     */
    public String getUser(String id){
        
        //用户名
        String name = null;
        
        //获取用户名
        try {
            myStatement  = connection.createStatement();
            resultSet = myStatement.executeQuery("select id,name from user where id = '"+ id +"'");
            if(resultSet.next()) {
                name = resultSet.getString("name");
            }
        } catch (SQLException e) {
            
            //获取用户名出错，错误提示
            JOptionPane.showMessageDialog(null, "获取用户名出错！", null, JOptionPane.ERROR_MESSAGE);
            close();
            System.exit(0);
            //e.printStackTrace();
        }
        
        //返回用户名
        return name;
    }
    
    /**
     * 获取备注名
     * <p>通过用户ID、好友FriId，基于数据库查询获得对应的备注名。
     * @param   id      用户ID
     * @param   friId   好友ID
     * @return  remarkName    备注名
     */
    public String getRemarkName(String myId, String friId){
        
        //备注名
        String remarkName = null;
        
        //获取备注名
        try {
            myStatement  = connection.createStatement();
            resultSet = myStatement.executeQuery("select remark_name from friend where id = '" + myId + "' and friend = '" + friId + "' ");
            if(resultSet.next()) {
                remarkName = resultSet.getString("remark_name");
            }
        } catch (SQLException e) {
            
            //获取备注名出错，退出程序
            JOptionPane.showMessageDialog(null, "获取备注名出错", null, JOptionPane.ERROR_MESSAGE); 
            close();
            System.exit(0);
            //e.printStackTrace();
        }
        
        //返回备注名
        return remarkName;
    }
    
    
    /**
     * 获取用户的好友数
     * <p>通过用户的ID，基于数据库查询获得对应的好友数
     * @param   id      用户ID
     * @return  number  用户的好友数
     */
    public int getFriendNum(String id){
        
        //好友数
        int number = 0;
        
        //获取好友数
        try {
            myStatement  = connection.createStatement();
            resultSet = myStatement.executeQuery("SELECT COUNT(Friend) as num from (SELECT Friend from user NATURAL JOIN friend WHERE id = '" + id +"')as sec_table");
            if(resultSet.next()) {
                number = resultSet.getInt("num");
            }
        } catch (SQLException e) {
            
            //获取好友数
            JOptionPane.showMessageDialog(null, "获取好友数错误", null, JOptionPane.ERROR_MESSAGE);
            close();
            System.exit(0);
            //e.printStackTrace();
        }
        
        //返回好友数
        return number;
    }
    
    
    /**
     * 获取好友列表
     * <p>通过用户的ID，基于数据库查询获得对应的好友列表
     * @param   id      用户ID
     * @return  friList 好友列表
     */
    public String[] listFriend(String id){
        
        //获取好友数
        int num = getFriendNum(id);
        
        //循环下标i
        int i = 0;
        
        //好友列表
        String[] friList = new String[num];
        
        
        //获取好友列表
        try {
            myStatement  = connection.createStatement();
            resultSet = myStatement.executeQuery("select name,id from\r\n"
                    + " user NATURAL join(SELECT friend as id from user NATURAL JOIN friend WHERE id = '" + id +"')as sec_table");
            while(resultSet.next()){
                friList[i] = resultSet.getString("id");
                ++i;
            }
        } catch (SQLException e) {

            //获取好友列表出错
            JOptionPane.showMessageDialog(null, "获取好友列表", null, JOptionPane.ERROR_MESSAGE); 
            close();
            System.exit(0);
            //e.printStackTrace();
        }
        
        //返回好友列表
        return friList;
    }
    
    
    /**
     * 获得聊天记录表
     * <p>通过用户ID，好友ID，获取聊天记录表
     * @param myId      用户ID
     * @param friId     好友ID
     * @param table     聊天记录表
     */
    public String getTable(String myId,String friId) {
        
        //聊天记录表
        String table = null;
        
        try {
            //通过用户ID和好友ID获取聊天记录存储的表名
            myStatement  = connection.createStatement();
            resultSet = myStatement.executeQuery("select record from friend where id = '" + myId +"' and friend = '"+ friId +"'");
            if(resultSet.next()) {
                table = resultSet.getString("record");
            }

        } catch (SQLException e) {
            
            //获取聊天记录表出错
            JOptionPane.showMessageDialog(null, "获取聊天记录表出错", null, JOptionPane.ERROR_MESSAGE); 
            close();
            System.exit(0);
            //e.printStackTrace();
        }
        
        //返回聊天记录表
        return table;
    }
    
    /**
     * 获得聊天记录
     * <p>通过用户ID，好友ID，在引用的文本块中输出聊天消息
     * @param myId      用户ID
     * @param friId     好友ID
     * @param output    输出消息框
     */
    public void getRecord(String myId,String friId,JTextArea output) {
        try {
            
            //定义临时Statement、ResultSet
            Statement myStatement = null;
            ResultSet resultSet = null;
            
            //查询聊天记录表内的记录
            String id,name,time,message;
            myStatement  = connection.createStatement();
            resultSet = myStatement.executeQuery("select * from " + getTable(myId, friId)  + " order by time asc");
            while(resultSet.next()){
                id = resultSet.getString("id");
                name = getUser(id);
                time = resultSet.getString("time");
                message = resultSet.getString("message");
                
                //将消息添加至文本域最后
                output.append(name + "(" + time + ")\n" + message + "\n");
                output.append("————————————————————————————————————————————————————\n");
            }
        
        } catch (SQLException e) {
            
            //获取聊天消息出错
            JOptionPane.showMessageDialog(null, "获取聊天记录出错", null, JOptionPane.ERROR_MESSAGE); 
            close();
            System.exit(0);
            //e.printStackTrace();
        }
    }
    
    
    /**
     * 删除聊天记录
     * <p>通过用户ID，好友ID，删除之前所有的聊天记录
     * @param myId      用户ID
     * @param friId     好友ID
     * @return count 聊天记录数
     */
    public void deleteRecord(String myId,String friId) {
        try {
            
            //查询聊天记录表内的记录数
            myStatement  = connection.createStatement();
            myStatement.execute("delete from " + getTable(myId, friId));
        } catch (SQLException e) {
            
            //删除聊天记录出错
            JOptionPane.showMessageDialog(null, "删除聊天记录出错", null, JOptionPane.ERROR_MESSAGE);
            close();
            System.exit(0);
            //e.printStackTrace();
        }
    }
    
    /**
     * 获得聊天记录数
     * <p>通过用户ID，好友ID，在引用的文本块中输出聊天记录数
     * @param myId      用户ID
     * @param friId     好友ID
     * @return count 聊天记录数
     */
    public int getRecordNum(String myId,String friId) {
        
        //聊天记录数
        int count = 0;
        
        //查询聊天记录表内的记录数
        try {
            myStatement  = connection.createStatement();
            resultSet = myStatement.executeQuery("select count(time) from " + getTable(myId, friId));
            while(resultSet.next()){
                count = resultSet.getInt("count(time)");
            }
        } catch (SQLException e) {
            
            //获取聊天记录出错
            JOptionPane.showMessageDialog(null, "获取聊天记录数出错", null, JOptionPane.ERROR_MESSAGE);
            close();
            System.exit(0);
            //e.printStackTrace();
        }
        
        //返回聊天记录数
        return count;
    }

    
    /**
     * 发送消息给好友
     * <p>将文本输入的内容插入至数据库表中
     * @param myId      发送端ID
     * @param friId     接受端ID
     * @param message   信息内容
     */
    public void submitMessage(String myId,String friId,String message) {
        
        //发送消息
        try {
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String sql = "Insert into " + getTable(myId,friId) +" Values ('"+ formatter.format(date)  +"','" + myId +"','"+ message +"')";
            myStatement.executeUpdate(sql);
        } catch (SQLException e) {
            
            //发送消息出错
            JOptionPane.showMessageDialog(null, "发送消息出错", null, JOptionPane.ERROR_MESSAGE); 
            close();
            System.exit(0);
            //e.printStackTrace();
        }
    }
    
    /**
     * 修改好友备注名
     * <p>将文本输入的内容插入至数据库表中
     * @param myId      发送端ID
     * @param friId     接受端ID
     * @param remark    备注名
     */
    public void updateRemark(String myId,String friId,String remark) {
        
        //修改备注名
        try {
            myStatement  = connection.createStatement();
            String sql = "UPDATE friend SET Remark_Name='"+ remark +"' WHERE id = '" + myId +"' and friend = '"+ friId +"'" ;
            myStatement.executeUpdate(sql);
        } catch (SQLException e) {
           
            //修改备注名出错
            JOptionPane.showMessageDialog(null, "修改备注名错误", null, JOptionPane.ERROR_MESSAGE);
            close();
            System.exit(0);
            //e.printStackTrace();
        }
    }
    
    
    /**
     * 删除好友
     * <p>删除与好友的聊天记录表以及好友关系
     * @param myId      发送端ID
     * @param friId     接受端ID
     */
    public void deleteFriend(String myId,String friId) {
        
        //删除好友
        try {
            
            //删除存储聊天记录的表
            String sql = "DROP table " + getTable(myId, friId);
            myStatement.executeUpdate(sql);
            
            //删除好友关系
            sql = "delete from friend where id = '" + myId + "' and friend = '"+ friId +"'";
            myStatement.executeUpdate(sql);
            sql = "delete from friend where id = '" + friId + "' and friend = '"+ myId +"'";
            myStatement.executeUpdate(sql);
            
        } catch (SQLException e) {
            
            //删除好友出错提醒
            JOptionPane.showMessageDialog(null, "删除好友出错", null, JOptionPane.ERROR_MESSAGE); 
            close();
            System.exit(0);
            //e.printStackTrace();
        }
    }
    
    /**
     * 添加好友
     * <p>添加与好友的聊天记录表以及好友关系
     * @param myId      发送端ID
     * @param friId     接受端ID
     */
    public boolean addFriend(String myId,String friId) {
        
        //添加好友
        try {
            
            //添加好友的id为自己的id，添加错误
            if(myId == friId) {
                JOptionPane.showMessageDialog(null, "无法添加自己为好友", null, JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            //添加好友的id不存在，添加错误
            myStatement  = connection.createStatement();
            resultSet = myStatement.executeQuery("select id from user where id = '" + friId +"'");
            if(!resultSet.next()) {
                JOptionPane.showMessageDialog(null, "没有找到该好友", null, JOptionPane.WARNING_MESSAGE);
               return false;
            }
            
            //添加好友的id为已经是好友的用户id，添加错误
            myStatement  = connection.createStatement();
            resultSet = myStatement.executeQuery("select friend from friend where id = '" + friId +"' and friend = " + friId);
            if(resultSet.next()) {
                JOptionPane.showMessageDialog(null, "已经添加该好友", null, JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            //添加好友关系
            String sql = "Insert into friend Values ('"+ myId +"','" + friId + "','"+ this.getUser(friId) + "','"+ this.getUser(myId) +"_" +  this.getUser(friId) +"');";
            myStatement.executeUpdate(sql);
            sql = "Insert into friend Values ('"+ friId +"','" + myId + "','"+ this.getUser(myId) + "','"+ this.getUser(myId) +"_" +  this.getUser(friId) +"');";
            myStatement.executeUpdate(sql);
            
            //添加好友聊天记录表
            sql = "create table "+ this.getUser(myId)+"_"+this.getUser(friId)+"( time varchar(20), id varchar(8), message varchar(100), primary key (time), foreign key (id) references user(id) on delete set null ) ";
            myStatement.executeUpdate(sql);
            
            //System.out.println("添加成功");

        } catch (SQLException e) {
            
            //添加好友错误提醒
            JOptionPane.showMessageDialog(null, "添加好友出错", null, JOptionPane.WARNING_MESSAGE); 
            //e.printStackTrace();
        }
        
        //添加成功返回
        return true;
    }
    
    /**
     * 数据库使用完毕，关闭数据库连接
     * <p>分别关闭ResultSet对象、Statement对象以及Connection对象
     */
    public void close() {
        try {
            
            //关闭ResultSet对象
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            
            //关闭Statement对象
            if (myStatement != null) {
                myStatement.close();
                myStatement = null;
            }
            
            //关闭Connection对象
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            
            //关闭数据库出错
            JOptionPane.showMessageDialog(null, "关闭数据库出错", null, JOptionPane.ERROR_MESSAGE); 
            //e.printStackTrace();
        }
    }
}
