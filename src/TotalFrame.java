import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * ChatFrame 聊天界面+好友界面类
 * <p>用户进行聊天操作的窗口。
 * @author 鲫鱼
 * @see JFrame,ChatPenal
 */
public class TotalFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private int            page = 0;
    private int            friendNum = 0;
    private String         myId = null;
    private JLabel         label = null;
    private JLabel         idLabel = null;
    private JLabel         userLabel = null;
    private JLabel         notice = null;
    private JPanel         leftP = null;
    private JPanel         userP = null;
    private JPanel         friendP = null;
    private JSplitPane     sp = null;
    private JTabbedPane    chatP = null;
    private JTextField     addFriend = null;
    private JButton[]      friend = null;
    private String[]       friendMessage = null;
    private int[][]        friendUsed = null;
    private SqlConnection  conn = null;
    private JPopupMenu     friendPopMenu;
    private JMenuItem[]    operFriend = new JMenuItem[3];
    private int            temp;
   
    
    /**
     * 构造函数
     * @param myId 传入的用户Id
     */
    public TotalFrame(String myId) {
        
        //初始化用户Id
        this.myId = myId;
        
        //数据库连接
        conn = new SqlConnection(); 
        
        //Let’s Chat提示标签
        label = new JLabel("Let's Chat!");
        label.setFont(new Font("", Font.BOLD,24));  
        
        //用户名提示标签
        userLabel = new JLabel("用户名：" + conn.getUser(myId));        
        userLabel.setFont(new Font("", Font.PLAIN,13));
        
        //Id提示标签
        idLabel = new JLabel("ID："+ myId,JLabel.LEFT);      
        idLabel.setFont(new Font("", Font.PLAIN,13));
        
        //用户信息面板
        userP = new JPanel();
        userP.setLayout(new FlowLayout(FlowLayout.LEFT));
        userP.setPreferredSize(new Dimension(170, 90));
        userP.setBorder(new TitledBorder(new EtchedBorder(), "用户"));
        userP.add(userLabel);
        userP.add(idLabel);

        //添加好友提示和输入框
        notice = new JLabel("请输入用户Id添加好友");
        addFriend = new JTextField();
        addFriend.setPreferredSize(new Dimension(150, 30));
        addFriend.addActionListener(new AddFriendActionListener());
        
        //好友列表面板
        friendP = new JPanel();
        friendP.setPreferredSize(new Dimension(170, 500));
        friendP.setBorder(new TitledBorder(new EtchedBorder(), "好友列表"));
        friendP.add(notice);
        friendP.add(addFriend);
        
        //更新好友列表
        updateFriendList();
       
        //初始化好友使用情况
        friendUsed = new int[friendNum][2];
        for(int i = 0; i<friendNum; ++i) {
            friendUsed[i][0] = 0;
            friendUsed[i][1] = -1;
        }
        
        //好友操作项目
        operFriend[0] = new JMenuItem("修改备注名");
        operFriend[0].addActionListener(new UpdateFriendActionListener());
        operFriend[1] = new JMenuItem("删除");
        operFriend[1].addActionListener(new DeleteFriendActionListener());
        operFriend[2] = new JMenuItem("删除所有聊天记录");
        operFriend[2].addActionListener(new DeleteAllRecord());
       
        //弹出菜单
        friendPopMenu = new JPopupMenu();
        friendPopMenu.add(operFriend[0]);
        friendPopMenu.add(operFriend[1]);
        friendPopMenu.add(operFriend[2]);
         
        //将Let's chat标签、用户消息、好友列表添加到左面板中
        leftP = new JPanel();
        leftP.setLayout(new FlowLayout());
        leftP.add(label);
        leftP.add(userP);
        leftP.add(friendP);
        
        //聊天标签页
        chatP = new JTabbedPane();
        
        //左右分割符
        sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftP, chatP);
        sp.setDividerLocation(180);
        sp.setEnabled(false);
  
        //设置窗口
        setContentPane(sp);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(130,80,1100,700);                 
        setVisible(true);               
        validate();                             
    }
    
    
    /**
     * 好友操作类FriendOperationListener
     * @author 鲫鱼
     */
    class FriendOperationListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            
            //存储进行操作的按钮
            temp = getFriIndex((JButton)e.getSource());
            
            //如果是鼠标右键，则弹出菜单
            if(e.getButton() == MouseEvent.BUTTON3) {
                friendPopMenu.show(e.getComponent(),e.getX(), e.getY());
            }
            
           
        }
    }
    
    
    /**
     * 删除所有聊天记录类DeleteAllRecord
     * @author 鲫鱼
     */
    class DeleteAllRecord implements ActionListener{
        public void actionPerformed(ActionEvent e) {

            //出现通知对话框
            int n = JOptionPane.showConfirmDialog(null, "确认删除所有聊天记录?", "确认对话框", JOptionPane.YES_NO_OPTION); 
            
            //如果单击"是"
            if (n == JOptionPane.YES_OPTION) { 
                
                //显示已经删除的对话框
                JOptionPane.showMessageDialog(new JFrame(),"已删除");
                
                //在数据库中删除聊天记录
                conn.deleteRecord(myId,friendMessage[temp]);
                
            //如果单击"取消"
            } else if (n == JOptionPane.NO_OPTION) { 
                //显示已经取消的对话框
                JOptionPane.showMessageDialog(new JFrame(),"已取消");
            }
        }
    }
    
    
    /**
     * 添加好友类AddFriendActionListener
     * @author 鲫鱼
     */
    class AddFriendActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            
            //出现通知对话框
            int n = JOptionPane.showConfirmDialog(null, "确认添加用户"+ addFriend.getText() +"?", "确认对话框", JOptionPane.YES_NO_OPTION); 
            
            //如果单击"是"
            if (n == JOptionPane.YES_OPTION) { 
                
                //显示已经添加的对话框
                JOptionPane.showMessageDialog(new JFrame(),"已添加");
                
                //在数据库中添加好友，更新界面上的好友按钮
                conn.addFriend(myId,addFriend.getText());
                updateFriendList();
                
            //如果单击"取消"
            } else if (n == JOptionPane.NO_OPTION) { 
                //显示已经取消的对话框
                JOptionPane.showMessageDialog(new JFrame(),"已取消");
            }
        }
    }
    
    
    /**
     * 删除好友类DeleteFriendActionListener
     * @author 鲫鱼
     */
    class DeleteFriendActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            
            //出现通知对话框
            int n = JOptionPane.showConfirmDialog(null, "确认删除好友?你们的聊天消息无法恢复。", "确认对话框", JOptionPane.YES_NO_OPTION); 
            
            //如果单击"是"
            if (n == JOptionPane.YES_OPTION) { 
                
                //显示已经删除的对话框
                JOptionPane.showMessageDialog(new JFrame(),"已删除");
                
                //在数据库中添加好友，更新界面上的好友按钮
                conn.deleteFriend(myId,friendMessage[temp]);
                updateFriendList();
            
                //如果单击"取消"
            } else if (n == JOptionPane.NO_OPTION) { 
                //显示已经取消的对话框
                JOptionPane.showMessageDialog(new JFrame(),"已取消");
            }
        }
    }
    
    
    /**
     * 修改好友备注名类UpdateFriendActionListener
     * @author 鲫鱼
     */
    class UpdateFriendActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            
            //出现输入对话框
            String remark = JOptionPane.showInputDialog("请输入"+conn.getRemarkName(myId, friendMessage[temp])+"修改后的备注名");
            
            //修改数据库中的备注名
            conn.updateRemark(myId,friendMessage[temp], remark);
            
            //改变面板中的备注名
            friend[temp].setText(conn.getRemarkName(myId, friendMessage[temp])+"(" + conn.getUser(friendMessage[temp]) + ")");
        }
        
    }
    
    /**
     * 更新好友列表方法
     */
    void updateFriendList() {
        
        //如果已经有好友按钮
        if(friend != null) {
            for(int i =0; i < friendNum; ++i) {
                friendP.remove(friend[i]);
            }
        }
        
        //获取好友数量
        friendNum = conn.getFriendNum(myId);
        //System.out.println(friendNum);
        
        //获取好友列表
        friendMessage = conn.listFriend(myId);
        
        //创建好友按钮
        friend = new JButton[friendNum];

        //设置每一个好友按钮，并添加至面板中
        for(int i = 0; i <friend.length; ++i) {
            friend[i] = new JButton(conn.getRemarkName(myId, friendMessage[i])+"(" + conn.getUser(friendMessage[i]) + ")");
            friend[i].setPreferredSize(new Dimension(150, 30));
            friend[i].setContentAreaFilled(false);
            friend[i].addActionListener(new AddChatActionListener());
            friend[i].addMouseListener(new FriendOperationListener());
            friendP.add(friend[i]);
        }
        //System.out.println("成功！");
    }
    
    
    /**
     * 获取好友index方法
     * <p>通过获取右键的按钮，得到对应的下标
     * @param temp
     * @return i 下标，-1 没有找到下标
     */
    private int getFriIndex(JButton temp) {
        
        //在friend里面进行搜索
        for(int i=0; i<friend.length; ++i) {
            
            //如果相同，则返回下标
            if(temp == friend[i]) {
                return i;
            }
        }
        
        //没有搜索到，返回-1
        return -1; 
    }
    
    /**
     * 添加聊天类AddChatActionListener
     * @author 鲫鱼
     */
    class AddChatActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
                
            //获取按钮所在下标
            int index = getFriIndex((JButton)e.getSource());
            
            //如果与该好友的聊天界面还没有被打开
            if(friendUsed[index][0] == 0) {
                
                //新建标签页
                ChatPanel chat = new ChatPanel(myId,friendMessage[index]);
                
                //添加关闭标签页的按钮
                chat.close.addActionListener(new CloseActionHandler());
                
                //在page的地方添加标签页
                chatP.add(chat.totalP);
                chatP.setTitleAt(page,((JButton)(e.getSource())).getText());
                
                //设置好友使用情况
                friendUsed[index][1] = page;
                chatP.setSelectedIndex(page);
                friendUsed[index][0] = 1;
                
                //page数量+1
                page++;
            }
            
            //如果标签页已经存在
            else {
                
                //选中对应的标签页
                chatP.setSelectedIndex(chatP.indexOfTab(((JButton)(e.getSource())).getText()));
            }
        }
    }
    
    /**关闭聊天类CloseActionHandler
     * @author 鲫鱼
     *
     */
    public class CloseActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
            //获取被选中的标签页
            Component selected = chatP.getSelectedComponent();
            
            //移除标签页
            if (selected != null) {
                chatP.remove(selected);
                
                //标签页数量-1
                page--;
                
                //设置好友使用情况
                for(int i=0; i<friendNum; ++i) {
                    if(friendUsed[i][1] == chatP.getSelectedIndex()) {
                        friendUsed[i][1] = -1;
                        friendUsed[i][0] = 0;
                    }
                }
            }
        }
    }
    
    
}