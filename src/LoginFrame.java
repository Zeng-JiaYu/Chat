import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


/**
 * 登录窗口类LoginFrame
 * <p>用户输入账户密码进行登录操作的窗口
 * @author 鲫鱼
 * @see JFrame,FriFrame
 */
public class LoginFrame extends JFrame{

    private static final long serialVersionUID = 1L;
    private JPanel          loginP;
    private JLabel          label;
    private JLabel          userLabel;
    private JLabel          passLabel;
    private JLabel          noticeLabel;
    private JButton         login;
    private JTextField      userText;
    private JPasswordField  passText;
    private SqlConnection   conn;
    
    /**
     * 构造函数
     */
    public LoginFrame() {
        
        //数据库连接
        conn = new SqlConnection(); 
        
        //Let's Chat 文本标签
        label = new JLabel("Let's Chat",JLabel.CENTER);     
        label.setFont(new Font("", Font.BOLD,24));  
        
        //提示输入账户标签
        userLabel = new JLabel("账户：",JLabel.RIGHT);
        userLabel.setFont(new Font("", Font.BOLD,13));
               
        //提示输入密码标签
        passLabel = new JLabel(" 密码：",JLabel.RIGHT);
        passLabel.setFont(new Font("", Font.BOLD,13));
            
        //账户输入文本框
        userText = new JTextField(15);  
        
        //密码输入的密码文本框添加动作监听，回车触发
        passText = new JPasswordField(15);
        passText.addActionListener(new RegisterActionListener());
        
        //登录是否成功提示
        noticeLabel = new JLabel("",JLabel.CENTER);
        
        //将组件添加到loginP
        loginP = new JPanel();
        loginP.add(userLabel);
        loginP.add(userText);
        loginP.add(passLabel);
        loginP.add(passText);
        loginP.add(noticeLabel);
        
        //添加登录按钮
        login = new JButton("登录");
        login.setBounds(20, 20, 30, 30);    
        login.addActionListener(new RegisterActionListener());
        
        //将组件添加到窗口中
        add(label,BorderLayout.NORTH);
        add(loginP,BorderLayout.CENTER);
        add(login,BorderLayout.SOUTH);
        
        //设置聊天窗口
        setResizable(false);
        setBounds(550,350,250,180);                 
        setVisible(true);               
        validate();                             
        
        //关闭动作监听
        addWindowListener(
                new WindowAdapter() { 
                    public void windowClosing(WindowEvent e) {
                        setVisible(false);
                        conn.close();
                        System.exit(0);
                    }
                }
        );
    
    }
    
    /**
     * 登录动作监听
     * @author 鲫鱼
     */
    class RegisterActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //获取文本框中的账号密码，进行比对
            if(conn.vertifyLogin(userText.getText(), String.valueOf(passText.getPassword()))) {
                noticeLabel.setText("登录成功,加载中");
                
                //线程开始，等待100ms
                new WaitThread().start();
                } else {
                    
                    //输入错误
                    noticeLabel.setText("登录失败，请重新检查账号和密码");
                }
        }
    }
    
    /**
     * 线程等待类
     * @author 鲫鱼
     *
     */
    private class WaitThread extends Thread {
        
        //线程运行函数
        public void run() {
            try {
                
                //等待1000ms
                sleep(1000);
                
                //关闭当前窗口
                dispose();
                
                //新建聊天界面窗口，传递用户Id
                new TotalFrame(userText.getText()).setTitle("Let's Chat!");
            } catch (InterruptedException e) {
                
                //线程等待错误报错
                JOptionPane.showMessageDialog(null, "加载出错", null, JOptionPane.ERROR_MESSAGE); 
                //e.printStackTrace();
            }
        }
    }
}

