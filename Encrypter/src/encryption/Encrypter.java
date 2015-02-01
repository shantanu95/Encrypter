package encryption;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Encrypter extends JFrame implements ActionListener{
	
	static Encrypter encr;
	JButton b1, b2;
	Text t;
	Files f;
	
	Encrypter(String s){
		super(s);
		Container c=this.getContentPane();
		Buttons b=new Buttons();
		t=new Text();
		f=new Files();
		b1.addActionListener(this);
		b2.addActionListener(this);
		this.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
		add(b);
		add(t);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		encr=new Encrypter("Encrypyter");
		encr.setSize(640, 480);
		encr.setVisible(true);
	}
	
	class Buttons extends JPanel{
		
		Buttons(){
			super();
			b1=new JButton("Work with String");
			b2=new JButton("Work with File");
			this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			add(Box.createHorizontalGlue());
			add(b1);
			add(Box.createRigidArea(new Dimension(10, 0)));
			add(b2);
			add(Box.createHorizontalGlue());
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if(ae.getSource()==b1){
			encr.remove(f);
			encr.add(t);
			b1.setEnabled(false);
			b2.setEnabled(true);
			encr.setSize(641, 481);
			encr.setSize(640, 480);
		}else{
			encr.remove(t);
			encr.add(f);
			b2.setEnabled(false);
			b1.setEnabled(true);
			encr.setSize(641, 481);
			encr.setSize(640, 480);
		}
	}

}
