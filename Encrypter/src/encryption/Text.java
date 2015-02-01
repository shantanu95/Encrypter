package encryption;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.util.*;

@SuppressWarnings("serial")
public class Text extends JPanel implements ActionListener {
	
	static Thread en, de;
	static Text encr;
	String s, f="";
	JTextArea ta1, ta2;
	JButton b1, b2;
	Vector<Integer> bl=new Vector<Integer>();
	Vector<Integer> wi=new Vector<Integer>();
	Vector<Integer> el=new Vector<Integer>();
	
	Text(){
		super();
		ta1=new JTextArea();
		ta2=new JTextArea();
		Font f=new Font(ta1.getFont().getName(), ta1.getFont().getStyle(), 24);
		ta1.setFont(f);
		ta2.setFont(f);
		JScrollPane s1=new JScrollPane(ta1);
		JScrollPane s2=new JScrollPane(ta2);
		s1.setPreferredSize(new Dimension(100000, Toolkit.getDefaultToolkit().getScreenSize().height/2));
		s2.setPreferredSize(new Dimension(100000, Toolkit.getDefaultToolkit().getScreenSize().height/2));
		Buttons but=new Buttons();
		b1.addActionListener(this);
		b2.addActionListener(this);
		b1.setAlignmentX(Container.RIGHT_ALIGNMENT);
		b2.setAlignmentX(Container.RIGHT_ALIGNMENT);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(s1);
		add(but);
		add(s2);
	}
	
	class Buttons extends JPanel{
		Buttons(){
			super();
			b1=new JButton("Encrypt");
			b2=new JButton("Decrypt");
			this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			add(Box.createHorizontalGlue());
			add(b1);
			add(b2);
			this.setBackground(Color.LIGHT_GRAY);
		}
	}
	
	int genAdd(int elno, int width, int block, int pChar, int nChar){
		long l=(long) ((long)(Math.pow(elno, 6)+Math.pow(width, 5)+Math.pow(block, 3))
		+Math.pow(pChar, 3)+Math.pow(nChar, 2)+elno*nChar*elno*width+width*block+
		nChar*block+block*elno+pChar*elno+127);
		int i=(int)(l%256);
		return i;
	}
	
	int genWidth(int b){
		String s="9864943745774539577776593776755384674878467669445735444568757745563"
			+"56485778577734575778964977363777875746844494755495343474654958537745479"
			+"5579689558953545479956";
		int k=b%s.length();
		Integer i=Integer.parseInt(""+s.charAt(k));
		return i.intValue();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		s=ta1.getText();
		if(e.getSource()==b1){
			Encrypt a=new Encrypt();
			en=new Thread(a);
			en.start();
		}else{
			Decrypt a=new Decrypt();
			de=new Thread(a);
			de.start();
		}
	}
	
	void Addition(){
		String f="";
		int b=0;
		int len=s.length();
		int k=0;
		for( ; k<s.length();){
			b++;
			int w=genWidth(b);
			if(w<=len){
				System.out.println("\nin if");
				for(int i=0; i<w; i++){
					int l;
					if(k==0){
						
						l=genAdd(i+1, w, b, 0, sumString(s.substring(k+1, s.length())));
						System.out.println(i+1+", "+w+", "+b+", "+sumString(f)+", "+sumString(s.substring(k+1, s.length())));
						int a=((int)s.charAt(k)+l)%256;
						f=f+(char)a;
						ta2.append(""+(char)a);
					}	
					else if(k==s.length()-1){
						
						l=genAdd(i+1, w, b, sumString(f), 0);
						System.out.println(i+1+", "+w+", "+b+", "+sumString(f)+", "+sumString(s.substring(k+1, s.length())));
						int a=((int)s.charAt(k)+l)%256;
						f=f+(char)a;
						ta2.append(""+(char)a);
					}else{
						
						l=genAdd(i+1, w, b, sumString(f), sumString(s.substring(k+1, s.length())));
						System.out.println(i+1+", "+w+", "+b+", "+sumString(f)+", "+sumString(s.substring(k+1, s.length())));
						int a=((int)s.charAt(k)+l)%256;
						f=f+(char)a;
						ta2.append(""+(char)a);
					}
					k++;
				}
				len=len-w;
			}else{
				System.out.println("\nin else");
				for(int i=0; i<len; i++){
					int l;
					if(k==0){
						
						l=genAdd(i+1, w, b, 0, sumString(s.substring(k+1, s.length())));
						System.out.println(i+1+", "+w+", "+b+", "+sumString(f)+", "+sumString(s.substring(k+1, s.length())));
						int a=((int)s.charAt(k)+l)%256;
						f=f+(char)a;
						ta2.append(""+(char)a);
					}	
					else if(k==s.length()-1){
						
						l=genAdd(i+1, w, b, sumString(f), 0);
						System.out.println(i+1+", "+w+", "+b+", "+sumString(f)+", "+sumString(s.substring(k+1, s.length())));
						int a=((int)s.charAt(k)+l)%256;
						f=f+(char)a;
						ta2.append(""+(char)a);
					}else{
						
						l=genAdd(i+1, w, b, sumString(f), sumString(s.substring(k+1, s.length())));
						System.out.println(i+1+", "+w+", "+b+", "+sumString(f)+", "+sumString(s.substring(k+1, s.length())));
						int a=((int)s.charAt(k)+l)%256;
						f=f+(char)a;
						ta2.append(""+(char)a);
					}
					k++;
				}
			}
		}
	}
	
	String invert(String s){
		String f="";
		for(int i=s.length()-1; i>=0; i--){
			f=f+s.charAt(i);
		}
		return f;
	}
	
	int sumString(String s){
		int k=0;
		for(int i=0; i<s.length(); i++){
			k=k+(int)s.charAt(i);
		}
		return k;
	}
	
	void setVectors(){
		int k=0;
		int b=0;
		int len=s.length();
		for( ; k<s.length();){
			b++;
			int w=genWidth(b);
			if(w<=len){
				for(int i=0; i<w; i++){
					bl.insertElementAt(new Integer(b), 0);
					wi.insertElementAt(new Integer(w), 0);
					el.insertElementAt(new Integer(i+1), 0);
					k++;
				}
				len=len-w;
			}else{
				for(int i=0; i<len; i++){
					bl.insertElementAt(new Integer(b), 0);
					wi.insertElementAt(new Integer(w), 0);
					el.insertElementAt(new Integer(i+1), 0);
					k++;
				}
			}
		}
	}
	
	void Subtraction(){
		f="";
		char c;
		int b=0;
		int len=s.length();
		setVectors();
		String s1=invert(s);
		int k=0;
		for( ; k<s.length();){
			b++;
			int w=genWidth(b);
			if(w<=len){
				for(int i=0; i<w; i++){
					int l;
					if(k==0){
						l=genAdd(el.elementAt(k), wi.elementAt(k), bl.elementAt(k), sumString(s1.substring(k+1, s1.length())), 0);
						int a=((int)s1.charAt(k)+256-l)%256;
						c=(char)a;
						f=f+c;
					}
					else if(k==s.length()-1){
						l=genAdd(el.elementAt(k), wi.elementAt(k), bl.elementAt(k), 0, sumString(f));
						int a=((int)s1.charAt(k)+256-l)%256;
						c=(char)a;
						f=f+c;
					}else{
						l=genAdd(el.elementAt(k), wi.elementAt(k), bl.elementAt(k), sumString(s1.substring(k+1, s1.length())), sumString(f));
						int a=((int)s1.charAt(k)+256-l)%256;
						c=(char)a;
						f=f+c;
					}
					k++;
				}
				len=len-w;
			}else{
				for(int i=0; i<len; i++){
					int l;
					if(k==0){
						l=genAdd(el.elementAt(k), wi.elementAt(k), bl.elementAt(k), sumString(s1.substring(k+1, s1.length())), 0);
						int a=((int)s1.charAt(k)+256-l)%256;
						c=(char)a;
						f=f+c;
					}
					else if(k==s.length()-1){
						l=genAdd(el.elementAt(k), wi.elementAt(k), bl.elementAt(k), 0, sumString(f));
						int a=((int)s1.charAt(k)+256-l)%256;
						c=(char)a;
						f=f+c;
					}else{
						l=genAdd(el.elementAt(k), wi.elementAt(k), bl.elementAt(k), sumString(s1.substring(k+1, s1.length())), sumString(f));
						int a=((int)s1.charAt(k)+256-l)%256;
						c=(char)a;
						f=f+c;
					}
					k++;
				}
			}
		}
		ta2.setText(invert(f));
	}
	
	class Encrypt implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ta2.setText("");
			Addition();
		}
		}
	
	class Decrypt implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ta2.setText("");
			Subtraction();
		}
		
	}
}

