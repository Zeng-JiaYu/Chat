import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

/**
 * ChatPenal 聊天面板
 * <p>用户查看消息，发送消息的窗口。
 * @author 鲫鱼
 * @see Thread
*/
public class ChatPanel extends Thread {

    private JLabel          friend;
    private JPanel          buttonP;
    private JTextArea       record,message;
    private JButton         submit;
    public  JButton         close;
    private JSplitPane      sp;
    private ScrollPane      scrollP1,scrollP2;
    private SqlConnection   conn;   
    private String          myId,friId;
    public  JPanel          totalP;
    
    /**
     * 聊天界面初始化
     * @param myId  用户ID
     * @param friId 好友ID
     */
    public ChatPanel(String myId,String friId) {
        
        //存储用户ID和聊天对象的ID
        this.myId = myId;
        this.friId = friId;
        
        //数据库连接
        conn = new SqlConnection();
        
        //提示与谁聊天的信息
        friend = new JLabel("Chatting with "+ conn.getRemarkName(myId, friId),JLabel.LEFT);
        friend.setFont(new Font("", Font.BOLD,18));
        
        //显示聊天记录
        record = new JTextArea(""); 
        record.setColumns(40);
        record.setLineWrap(true);                    
        record.setWrapStyleWord(true);              
        record.setEditable(false);
        record.setFont(new Font("", Font.BOLD,15));
        
        //将聊天记录文本框添加到ScrollPane中
        scrollP1 = new ScrollPane();
        scrollP1.add(record);
        scrollP1.setBounds(new Rectangle(850,420));
    
        //发送消息文本域
        message = new JTextArea("");
        message.setLineWrap(true);                  
        message.setWrapStyleWord(true);             
        message.setFont(new Font("",Font.BOLD,15));
        
        //将发送消息文本域添加到ScrollPane中
        scrollP2 = new ScrollPane();
        scrollP2.add(message);
        scrollP2.setBounds(new Rectangle(850,120));
        
        //添加横向分割符，分开发送消息文本域和发送消息文本域
        sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollP1, scrollP2);
        sp.setDividerLocation(420);
        
        //添加发送消息按钮
        submit = new JButton("发送");
        submit.addActionListener(new AddSubmitActionListener());
        
        //添加关闭聊天界面按钮
        close = new JButton("关闭");
         
        //按钮面板
        buttonP = new JPanel();
        buttonP.add(submit);
        buttonP.add(close);
  
        //将左右组件添加到面板中
        totalP = new JPanel();
        totalP.add(friend,BorderLayout.NORTH);
        totalP.add(sp,BorderLayout.CENTER);
        totalP.add(buttonP,BorderLayout.SOUTH);     
        totalP.setVisible(true);                
        totalP.validate();  
        
        //开始线程：每500ms刷新一次聊天记录
        start();
    }
    
    /**
     * 线程运行函数
     */
    public void run() {
        
        //聊天消息记录数
        int num = 0;
        
        //当数据库连接的时候且线程并没有被暂停的时候
        while(conn != null && !Thread.currentThread().isInterrupted()) {
            try {
                
                //如果聊天记录数有变化，则刷新聊天记录和聊天记录数
                if(num != conn.getRecordNum(myId, friId)) {  
                    record.setText("");
                    conn.getRecord(myId, friId,record);
                    num = conn.getRecordNum(myId, friId);
                }
                
                //间隔500ms刷新
                Thread.sleep(500);  
            } catch (InterruptedException e) {
               
                //聊天记录出错提醒
                JOptionPane.showMessageDialog(null, "刷新聊天记录出错", null, JOptionPane.ERROR_MESSAGE); 
                conn.close();
                System.exit(0);
                //e.printStackTrace();
            }
        }
    }
    
    
    /**
     * 发送消息动作监听
     * @author 鲫鱼
     */
    class AddSubmitActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            conn.submitMessage(myId, friId, message.getText());
            message.setText("");
        }
    }
} 
